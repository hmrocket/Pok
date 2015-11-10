package com.hmrocket.poker.ai.bot;

import com.hmrocket.poker.Turn;
import com.hmrocket.poker.card.Card;
import com.hmrocket.poker.card.Rank;

/**
 * Created by hmrocket on 09/11/2015.
 */
public class SafeBotRakeBackPros extends SafeBot {

	public SafeBotRakeBackPros(String name, long bankBalance, long cash) {
		super(name, bankBalance, cash);
	}

	// http://www.rakebackpros.net/texas-holdem-starting-hands/
	@Override
	void preflopStrategy(Turn turn) {
		// here where PlayingStyle.tight will have an effect
		// but SafeBot play very tight
		Zone zone = getZone();
		switch (zone) {
			case BLUE:
				raise(calculateRaise(turn));
				break;
			case GREEN:
				// early position shouldn't play
				switch (turn.getPokerPosition()) {
					case EARLY:
						if (turn.isRaisedBefore())
							fold();
						else call(turn.getAmountToContinue());
						break;
					case BLINDS:
						if (turn.isRaisedBefore())
							call(turn.getAmountToContinue());
						else raise(calculateRaise(turn));
						break;
					case MID_POSITION:
						if (turn.isRaisedBefore())
							call(turn.getAmountToContinue());
						else raise(calculateRaise(turn));
						break;
					case LATE:
						if (turn.isRaisedAfter())
							call(turn.getAmountToContinue());
						else raise(calculateRaise(turn));
						break;
					default:
						fold();
				}
			case YELLOW:
				// I should only play if I'm Late position
				switch (turn.getPokerPosition()) {
					case EARLY:
						if (turn.isRaisedBefore())
							fold();
						else call(turn.getAmountToContinue());
						break;
					case MID_POSITION:
						if (turn.getMinBet() * 3 < turn.getAmountToContinue())
							fold();
						else call(turn.getAmountToContinue());
						break;
					case BLINDS:
						if (turn.getMinBet() * 3 < turn.getAmountToContinue())
							fold();
						else call(turn.getAmountToContinue());
						break;
					case LATE:
						call(turn.getAmountToContinue());
						break;
					default:
						fold();
						break;
				}
				break;
			case RED:
				// I should fold
				switch (turn.getPokerPosition()) {
					case EARLY:
						fold();
						break;
					case MID_POSITION:
						if (turn.isRaisedBefore() || turn.isEveryoneFoldOnPreflop()) {
							fold();
						} else call(turn.getAmountToContinue());
						break;
					case BLINDS:
					case LATE:
						if (turn.isRaisedBefore())
							fold();
						else call(turn.getAmountToContinue());
						break;
					default:
						fold();
						break;
				}
				break;
		}
	}

	// http://www.rakebackpros.net/texas-holdem-starting-hands/
	protected Zone getZone() {
		Card min = handHoldem.getHand().getMin();
		Card max = handHoldem.getHand().getMax();

		if (handHoldem.getHand().isPair()) {
			// Pair 7 and higher
			if (Rank.SEVEN.compareTo(handHoldem.getHand().getCard1().getRank()) >= 0) {
				return Zone.BLUE;
			} else if (Rank.FIVE.compareTo(handHoldem.getHand().getCard1().getRank()) >= 0) {
				// Pair 5,6
				return Zone.GREEN;
			} else return Zone.YELLOW; //Pair 4-2
		} else if (handHoldem.getHand().isSuited()) {
			// Suited listed
			switch (max.getRank()) {
				case ACE:
					if (min.getRank().compareTo(Rank.NINE) > 0)
						return Zone.BLUE;
					else if (min.getRank().compareTo(Rank.FIVE) > 0)
						return Zone.GREEN;
					else return Zone.YELLOW;
				case KING:
					if (min.getRank().compareTo(Rank.NINE) > 0)
						return Zone.BLUE;
					else if (min.getRank().compareTo(Rank.EIGHT) > 0)
						return Zone.GREEN;
					else return Zone.YELLOW;
				case QUEEN:
					if (min.getRank().compareTo(Rank.NINE) > 0)
						return Zone.BLUE;
					else if (min.getRank().compareTo(Rank.SEVEN) > 0)
						return Zone.GREEN;
					else return Zone.RED;
				case JACK:
					if (min.getRank().compareTo(Rank.EIGHT) > 0)
						return Zone.BLUE;
					else if (min.getRank().compareTo(Rank.SEVEN) > 0)
						return Zone.GREEN;
					else if (min.getRank().compareTo(Rank.SIX) > 0)
						return Zone.YELLOW;
					else return Zone.RED;
				case TEN:
					if (min.getRank().compareTo(Rank.EIGHT) > 0)
						return Zone.BLUE;
					else if (min.getRank().compareTo(Rank.SEVEN) > 0)
						return Zone.GREEN;
					else if (min.getRank().compareTo(Rank.SIX) > 0)
						return Zone.YELLOW;
					else return Zone.RED;
				case NINE:
					if (min.getRank().compareTo(Rank.SEVEN) > 0)
						return Zone.GREEN;
					else if (min.getRank().compareTo(Rank.FIVE) > 0)
						return Zone.YELLOW;
					else return Zone.RED;
				case EIGHT:
					if (min.getRank().compareTo(Rank.FIVE) > 0)
						return Zone.YELLOW;
					else return Zone.RED;
				case SEVEN:
					if (min.getRank().compareTo(Rank.FOUR) > 0)
						return Zone.YELLOW;
					else return Zone.RED;
				case SIX:
					if (min.getRank() == Rank.FIVE)
						return Zone.YELLOW;
					else return Zone.RED;
				case FIVE:
					if (min.getRank() == Rank.FOUR)
						return Zone.YELLOW;
					else return Zone.RED;
				case FOUR:
				case THREE:
				default:
					return Zone.RED;
			}
		} else {
			// Not Suited listed
			switch (max.getRank()) {
				case ACE:
					if (min.getRank().compareTo(Rank.NINE) > 0)
						return Zone.BLUE;
					else if (min.getRank().compareTo(Rank.SIX) > 0)
						return Zone.YELLOW;
					else return Zone.RED;
				case KING:
					if (min.getRank().compareTo(Rank.TEN) > 0)
						return Zone.BLUE;
					else if (min.getRank() == Rank.TEN)
						return Zone.GREEN;
					else if (min.getRank() == Rank.NINE)
						return Zone.YELLOW;
					else return Zone.RED;
				case QUEEN:
					if (min.getRank().compareTo(Rank.NINE) > 0)
						return Zone.GREEN;
					else if (min.getRank() == Rank.NINE)
						return Zone.YELLOW;
					else return Zone.RED;
				case JACK:
					if (min.getRank() == Rank.TEN)
						return Zone.GREEN;
					else if (min.getRank().compareTo(Rank.SEVEN) > 0)
						return Zone.YELLOW;
					else return Zone.RED;
				case TEN:
					if (min.getRank().compareTo(Rank.SEVEN) > 0)
						return Zone.YELLOW;
					else return Zone.RED;
				case NINE:
					if (min.getRank().compareTo(Rank.SIX) > 0)
						return Zone.YELLOW;
					else return Zone.RED;
				case EIGHT:
					if (min.getRank() == Rank.SEVEN)
						return Zone.YELLOW;
					else return Zone.RED;
				default:
					return Zone.RED;
			}
		}
	}

	protected enum Zone {
		BLUE,
		GREEN,
		YELLOW,
		RED
	}
}
