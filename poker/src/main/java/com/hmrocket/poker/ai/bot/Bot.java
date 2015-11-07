package com.hmrocket.poker.ai.bot;

import com.hmrocket.poker.Player;
import com.hmrocket.poker.RoundPhase;
import com.hmrocket.poker.Turn;
import com.hmrocket.poker.ai.HandOdds;
import com.hmrocket.poker.ai.HandOddsCalculator;
import com.hmrocket.poker.ai.PlayingStyle;
import com.hmrocket.poker.ai.Strategy;
import com.hmrocket.poker.card.HandHoldem;
import com.hmrocket.poker.card.Rank;

import java.util.Random;

/**
 * Created by hmrocket on 02/11/2015.
 */
public class Bot extends Player {

	private final PlayingStyle playingStyle;
	private final Random random = new Random();
	private final HandOddsCalculator handOddsCalculator;
	private int level;
	private Strategy strategy;

	public Bot(String name, int level, long bankBalance, long cash) {
		super(name, bankBalance, cash);
		this.level = level;
		playingStyle = new PlayingStyle(random.nextFloat(), random.nextFloat());
		handOddsCalculator = new HandOddsCalculator(level * 100);
	}

	@Override
	public void play(long amountToContinue) {

	}

	public void play(Turn turn) {
		if (turn.getPhase() == RoundPhase.FLOP) { // actually it's pre flop
			preflopStrategy(turn);
			return;
		} else {
			int useBluffStrategy = random.nextInt(100);
			if (useBluffStrategy >= 90)
				strategy = Strategy.getPossibleStrategy(handHoldem, turn);
		}
		// calculate hand strength using OddCalculator basing on a strategy
		HandHoldem handHoldemStrategy;
		if (strategy == null || strategy.getStrategy() == Strategy.NONE) {
			// we don't have a strategy to exploit we leave our hand the same
			handHoldemStrategy = handHoldem;
		} else {
			// hand odds will be calculated basing on this
			handHoldemStrategy = strategy.getHandStrategic();
		}

		// calculate hand odd using handHoldemStrategy
		HandOdds handStrength = handOddsCalculator.getHandOdds(turn.getPokerRoundTurnsCount(), handHoldemStrategy);
		// if we do have a strategy our handStrength will be different ==> we will act differently
		makeMove(turn, handStrength.getHandStrength(), calculateRaiseStyle(turn.getAmountToContinue()));
	}

	/**
	 * based on the hand percentage and the amount to continue (added bet = calculateRaiseStyle)
	 *
	 * @param turn
	 * @param winPercentage using HandOdds (MontCarlo) determine winPercentage
	 * @param addedBet      bet to add to the pot
	 */
	protected void makeMove(Turn turn, float winPercentage, long addedBet) {
		float ror = calculateRateOfReturn(winPercentage, calculatePotOdds(turn, addedBet));
		//If RR < 0.8 then 95% fold, 0 % call, 5% raise (bluff)
//		If RR < 1.0 then 80%, fold 5% call, 15% raise (bluff)
//		If RR <1.3 the 0% fold, 60% call, 40% raise
//		Else (RR >= 1.3) 0% fold, 30% call, 70% raise
//		If fold and amount to call is zero, then call.
		// TODO implement more customizable move (ror thresholds should change depending on the player)
		// ror take care in consideration the number of players left
		// ror take in consideration the return value and the risk
		int per = random.nextInt(100);
		if (ror < 0.8) {
			if (per < 95)
				botFold(turn);
			else botRaise(turn, BetType.BLUFF);
		} else if (ror < 1.0) {
			if (ror < 80)
				botFold(turn);
			else if (ror < 85)
				botCall(turn);
			else botRaise(turn, BetType.BLUFF);
		} else if (ror < 1.3) {
			if (ror < 60) botCall(turn);
			else botRaise(turn, BetType.FOR_VALUE);
		} else {
			if (ror < 30) botCall(turn);
			else botRaise(turn, BetType.BEST_HAND);
		}
	}

	// Credit: https://www.pokerschoolonline.com/articles/NLHE-cash-pre-flop-essentials
	void preflopStrategy(Turn turn) {
		// TODO here where PlayingStyle.tight will have an effect
		if (getHandHoldem().getHand().isPair()) {
			switch (getHandHoldem().getHand().getCard1().getRank()) {
				case ACE: // AA
				case KING: // KK
					botRaise(turn, BetType.BEST_HAND);
					break;
				case QUEEN: // QQ
					if (turn.isRaisedAfter()) {
						botCall(turn);
					} else botRaise(turn, BetType.BEST_HAND);
					break;
				case JACK:
				case TEN:
				case NINE: // JJ to 99
					switch (turn.getPokerPosition()) {
						case EARLY:
							if (turn.isRaisedBefore() || turn.isRaisedAfter())
								botCall(turn);
							else if (turn.isEveryoneFoldOnPreflop())
								botFold(turn);
							else botRaise(turn, BetType.FOR_VALUE);
							break;
						case MID_POSITION:
						case LATE:
						case BLINDS:
							if (turn.isRaisedAfter() || turn.isRaisedBefore()) {
								botCall(turn);
							} else {
								botRaise(turn, BetType.FOR_VALUE);
							}
							break;
					}
					break;
				default: // 88 to 22
					switch (turn.getPokerPosition()) {
						case EARLY:
							if (turn.isRaisedBefore() || turn.isRaisedAfter())
								botCall(turn);
							else botFold(turn);
							break;
						case MID_POSITION:
						case BLINDS:
							botCall(turn);
							break;
						case LATE:
							if (turn.isEveryoneFoldOnPreflop())
								botRaise(turn, BetType.FOR_VALUE);
							else botCall(turn);
							break;
					}
					break;
			}
		} else if (getHandHoldem().getHand().getMax().getRank() == Rank.ACE) {
			switch (getHandHoldem().getHand().getMin().getRank()) {
				case KING: //AK
					if (turn.isRaisedAfter())
						// FIXME I don't believe AK should fold if someone re-raised you Make move ?
						botFold(turn);
						//makeMove(turn, handHoldem.winPercentage(), turn.getAmountToContinue());
					else botRaise(turn, BetType.BEST_HAND);
					break;
				case QUEEN: //AQ to AT
				case JACK:
				case TEN:
					switch (turn.getPokerPosition()) {
						case EARLY:
							botFold(turn);
							break;
						case MID_POSITION:
						case BLINDS:
							if (turn.isRaisedAfter() || turn.isRaisedBefore()) {
								botFold(turn);
							} else if (turn.isEveryoneFoldOnPreflop()) {
								botRaise(turn, BetType.FOR_VALUE);
							} else botCall(turn);
							break;
						case LATE:
							if (turn.isRaisedAfter() || turn.isRaisedBefore()) {
								botFold(turn);
							} else {
								botRaise(turn, BetType.FOR_VALUE);
							}
							break;
					}
					break;
				default: // A9 to A2
					if (getHandHoldem().getHand().isSuited()) { // Suited Aces
						switch (turn.getPokerPosition()) {
							case EARLY:
								botFold(turn);
								break;
							case MID_POSITION:
							case BLINDS:
								if (turn.isRaisedBefore() || turn.isRaisedAfter()) {
									botFold(turn);
								} else if (turn.isEveryoneFoldOnPreflop()) {
									botRaise(turn, BetType.FOR_VALUE);
								} else botCall(turn);
								break;
							case LATE:
								if (turn.isRaisedBefore() || turn.isRaisedAfter()) {
									botFold(turn);
								} else botRaise(turn, BetType.FOR_VALUE);
								break;
						}
					} else botFold(turn);
					break;
			}
		} else if (isFaceCards()) {
			switch (turn.getPokerPosition()) {
				case EARLY:
				case MID_POSITION:
					botFold(turn);
					break;
				case LATE:
				case BLINDS:
					if (turn.isRaisedBefore() || turn.isRaisedAfter())
						botFold(turn);
					else if (turn.isEveryoneFoldOnPreflop())
						botRaise(turn, BetType.FOR_VALUE);
					else botCall(turn);
					break;
			}
		} else if (isSuitedConnectors()) { // JTs to 54s
			switch (turn.getPokerPosition()) {
				case EARLY:
				case MID_POSITION:
					botFold(turn);
					break;
				case LATE:
					if (turn.isEveryoneFoldOnPreflop())
						botRaise(turn, BetType.FOR_VALUE);
					else if (turn.isRaisedBefore() || turn.isRaisedAfter())
						botFold(turn);
					else botCall(turn);
					break;
				case BLINDS:
					if (turn.isEveryoneFoldOnPreflop() || turn.isRaisedBefore() || turn.isRaisedAfter())
						botFold(turn);
					else botCall(turn);
					break;
			}
		} else {
			botFold(turn);
		}
	}

	/**
	 * Calculate the Average raise amount for a certain citation
	 *
	 * @param turn    turnStat needed to calculate the bet (for example (4+limpersCount)*minRaise )
	 * @param betType why are we raising ? (this is important
	 * @return
	 */
	private long calculateRawRaise(Turn turn, BetType betType) {
		// TODO determine the rawBet,
		return (long) (turn.getAmountToContinue() * 2 + turn.getMoneyOnTable() * playingStyle.getAggressive());
	}

	/**
	 * @param rawBet represent the bet without taking account of bot aggressive character
	 * @return bet taking careof the agressive character
	 */
	private long calculateRaiseStyle(long rawBet) {
		// TODO here where aggressive attribute will play role
		// generate a formula that use aggressive playingStyle
		return rawBet;
	}

	private float calculatePotOdds(Turn turn, long addedBet) {
		// pots odds = (value you will add to the pot) / (pot value after your add)
		// when potOdds get closer to 0.5 you mean you're put lot of money
		return addedBet / (addedBet + turn.getPotValue());
	}

	private float calculateRateOfReturn(float handStrength, float potOdds) {
		return handStrength / potOdds;
	}

	/**
	 * @return true if hand equal to KQ, KJ, KT, QJ, QT, JT, false otherwise
	 */
	private boolean isFaceCards() {
		return handHoldem.getHand().getMax().getRank().compareTo(Rank.TEN) > 0 &&
				handHoldem.getHand().getMin().getRank().compareTo(Rank.NINE) > 0;
	}

	/**
	 * example AKs to 54s
	 *
	 * @return true if the hand is suited Connector and min higher than 3, false otherwise
	 */
	private boolean isSuitedConnectors() {
		return handHoldem.getHand().isSuited() && handHoldem.getHand().isConnector()
				&& handHoldem.getHand().getMax().getRank().compareTo(Rank.THREE) > 0;
	}

	/**
	 * we add some randomness to the bot raise, 85% raise, 14% call (passive), 1% fold (stupid play)
	 * these values will change depending on bot level
	 *
	 * @param turn
	 */
	public void botRaise(Turn turn, BetType betStyle) {
		int percentage = random.nextInt(100);
		// TODO fix mistake gap to proportional and not simply one take /2 and the other the same
		// mistake gap 30% for level 1; 0% for level 100
		int mistake = calculateMistakeGap(level);
		// 85% ==> 100 - 85 = 15 higher or equal to 15 it's a raise
		// mistake reduce the % of getting raise ==> it mean higher threshold
		int raisePercentageTHold = 15 + mistake;
		// mistake will make the rang of getting call or fold higher by mistake/2 for each
		int callPercentageTHold = 1 + mistake / 2;
		if (percentage >= raisePercentageTHold) {
			// 85% raise if mistake = 0, (85 - mistake/2)%
			super.raise(calculateRaise(turn, betStyle) + bet);
		} else if (percentage >= callPercentageTHold) {
			// 14% call if mistake = 0 (14 + mistake/2)%
			super.call(turn.getAmountToContinue());
		} else {
			// 1% fold if mistake = 0, (1 + mistake/2)%
			super.fold();
		}
	}

	/**
	 * we add some randomness to the bot call, 10% raise, 85% call, 5% fold
	 *
	 * @param turn
	 */
	public void botCall(Turn turn) {
		int percentage = random.nextInt(100);
		// mistake chance 30%
		int mistake = calculateMistakeGap(level);
		// 85% ==> 100 - 85 = 15 higher or equal than 15 it's a call
		int callPercentageTHold = 15 + mistake;
		// 10 % ==> 100 - 85 - 10 = 5, higher or equal to 5 it's a raise
		int raisePercentageTHold = 5 + mistake / 2;
		if (percentage >= callPercentageTHold) {
			// 85% raise if mistake = 0, (85 - mistake/2)%
			super.call(turn.getAmountToContinue());
		} else if (percentage >= raisePercentageTHold) {
			// 14% call if mistake = 0 (14 + mistake/2)%
			super.raise(calculateRaise(turn, BetType.STEAL) + bet);
		} else {
			// 5% ==> 100 - 85 - 10 - 5 = 0, higher or equal to 0 it's a fold
			// 5% fold if mistake = 0, (5 + mistake/2)%
			super.fold();
		}
	}

	private long calculateRaise(Turn turn, BetType betStyle) {
		return calculateRaiseStyle(calculateRawRaise(turn, betStyle));
	}

	/**
	 * we add some randomness to the bot call, 5% raise (bluff stupidly), 1% call, 94% fold
	 *
	 * @param turn
	 */
	public void botFold(Turn turn) {
		int percentage = random.nextInt(100);
		// mistake gap 30% for level 1; 0% for level 100
		int mistake = calculateMistakeGap(level);
		// 94% ==> 100 - 94 = 6 higher or equal to 6 it's a fold
		// mistake reduce the % of getting fold ==> it mean higher threshold
		int foldPercentageTHold = 6 + mistake;
		// mistake will make the rang of getting call or fold higher by mistake/2 for each
		int raisePercentageTHold = 1 + mistake / 2;
		if (percentage >= foldPercentageTHold) {
			// 94% fold if mistake = 0, (94 - mistake/2)%
			super.fold();
		} else if (percentage >= raisePercentageTHold) {
			// 5% raise if mistake = 0 (5 + mistake/2)%
			super.raise(calculateRaise(turn, BetType.BLUFF) + bet);
		} else {
			// 1% call if mistake = 0, (1 + mistake/2)%
			super.call(turn.getAmountToContinue());
		}
	}

	/**
	 * 0% - 30% mistake chance
	 *
	 * @param botLevel the higher the level the less the bot will make a mistake
	 * @return
	 */
	private int calculateMistakeGap(int botLevel) {
		//int maxBotLevel = 100; maxBotLevel / 4 to be in rang 0 - 1
		// level bot start 0 and can reach 100
		return (int) (30 * Math.cos(Math.PI * botLevel / 200));
	}

	enum BetType {
		/**
		 * min amount to force the players to fold
		 * (use it only if raiseBefore = false and good position
		 */
		BLUFF,
		/**
		 * raise to announces to the poker table that you have a strong hand.
		 */
		STEAL,
		/**
		 * Raise for Value or NORMAL is min amount raise like 4 * minBet
		 */
		FOR_VALUE,
		/**
		 * raise enough money to win big (for example if someone raised take that into account)
		 */
		BEST_HAND,
	}

}
