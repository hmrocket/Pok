package com.hmrocket.poker.ai.bot;

import com.hmrocket.poker.PokerTools;
import com.hmrocket.poker.Turn;
import com.hmrocket.poker.card.Rank;

/**
 * Created by hmrocket on 09/11/2015.
 */
public class SafeBotM extends SafeBot {

	public SafeBotM(String name, long bankBalance, long cash) {
		super(name, bankBalance, cash);
	}

	// Credit: Mhamed
	@Override
	void preflopStrategy(Turn turn) {
		// here where PlayingStyle.tight will have an effect
		// but SafeBot play very tight
		if (getHandHoldem().getHand().isPair()) {
			switch (getHandHoldem().getHand().getCard1().getRank()) {
				case ACE: // AA
				case KING: // KK
					if (turn.getPokerRoundTurnsCount() / turn.getPlayerCount() >= 2)
						allIn();
					else raise(calculateRaise(turn));
					break;
				case QUEEN: // QQ
					if (turn.isRaisedAfter() && turn.getRoundPlayerRaised() > 4) {
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
								raise(calculateRaise(turn));
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
								if (turn.getAmountToContinue() / turn.getMinBet() >= 50)
									fold();
								else call(turn.getAmountToContinue());
							else raise(calculateRaise(turn));
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
							call(turn.getAmountToContinue());
							break;
						case MID_POSITION:
						case BLINDS:
							if (turn.isRaisedAfter()) {
								if (bet / turn.getAmountToContinue() > 0.05) {
									call(turn.getAmountToContinue());
								} else fold();
							} else if (!turn.isRaisedBefore()) {
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
								if (PokerTools.calculatePotOdds(turn, turn.getAmountToContinue()) > .5)
									fold();
								else
									call(turn.getAmountToContinue());
								break;
							case MID_POSITION:
							case BLINDS:
								if (turn.isRaisedBefore() || turn.isRaisedAfter()) {
									if (PokerTools.calculatePotOdds(turn, turn.getAmountToContinue()) > .4)
										fold();
									else call(turn.getAmountToContinue());
								} else if (!turn.isRaisedBefore()) {
									raise(calculateRaise(turn));
								} else call(turn.getAmountToContinue());
								break;
							case LATE:
								if (turn.isRaisedBefore() || turn.isRaisedAfter()) {
									if (PokerTools.calculatePotOdds(turn, turn.getAmountToContinue()) > .5)
										fold();
									else call(turn.getAmountToContinue());
								} else raise(calculateRaise(turn));
								break;
						}
					} else if (PokerTools.calculatePotOdds(turn, turn.getAmountToContinue()) > .3)
						fold();
					else call(turn.getAmountToContinue());
					break;
			}
		} else if (isFaceCards()) {
			switch (turn.getPokerPosition()) {
				case EARLY:
				case MID_POSITION:
					if (PokerTools.calculatePotOdds(turn, turn.getAmountToContinue()) > .3)
						fold();
					else call(turn.getAmountToContinue());
					break;
				case LATE:
				case BLINDS:
					if (turn.isRaisedBefore() || turn.isRaisedAfter())
						if (PokerTools.calculatePotOdds(turn, turn.getAmountToContinue()) > .5)
							fold();
						else call(turn.getAmountToContinue());
					else if (turn.isEveryoneFoldOnPreflop())
						raise(calculateRaise(turn));
					else call(turn.getAmountToContinue());
					break;
			}
		} else if (isSuitedConnectors()) { // JTs to 54s
			switch (turn.getPokerPosition()) {
				case EARLY:
				case MID_POSITION:
					if (PokerTools.calculatePotOdds(turn, turn.getAmountToContinue()) > .4)
						fold();
					else call(turn.getAmountToContinue());
					break;
				case LATE:
					if (turn.isEveryoneFoldOnPreflop())
						raise(calculateRaise(turn));
					else if (turn.isRaisedBefore() || turn.isRaisedAfter())
						if (PokerTools.calculatePotOdds(turn, turn.getAmountToContinue()) > .5)
							fold();
						else call(turn.getAmountToContinue());
					else call(turn.getAmountToContinue());
					break;
				case BLINDS:
					if (turn.isEveryoneFoldOnPreflop() || turn.isRaisedBefore() || turn.isRaisedAfter())
						if (PokerTools.calculatePotOdds(turn, turn.getAmountToContinue()) > .3)
							fold();
						else call(turn.getAmountToContinue());
					else call(turn.getAmountToContinue());
					break;
			}
		} else {
			if (PokerTools.calculatePotOdds(turn, turn.getAmountToContinue()) > .3)
				fold();
			else call(turn.getAmountToContinue());
		}
	}
}
