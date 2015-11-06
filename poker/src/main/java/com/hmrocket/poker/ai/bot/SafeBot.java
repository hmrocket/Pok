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
	public void play(long amountToContinue) {

	}

	// Credit: https://www.pokerschoolonline.com/articles/NLHE-cash-pre-flop-essentials
	void preflopStrategy(Turn turn) {
		// TODO here where PlayingStyle.tight will have an effect
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

	private long calculateRaise(Turn turn) {
		// TODO here where aggressive attribute will play role
		return turn.getAmountToContinue() * 3;
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
	public void botRaise(Turn turn) {
		int percentage = random.nextInt(100);
		// mistake gap 30% for level 1; 0% for level 100
		int mistake = calculateMistakeGap(level);
		// 85% ==> 100 - 85 = 15 higher or equal to 15 it's a raise
		// mistake reduce the % of getting raise ==> it mean higher threshold
		int raisePercentageTHold = 15 + mistake;
		// mistake will make the rang of getting call or fold higher by mistake/2 for each
		int callPercentageTHold = 1 + mistake / 2;
		if (percentage >= raisePercentageTHold) {
			// 85% raise if mistake = 0, (85 - mistake/2)%
			super.raise(calculateRaise(turn));
		} else if (percentage >= callPercentageTHold) {
			// 14% call if mistake = 0 (14 + mistake/2)%
			super.call(turn.getAmountToContinue());
		} else {
			// 1% fold if mistake = 0, (1 + mistake/2)%
			super.fold();
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

	/**
	 * we add some randomness to the bot call, 10% raise, 85% call, 5% fold
	 *
	 * @param turn
	 * @param amount
	 */
	public void botCall(Turn turn, long amount) {
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
			super.raise(calculateRaise(turn));
		} else {
			// 5% ==> 100 - 85 - 10 - 5 = 0, higher or equal to 0 it's a fold
			// 5% fold if mistake = 0, (5 + mistake/2)%
			super.fold();
		}
	}

	/**
	 * we add some randomness to the bot call, 5% raise (bluff stupidly), 1% call, 94% fold
	 *
	 * @param turn
	 * @param amount
	 */
	public void botFold(Turn turn, long amount) {
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
			super.raise(calculateRaise(turn));
		} else {
			// 1% call if mistake = 0, (1 + mistake/2)%
			super.call(turn.getAmountToContinue());
		}
	}

}
