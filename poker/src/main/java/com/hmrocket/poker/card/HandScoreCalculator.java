package com.hmrocket.poker.card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * poker arnaqueur - poker crook
 * Created by hmrocket on 04/10/2015.
 */
public final class HandScoreCalculator {

	private HandScoreCalculator() {
	}

	public static HandScore getHandScore(Hand hand, CommunityCards communityCards) {
		if (communityCards != null) {
			return getHandScore(hand, communityCards.getFlop(), communityCards.getTurn(), communityCards.getRiver());
		} else {
			return getHandScore(hand);
		}
	}

	public static HandScore getHandScore(Hand hand, Flop flop, Card turn, Card river) {
		if (river != null) {
			return calculateHandScore(hand, flop.getCard1(), flop.getCard2(), flop.getCard3(), turn, river);
		} else {
			return getHandScore(hand, flop, turn);
		}
	}

	public static HandScore getHandScore(Hand hand) {
		if (hand.isPair()) {
			return new HandScore(HandType.ONE_PAIR, hand.getCard1().getRank());
		} else {
			ArrayList<Card> kicker = new ArrayList<>();
			kicker.add(hand.getMin());
			return new HandScore(HandType.HIGH_CARD, hand.getMax().getRank(), kicker);
		}
	}

	/**
	 * call this only if there more than 2 cards
	 *
	 * @param hand        hand (used to calculate kickers)
	 * @param sharedCards Community Cards (window cards)
	 * @return
	 */
	private static HandScore calculateHandScore(Hand hand, Card... sharedCards) {
		//cards are null, go through different process
		if (sharedCards == null || sharedCards.length == 0)
			return getHandScore(hand);
		// all cards
		Card[] cards = new Card[2 + sharedCards.length];
		System.arraycopy(hand.getCards(), 0, cards, 0, 2);
		System.arraycopy(sharedCards, 0, cards, 2, sharedCards.length);

		// Sort Card only once (Descending sort)
		Arrays.sort(cards, Collections.reverseOrder());

		// Check Flush
		Rank rank;
		Suit suit;
		suit = checkFlush(cards);
		rank = checkStraight(cards);
		HandScore handScore;
		if (suit != null && rank != null) {
			// rank.equals(Rank.ACE) ? HandType.ROYAL_FLUSH : HandType.STRAIGHT_FLUSH
			handScore = checkStraightFlush(cards, rank, suit);
			if (handScore != null) return handScore; //No kickers for this case
		}
		handScore = checkRecurrences(cards);

		if (handScore.getHandType() == HandType.FOUR_OF_A_KIND || handScore.getHandType() == HandType.FULL_HOUSE) {
			handScore.setKickers(searchKickers(cards, handScore));
			return handScore;
		} else if (suit != null)
			// get Flush Rank and Kickers
			return getFlushHandScore(suit, cards);
		else if (rank != null)
			return new HandScore(HandType.STRAIGHT, rank);
		else { // THREE_OF_A_KIND or TWO PAIR or ONE_PAIR or HIGH_CARD
			handScore.setKickers(searchKickers(cards, handScore));
			return handScore;
		}
	}

	public static HandScore getHandScore(Hand hand, Flop flop, Card turn) {
		if (turn != null) {
			return calculateHandScore(hand, flop.getCard1(), flop.getCard2(), flop.getCard3(), turn);
		} else {
			return HandScoreCalculator.getHandScore(hand, flop);
		}
	}

	/**
	 * @param cards
	 * @return {@link Suit} of the flush, null if no flush can be constructed from <code>cards</code>cards
	 */
	public static Suit checkFlush(Card... cards) {
		if (cards == null || cards.length < 5) return null;
		int hearts = 0, clubs = 0, diamonds = 0, spades = 0;
		for (Card card : cards) {
			switch (card.getSuit()) {
				case CLUBS:
					clubs++;
					break;
				case DIAMONDS:
					diamonds++;
					break;
				case HEARTS:
					hearts++;
					break;
				case SPADES:
					spades++;
					break;
			}
		}
		if (clubs > 4) return Suit.CLUBS;
		else if (hearts > 4) return Suit.HEARTS;
		else if (diamonds > 4) return Suit.DIAMONDS;
		else if (spades > 4) return Suit.SPADES;
		else return null;
	}

	/**
	 * Search if there is more than 5 consecutive cards (straight)
	 *
	 * @param cards Descending order Card Array
	 * @return the higher card rank of the straight, null otherwise
	 */
	private static Rank checkStraight(Card... cards) {
		// Card implements comparable. Rank Comparative
		for (int i = 0; i < cards.length; i++) {
			Rank currentRank = cards[i].getRank();
			int consecutiveCards = 1;
			int j;
			for (j = i + 1; j < cards.length; j++) {
				Rank nextCardRank = cards[j].getRank();
				if (currentRank.ordinal() - nextCardRank.ordinal() == 1) { // if rank and nextRank are consecutive
					consecutiveCards++;
					currentRank = nextCardRank;
				} else if (currentRank.ordinal() - nextCardRank.ordinal() != 0) { // if not same rank break, (straight chain is broken)
					break;
				}
			}
			if (consecutiveCards < 4) {
				// startNewHand the next loop from card j
				i = j - 1;
				continue;
			} else {
				boolean straightFrom5to1 = (consecutiveCards == 4
						&& cards[i].getRank() == Rank.FIVE && cards[0].getRank() == Rank.ACE);
				if (straightFrom5to1)
					consecutiveCards++;
				if (consecutiveCards > 4) return cards[i].getRank();
			}
		}
		return null;
	}

	/**
	 * Call this method if you have straight and flush but you are not sure if it's StraightFlush
	 * <p>Note: This function is like removing all cards that aren't from the same suit as <code>flushSuit</code>
	 * and check for straight
	 * </p>
	 *
	 * @param cards        ordered descending
	 * @param straightRank rank of the straight
	 * @param flushSuit    suit of the flush
	 * @return null if there's no StraightFlush or RoyalFlush, the Hand Score otherwise
	 */
	private static HandScore checkStraightFlush(Card[] cards, Rank straightRank, Suit flushSuit) {
		// it's the same code as straight but we ignore any card that hasn't the same Suit as flushSuit
		for (int i = 0; i < cards.length; i++) {
			Card card = cards[i];
			if (flushSuit.compareTo(card.getSuit()) != 0 || straightRank.compareTo(card.getRank()) < 0)
				continue;

			Rank currentRank = card.getRank();
			int consecutiveCards = 1;
			int j;
			for (j = i + 1; j < cards.length; j++) {
				Card nextCard = cards[j];
				Rank nextCardRank = nextCard.getRank();
				int diff = currentRank.ordinal() - nextCardRank.ordinal();
				if (diff == 1) { // if rank and nextRank are consecutive
					// don't considered any card with a different suit (see regressionTest on this test class)
					if (flushSuit.compareTo(nextCard.getSuit()) != 0)
						continue;
					consecutiveCards++;
					currentRank = nextCardRank;
				} else if (diff != 0) { // if not same rank break, (straight chain is broken)
					break;
				}
			}
			if (consecutiveCards < 4) {
				// startNewHand the next loop from card j
				i = j - 1;
				continue;
			} else {
				boolean straightFlushFrom5to1 = (consecutiveCards == 4 && cards[i].getRank() == Rank.FIVE);
				if (straightFlushFrom5to1) {
					// check if there's an Ace with the same suit at the top ordered list
					for (Card c : cards) {
						if (c.getRank() == Rank.ACE) {
							// we found an Ace with the same flush suit means we have a Flush_Straight to 5
							if (c.getSuit() == flushSuit)
								break;
						} else {
							// no more Ace expected, straightFlushFrom5to1 is impossible (note card is ordered)
							straightFlushFrom5to1 = false;
							break;
						}
					}
				}

				if (straightFlushFrom5to1)
					consecutiveCards++;
				if (consecutiveCards > 4) {
					Rank rankStraightFlush = cards[i].getRank();
					HandType handType = rankStraightFlush == Rank.ACE ? HandType.ROYAL_FLUSH : HandType.STRAIGHT_FLUSH;
					return new HandScore(handType, rankStraightFlush);
				}
			}
		}
		return null;
	}

	/**
	 * Note cards must be ordered
	 *
	 * @param cards Ascending/Descending order of a Sorted Card Array (higher Card first/lower Card first)
	 * @return
	 */
	private static HandScore checkRecurrences(Card... cards) {
		// Card Ranks, occurrence
		LinkedHashMap<Card, Integer> map = new LinkedHashMap<>();
		int maxOccurrence = 0; // the most (occurred Rank) value
		int maxCount = 0; // represent How many ranks has max value
		Rank max = null; // Higher Rank that have max occurrence.
		for (int i = 0; i < cards.length; i++) {
			int occurrence = 1; // occurrence of the rank of the cards[i]
			int j;
			for (j = i + 1; j < cards.length; j++) {
				if (cards[j].compareTo(cards[i]) == 0) occurrence++;
				else break; // Not Equal ranks
			}

			Card card = cards[i];
			if (maxOccurrence < occurrence) {
				maxOccurrence = occurrence;
				max = card.getRank();
				maxCount = 1;
			} else if (maxOccurrence == occurrence) {
				maxCount++;
			}
			map.put(cards[i], occurrence); //// Calculate how many occurrences
			i = j - 1; // next loop we start from i = j (that why -1)
		}

		// Rank frequency is defined now Specify the HandType based on maxOccurrence
		HandType handType;
		if (maxOccurrence == 4)
			handType = HandType.FOUR_OF_A_KIND;
		else if (maxOccurrence == 3)
			handType = map.containsValue(2) || maxCount >= 2 ? HandType.FULL_HOUSE : HandType.THREE_OF_A_KIND;
		else if (maxOccurrence == 2)
			handType = maxCount >= 2 ? HandType.TWO_PAIRS : HandType.ONE_PAIR; // using maxCount we will know if there is only one pair or two
		else handType = HandType.HIGH_CARD;

		return new HandScore(handType, max);
	}

	/**
	 * Search for kickers<br/>
	 * https://www.pagat.com/poker/rules/ranking.html
	 *
	 * @param cards     a list of 7 card ordered (descending order)
	 * @param handScore the score representing the hand
	 * @return kickers that a Hand has to break a possible tie
	 */
	private static List<Card> searchKickers(Card[] cards, HandScore handScore) {
		List<Card> kickers = new ArrayList<>();
		Card[] handCard;
		int kickersCount = 4;

		switch (handScore.getHandType()) {
			case FOUR_OF_A_KIND: // 1 kicker for this case
				kickersCount -= 1;
			case THREE_OF_A_KIND: // 2 kickers for this case
				kickersCount -= 1;
			case ONE_PAIR: // 3 kickers
				kickersCount -= 1;
			case HIGH_CARD: // 4 kickers
				for (Card card : cards)
					if (card.getRank() != handScore.getRank()) {
						kickers.add(card);
						if (kickers.size() == kickersCount) return kickers; // Return immediately
					}
				break;
			case FULL_HOUSE: // 2 kickers here
			case TWO_PAIRS: // 3 kickers here
				// take top card with a rank occurred two time or more
				// we exclude cards with the same HandScore's Rank
				// Note this code is taking a fact that a Pair with a different rank than HandScore must exist
				for (int i = 0; i < cards.length; i++) {
					Card c = cards[i];

					if (c.getRank() != handScore.getRank()) {
						Card nextC = cards[i + 1];
						if (c.equalRank(nextC)) {
							kickers.add(nextC);
							kickers.add(c);
							break;
						}
					}
				}
				if (handScore.getHandType() == HandType.FULL_HOUSE)
					return kickers;
				// TWO_PAIRS take top one pair also one high card
				// we exclude cards with the same HandScore's Rank
				// Note this code is taking a fact that a card with a different rank than HandScore and second Rank must exist
				for (int i = 0; i < cards.length; i++) {
					Card c = cards[i];

					if (c.getRank() != handScore.getRank() && !c.equalRank(kickers.get(0))) {
						// add kicker 3 after the second pair
						kickers.add(c);
						return kickers;
					}
				}
				break;
		}

		return kickers.isEmpty() ? null : kickers;
	}

	/**
	 * Get the Flush Rank and kickers
	 * It turns out there's kickers in Flush:
	 * Credit: http://poker.stackexchange.com/questions/1501/does-the-top-5-cards-rule-apply-to-a-flush/
	 *
	 * @param flushSuit suit repeated 5 times on card
	 * @param ordCards  7 cards reverse ordered
	 * @return a HandScore specific for Flush HandType
	 */
	private static HandScore getFlushHandScore(Suit flushSuit, Card... ordCards) {
		if (flushSuit == null) throw new IllegalArgumentException("FlushSuit can't be null");
		Rank flushRank = null;
		int cardsCount = 0;

		List<Card> kickers = new ArrayList<>(2);
		for (Card card : ordCards) {
			// save max flush (Represent the rank)
			if (card.getSuit() == flushSuit) {
				if (flushRank == null) flushRank = card.getRank();
				else kickers.add(card);
				cardsCount++;
				if (cardsCount == 5)
					return new HandScore(HandType.FLUSH, flushRank, kickers);
			}
		}
		return null;
	}

	public static HandScore getHandScore(Hand hand, Flop flop) {
		if (flop == null) {
			return getHandScore(hand);
		} else {
			return calculateHandScore(hand, flop.getCard1(), flop.getCard2(), flop.getCard3());
		}

	}

	public static Set<Card> get5BestCard(HandScore handScore, Hand hand, CommunityCards cc) {
		if (handScore == null || hand == null || cc == null || cc.getMissingCardCount() != 0)
			throw new IllegalArgumentException("all arguments must be nonnull and shared cards count should be " + CommunityCards.TEXAS_HOLDEM_COUNT);

		Set<Card> best = new HashSet<>(5);
		List<Card> cards = new ArrayList<>(7);
		cards.add(hand.getCard1());
		cards.add(hand.getCard2());
		cards.add(cc.getRiver());
		cards.add(cc.getTurn());
		cards.add(cc.getFlop().getCard1());
		cards.add(cc.getFlop().getCard2());
		cards.add(cc.getFlop().getCard3());
		Collections.sort(cards, Collections.reverseOrder());
		// get
		switch (handScore.getHandType()) {
			case HIGH_CARD:
				// take the 5 first card
				best.addAll(cards.subList(0, 5));
				break;
			case ONE_PAIR:
				// take one pair then same as high card
				set5BestPlusMostOccurredCard(best, cards, handScore.getRank(), 2);
				break;
			case TWO_PAIRS:
				// take two pair then add top card (same as HIgh card)
				// take one pair then same as high card
				int pairsAdded = 0;
				Iterator<Card> iterator = cards.iterator();
				do {
					Card c = iterator.next();
					// always add card as long two pairs were Added or there's enough space to add them
					if (pairsAdded > 1 || best.size() < 2 + 2 * pairsAdded)
						best.add(c);
					// FIXME we need more info about second par rank
					if (c.getRank() == handScore.getRank()) {
						pairsAdded++;
						best.add(iterator.next());
					}
				} while (best.size() != 5);
				break;
			case THREE_OF_A_KIND:
				// take THREE_OF_A_KIND then add top card (same as HIgh card)
				// take one pair then same as high card
				set5BestPlusMostOccurredCard(best, cards, handScore.getRank(), 3);
				break;

			case FULL_HOUSE:
				// take cards with the same rank + kickers
				break;

			case FOUR_OF_A_KIND:
				// take 4 card with a certain rank
				set5BestPlusMostOccurredCard(best, cards, handScore.getRank(), 4);
				break;
			case STRAIGHT_FLUSH:
			case ROYAL_FLUSH:
				// apply flush then apply straight
			case FLUSH:
				// take top 5 card with equal to handscore suit
				// TODO HandScore should store the suit of a flush
				int suits[] = new int[4];
				for (Card c : cards) {
					suits[c.getSuit().ordinal()]++;
				}
				Suit suit = null;
				for (int i = 0; i < suits.length; i++) {

					if (suits[i] > 4) {
						suit = Suit.values()[i];
						break;
					}
				}
				iterator = cards.iterator();
				boolean straightFlush = handScore.getHandType() != HandType.FLUSH;
				do {
					Card c = iterator.next();
					if (c.getSuit() == suit)
						best.add(c);
					else if (straightFlush) iterator.remove();

				} while (iterator.hasNext());
				// don't break it's ROYAL_FLUSH or STRAIGHT_FLUSH
				if (!straightFlush) break;
			case STRAIGHT:
				// copy 5 cards with unique Rank starting from HandScore rank
				Rank previousRank = null;
				best.clear();
				for (Card c : cards) {
					if (handScore.getRank().compareTo(c.getRank()) >= 0 && previousRank != c.getRank())
						best.add(c);
				}
				break;
		}


		return best;
	}

	/**
	 * @param best               a non null Set with a 5 card capacity
	 * @param reverseOrderedCard hand's card + shared card reverse ordered (Rank order)
	 * @param mostOccuRank       most occurred rank (i.e THREE_OF_A_KIND has a rank that's repeated 3 times)
	 * @param occurrence         times that rank occurred
	 */
	private static void set5BestPlusMostOccurredCard(Set<Card> best, List<Card> reverseOrderedCard, Rank mostOccuRank, int occurrence) {
		int n = 0;
		Iterator<Card> iterator = reverseOrderedCard.iterator();
		do {
			Card c = iterator.next();
			// always add card as long the pair card were Added or there's enough space to add them later
			if (n == occurrence || best.size() < 5 - occurrence)
				best.add(c);

			if (c.getRank() == mostOccuRank) {
				for (n = 0; n < occurrence; n++) {
					best.add(iterator.next());
				}
			}

		} while (best.size() < 5);

	}
}
