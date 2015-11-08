package com.hmrocket.poker.ai;

import com.hmrocket.poker.Turn;
import com.hmrocket.poker.card.Card;
import com.hmrocket.poker.card.Hand;
import com.hmrocket.poker.card.HandHoldem;
import com.hmrocket.poker.card.Rank;
import com.hmrocket.poker.card.Suit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hmrocket on 04/11/2015.
 */
public final class Strategy {

	public static int NONE = 0;
	public static int STEAL_FLUSH = 1;
	public static int STEAL_STRAIGHT = 2;
	public static int STEAL_STRAIGHT_FLUSH = 3;
	public static int CHECK_RAISE = 10;

	private HandHoldem handStrategic;
	private int strategy;

	/*
	 * This Constructor must remain private cause this class is actually a Factory strategy
	 */
	private Strategy(int strategy, HandHoldem perfectHandScenario) {
		this.strategy = strategy;
		this.handStrategic = perfectHandScenario;
	}

	/**
	 * @param handHoldem
	 * @param turn
	 * @return a possible strategy (mainly a fake Hand that the bot will use as a reference for his bets)
	 */
	public static Strategy getPossibleStrategy(HandHoldem handHoldem, Turn turn) {
		// my favorite one is STEAL I use it often
		// when no one has raised but there is a possible Flush or straight at the begging
		// Conditions (3): mid or late position, no one has raised; (e3) possible flush or straight
		if (turn.getPokerPosition() != PokerPosition.EARLY && !turn.isRaisedBefore()) {
			Hand hand = checkPossibleStealFlush(handHoldem);
			if (hand != null) {
				// we can use a strategy and act like we own this hand
				return new Strategy(STEAL_FLUSH, new HandHoldem(hand, handHoldem.getCommunityCards()));
			} else {
				hand = checkPossibleStealStraight(handHoldem);
				if (hand != null) {
					// we can use a strategy and act like we own this hand
					return new Strategy(STEAL_STRAIGHT, new HandHoldem(hand, handHoldem.getCommunityCards()));
				}
			}
		}
		return null;
	}

	/**
	 * Check if we can steal the pot by acting like someone who has two suited Cards
	 *
	 * @param handHoldem
	 * @return If stealing flush is possible then it return a strategic Hand suited for this type of play,
	 * null otherwise
	 */
	private static Hand checkPossibleStealFlush(HandHoldem handHoldem) {
		int[] suitsFrequency = new int[4];
		if (handHoldem.getFlop() == null)
			return null;
		suitsFrequency[handHoldem.getFlop().getCard1().getSuit().ordinal()] += 1;
		suitsFrequency[handHoldem.getFlop().getCard2().getSuit().ordinal()] += 1;
		suitsFrequency[handHoldem.getFlop().getCard3().getSuit().ordinal()] += 1;
		if (handHoldem.getTurn() != null) {
			suitsFrequency[handHoldem.getTurn().getSuit().ordinal()] += 1;
		}
		// before the river we can act like we have suited card and we are waiting for the last
		// card to construct a flush
		int minSuitFrequencyToSteal = 2;
		// after the river is flipped there must be 3 total same card
		// or we can't continue Strategy stealing
		if (handHoldem.getRiver() != null) {
			suitsFrequency[handHoldem.getRiver().getSuit().ordinal()] += 1;
			minSuitFrequencyToSteal = 3;
		}

		for (int i = 0; i < suitsFrequency.length; i++) {
			if (suitsFrequency[i] >= minSuitFrequencyToSteal) {
				Suit mostFreqSuit = Suit.values()[i];
				int suitsFrenqInHand = (mostFreqSuit.equals(handHoldem.getHand().getCard1().getSuit()) ?
						2 : 1) -
						(mostFreqSuit.equals(handHoldem.getHand().getCard2().getSuit()) ?
								0 : 1);
				// For steal to work we have to make sure that the suit isn't available more than it should
				// for example if 5 suited card on the table (shared an we don't have any that becomes risky)
				if (suitsFrequency[i] <= 3 + suitsFrenqInHand) {
					// at this point the bot must play like he has these cards
					Card card1 = new Card(handHoldem.getHand().getCard1().getRank(), mostFreqSuit);
					Card card2 = new Card(handHoldem.getHand().getCard2().getRank(), mostFreqSuit);
					return new Hand(card1, card2);

				} else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * A copy from HandScoreCalculator
	 * Search if there is more than 2 consecutive cards to construct (straight)
	 *
	 * @param handHoldem hand
	 * @return the higher card rank of the straight, null otherwise
	 */
	private static Hand checkPossibleStealStraight(HandHoldem handHoldem) {
		List<Card> cardList = new ArrayList<>();
		if (handHoldem.getFlop() != null) {
			cardList.addAll(Arrays.asList(handHoldem.getFlop().getCards()));
		}
		if (handHoldem.getTurn() != null)
			cardList.add(handHoldem.getTurn());
		if (handHoldem.getRiver() != null) {
			cardList.add(handHoldem.getRiver());
		}

		Card[] cards = cardList.toArray(new Card[cardList.size()]);

		// Card implements comparable. Rank Comparative
		for (int i = 0; i < cards.length; i++) {
			Rank currentRank = cards[i].getRank();
			Rank rank1 = null;
			Rank rank2 = null;
			int possibleConsecutiveCards = 1;
			int j;
			boolean canJump = true;
			for (j = i + 1; j < cards.length; j++) {
				Rank nextCardRank = cards[j].getRank();
				int rankDiff = currentRank.ordinal() - nextCardRank.ordinal();
				if (rankDiff == 1) { // if rank and nextRank are consecutive
					possibleConsecutiveCards++;
					currentRank = nextCardRank;
				} else if (rankDiff != 0) { // if not same rank break, (straight chain is broken)
					if (canJump && rankDiff <= 3) { // we are allowed only one jump of 1 step or 2 steps Ranks max
						if (rankDiff == 2) {
							// one card is missing in the straight chain
							rank2 = Rank.values()[nextCardRank.ordinal() + 1];
							rank1 = null;
						} else {
							// diff is 3 like A - J
							// two cards are missing in the chain straight
							rank2 = Rank.values()[nextCardRank.ordinal() + 1];
							rank1 = Rank.values()[nextCardRank.ordinal() + 2];

						}
						canJump = false;
						possibleConsecutiveCards++;
						currentRank = nextCardRank;
					} else {
						break;
					}
				}
				if (possibleConsecutiveCards >= 3)
					break;
			}
			// we are trying to steel the pot by acting like we do have the two missing cards needed
			// to construct a straight
			if (possibleConsecutiveCards < 2) {
				// NO - startNewHand the next loop from card j
				//i = j - 1;
				continue;
			} else {
				// 3 card or more must be close to each other to be able to fake the straight
				boolean straightFrom5to1 = (possibleConsecutiveCards == 2
						&& cards[i].getRank() == Rank.FIVE && cards[0].getRank() == Rank.ACE);
				if (straightFrom5to1)
					possibleConsecutiveCards++;
				if (possibleConsecutiveCards > 2) {
					// return the strategic fake hand
					if (canJump) {
						// if canJump never set to false it mean that we fill the straight with
						// two card at top
						rank1 = Rank.values()[cards[i].getRank().ordinal() + 2];
						rank2 = Rank.values()[cards[i].getRank().ordinal() + 1];
					} else {
						// if canJump false, means that the Card missing for the straight are in the sequence
						// rank1 and rank2 might be both filled
						if (rank1 == null) {
							rank1 = Rank.values()[cards[i].getRank().ordinal() + 1];
						}
						// return strategy hand
					}
					return new Hand(new Card(rank1, Suit.DIAMONDS), new Card(rank2, Suit.HEARTS));
				}
			}
		}
		return null;
	}

	/**
	 * Get strategic hand
	 *
	 * @return
	 */
	public HandHoldem getHandStrategic() {
		return handStrategic;
	}

	/**
	 * get strategy
	 *
	 * @return
	 */
	public int getStrategy() {
		return strategy;
	}

}
