package com.hmrocket.poker.ai.bot;

import com.hmrocket.poker.Player;
import com.hmrocket.poker.Turn;
import com.hmrocket.poker.ai.PlayingStyle;
import com.hmrocket.poker.card.Rank;

import java.util.Random;

/**
 * Created by hmrocket on 02/11/2015.
 */
public final class SafeBot extends Player {

	private static final PlayingStyle playingStyle = new PlayingStyle(0, 1);
	private final Random random = new Random();
	private int level;

	public SafeBot(String name, long bankBalance, long cash) {
		super(name, bankBalance, cash);
	}

	@Override
	public void play(Turn turn) {

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
				fold();
			else raise(calculateRaise(turn));
		} else if (ror < 1.0) {
			if (ror < 80)
				fold();
			else if (ror < 85)
				call(turn.getAmountToContinue());
			else raise(calculateRaise(turn));
		} else if (ror < 1.3) {
			if (ror < 60) call(turn.getAmountToContinue());
			else raise(calculateRaise(turn));
		} else {
			if (ror < 30) call(turn.getAmountToContinue());
			else raise(calculateRaise(turn));
		}
	}

	private float calculateRateOfReturn(float handStrength, float potOdds) {
		return handStrength / potOdds;
	}

	private float calculatePotOdds(Turn turn, long addedBet) {
		// pots odds = (value you will add to the pot) / (pot value after your add)
		// when potOdds get closer to 0.5 you mean you're put lot of money
		return addedBet / (addedBet + turn.getPotValue());
	}

	private long calculateRaise(Turn turn) {
		// here where aggressive attribute will play role
		// but SafeBot has aggression 0
		return Math.max(turn.getAmountToContinue() * 4, turn.getPotValue() / 4);
	}

	// Credit: https://www.pokerschoolonline.com/articles/NLHE-cash-pre-flop-essentials
	void preflopStrategy(Turn turn) {
		// here where PlayingStyle.tight will have an effect
		// but SafeBot play very tight
		if (getHandHoldem().getHand().isPair()) {
			switch (getHandHoldem().getHand().getCard1().getRank()) {
				case ACE: // AA
				case KING: // KK
					raise(calculateRaise(turn));
					break;
				case QUEEN: // QQ
					if (turn.isRaisedAfter()) {
						call(turn.getAmountToContinue());
					} else raise(calculateRaise(turn));
					break;
				case JACK:
				case TEN:
				case NINE: // JJ to 99
					switch (turn.getPokerPosition()) {
						case EARLY:
							if (turn.isRaisedBefore() || turn.isRaisedAfter())
								call(turn.getAmountToContinue());
							else if (turn.isEveryoneFoldOnPreflop())
								fold();
							else raise(calculateRaise(turn));
							break;
						case MID_POSITION:
						case LATE:
						case BLINDS:
							if (turn.isRaisedAfter() || turn.isRaisedBefore()) {
								call(turn.getAmountToContinue());
							} else {
								raise(calculateRaise(turn));
							}
							break;
					}
					break;
				default: // 88 to 22
					switch (turn.getPokerPosition()) {
						case EARLY:
							if (turn.isRaisedBefore() || turn.isRaisedAfter())
								call(turn.getAmountToContinue());
							else fold();
							break;
						case MID_POSITION:
						case BLINDS:
							call(turn.getAmountToContinue());
							break;
						case LATE:
							if (turn.isEveryoneFoldOnPreflop())
								raise(calculateRaise(turn));
							else call(turn.getAmountToContinue());
							break;
					}
					break;
			}
		} else if (getHandHoldem().getHand().getMax().getRank() == Rank.ACE) {
			switch (getHandHoldem().getHand().getMin().getRank()) {
				case KING: //AK
					if (turn.isRaisedAfter())
						fold();
					else raise(calculateRaise(turn));
					break;
				case QUEEN: //AQ to AT
				case JACK:
				case TEN:
					switch (turn.getPokerPosition()) {
						case EARLY:
							fold();
							break;
						case MID_POSITION:
						case BLINDS:
							if (turn.isRaisedAfter() || turn.isRaisedBefore()) {
								fold();
							} else if (turn.isEveryoneFoldOnPreflop()) {
								raise(calculateRaise(turn));
							} else call(turn.getAmountToContinue());
							break;
						case LATE:
							if (turn.isRaisedAfter() || turn.isRaisedBefore()) {
								fold();
							} else {
								raise(calculateRaise(turn));
							}
							break;

					}
					break;
				default: // A9 to A2
					if (getHandHoldem().getHand().isSuited()) { // Suited Aces
						switch (turn.getPokerPosition()) {
							case EARLY:
								fold();
								break;
							case MID_POSITION:
							case BLINDS:
								if (turn.isRaisedBefore() || turn.isRaisedAfter()) {
									fold();
								} else if (turn.isEveryoneFoldOnPreflop()) {
									raise(calculateRaise(turn));
								} else call(turn.getAmountToContinue());
								break;
							case LATE:
								if (turn.isRaisedBefore() || turn.isRaisedAfter()) {
									fold();
								} else raise(calculateRaise(turn));
								break;
						}
					} else fold();
					break;
			}
		} else if (isFaceCards()) {
			switch (turn.getPokerPosition()) {
				case EARLY:
				case MID_POSITION:
					fold();
					break;
				case LATE:
				case BLINDS:
					if (turn.isRaisedBefore() || turn.isRaisedAfter())
						fold();
					else if (turn.isEveryoneFoldOnPreflop())
						raise(calculateRaise(turn));
					else call(turn.getAmountToContinue());
					break;
			}
		} else if (isSuitedConnectors()) { // JTs to 54s
			switch (turn.getPokerPosition()) {
				case EARLY:
				case MID_POSITION:
					fold();
					break;
				case LATE:
					if (turn.isEveryoneFoldOnPreflop())
						raise(calculateRaise(turn));
					else if (turn.isRaisedBefore() || turn.isRaisedAfter())
						fold();
					else call(turn.getAmountToContinue());
					break;
				case BLINDS:
					if (turn.isEveryoneFoldOnPreflop() || turn.isRaisedBefore() || turn.isRaisedAfter())
						fold();
					else call(turn.getAmountToContinue());
					break;
			}
		} else {
			fold();
		}
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

}
