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

	private long calculateRaise(Turn turn) {
		// here where aggressive attribute will play role
		// but SafeBot has aggression 0
		return Math.max(turn.getAmountToContinue() * 4, turn.getPotValue() / 4);
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
