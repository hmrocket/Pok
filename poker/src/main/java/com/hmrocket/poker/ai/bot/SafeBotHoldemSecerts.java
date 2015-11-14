package com.hmrocket.poker.ai.bot;

import com.hmrocket.poker.Turn;
import com.hmrocket.poker.card.Card;
import com.hmrocket.poker.card.Hand;
import com.hmrocket.poker.card.Rank;

/**
 * Created by hmrocket on 10/11/2015.
 */
public class SafeBotHoldemSecerts extends SafeBot {

	public SafeBotHoldemSecerts(String name, long bankBalance, long cash) {
		super(name, bankBalance, cash);
	}

	// Credit: http://www.holdemsecrets.com/preflop.htm
	@Override
	void preflopStrategy(Turn turn) {
		Zone startingHandzone = getZone();
		Hand hand = handHoldem.getHand();
		switch (turn.getPokerPosition()) {
			case EARLY:
				switch (startingHandzone) {
					case GREEN:
						raCa(turn);
						break;
					case GREEN_LIGHT:
						if (hand.isSuited() || hand.isPair()) {
							c1C2(turn);
						} else fold();
						break;
					case YELLOW:
						fold();
						break;
					case RED:
						fold();
						break;
				}
				break;
			case MID_POSITION:
				switch (startingHandzone) {
					case GREEN:
						raCa(turn);
						break;
					case GREEN_LIGHT:
						if (hand.isSuited() || hand.isPair())
							c2C3(turn);
						else c1C2(turn);
						break;
					case YELLOW:
						if (hand.isSuited() || hand.isPair())
							c1C2(turn);
						else fold();
						break;
					case RED:
						fold();
						break;
				}
				break;
			case LATE:
				switch (startingHandzone) {
					case GREEN:
						raCa(turn);
						break;
					case GREEN_LIGHT:
						if (hand.isSuited() || hand.isPair())
							c3Ca(turn);
						else c2C3(turn);
						break;
					case YELLOW:
						if (hand.isSuited() || hand.isPair())
							c3Ca(turn);
						else c2C2(turn);
						break;
					case RED:
						if (hand.isSuited())
							c2C3(turn);
						break;
				}
				break;
			case BLINDS:
				switch (startingHandzone) {
					case GREEN:
						r2Ca(turn);
						break;
					case GREEN_LIGHT:
						r2Ca(turn);
						break;
					case YELLOW:
						if (hand.isSuited() || hand.isPair())
							r2Ca(turn);
						else c1C1(turn);
						break;
					case RED:
						if (hand.isSuited())
							c1C1(turn);
						break;
				}
				break;
		}
	}

	// Credit: http://www.holdemsecrets.com/preflop.htm
	protected Zone getZone() {
		Card min = handHoldem.getHand().getMin();
		Card max = handHoldem.getHand().getMax();

		if (handHoldem.getHand().isPair()) {
			// Pair Q and higher
			if (Rank.QUEEN.compareTo(handHoldem.getHand().getCard1().getRank()) >= 0) {
				return Zone.GREEN;
			} else if (Rank.EIGHT.compareTo(handHoldem.getHand().getCard1().getRank()) >= 0) {
				// Pair J,.., 8
				return Zone.GREEN_LIGHT;
			} else {
				// Pair 7,..,2
				return Zone.YELLOW;
			}
		} else {
			switch (max.getRank()) {
				case ACE:
					if (min.getRank() == Rank.KING)
						return Zone.GREEN;
					else if (min.getRank().compareTo(Rank.TEN) >= 0)
						return Zone.GREEN_LIGHT;
					else if (min.getRank().compareTo(Rank.SEVEN) >= 0)
						return Zone.YELLOW;
					else return Zone.RED;
				case KING:
					if (min.getRank().compareTo(Rank.JACK) >= 0)
						return Zone.GREEN_LIGHT;
					else if (min.getRank().compareTo(Rank.EIGHT) > 0)
						return Zone.YELLOW;
					else return Zone.RED;
				case QUEEN:
					if (min.getRank() == Rank.JACK)
						return Zone.GREEN_LIGHT;
					else if (min.getRank() == Rank.TEN)
						return Zone.YELLOW;
					else return Zone.RED;
				case JACK:
					if (min.getRank() == Rank.TEN)
						return Zone.GREEN_LIGHT;
					else if (min.getRank() == Rank.NINE)
						return Zone.YELLOW;
					else return Zone.RED;
				case TEN:
					if (min.getRank() == Rank.NINE)
						return Zone.YELLOW;
					else return Zone.RED;
				case NINE:
					if (min.getRank() == Rank.EIGHT)
						return Zone.YELLOW;
					else return Zone.RED;
				default:
					return Zone.RED;
			}
		}
	}

	protected void raCa(Turn turn) {
		long raiseAny = 3 * turn.getMinBet() + random.nextInt((int) cash);
		if (raiseAny > turn.getAmountToContinue())
			raise(raiseAny);
		else call(turn.getAmountToContinue());
	}

	protected void c1C2(Turn turn) {
		long raiseLimit = 2 * turn.getMinBet();
		if (raiseLimit < turn.getAmountToContinue())
			fold();
		else call(turn.getAmountToContinue());
	}

	protected void c2C3(Turn turn) {
		long raiseLimit = 3 * turn.getMinBet();
		if (raiseLimit < turn.getAmountToContinue())
			fold();
		else call(turn.getAmountToContinue());
	}

	protected void c3Ca(Turn turn) {
		long raise = 4 * turn.getMinBet();
		if (raise > turn.getAmountToContinue())
			raise(raise);
		else call(turn.getAmountToContinue());
	}

	protected void c2C2(Turn turn) {
		long raiseLimit = 2 * turn.getMinBet();
		if (raiseLimit < turn.getAmountToContinue())
			fold();
		else call(turn.getAmountToContinue());
	}

	protected void r2Ca(Turn turn) {
		long raise = 3 * turn.getMinBet();
		if (raise > turn.getAmountToContinue())
			raise(raise);
		else call(turn.getAmountToContinue());
	}

	protected void c1C1(Turn turn) {
		long raiseLimit = turn.getMinBet();
		if (raiseLimit < turn.getAmountToContinue())
			fold();
		else call(turn.getAmountToContinue());
	}

	protected enum Zone {
		GREEN,
		GREEN_LIGHT,
		YELLOW,
		RED
	}
}
