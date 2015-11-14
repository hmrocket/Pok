package com.hmrocket.poker.card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

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
			handScore.setKickers(searchKickers(hand, handScore));
			return handScore;
		} else if (suit != null)
			// get Flush Rank and Kickers
			return getFlushHandScore(suit, hand, cards);
		else if (rank != null)
			return new HandScore(HandType.STRAIGHT, rank);
		else { // TWO PAIR or ONE_PAIR or HIGH_CARD
			handScore.setKickers(searchKickers(hand, handScore));
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
	private static Suit checkFlush(Card... cards) {
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
			if (flushSuit.compareTo(card.getSuit()) != 0)
				continue;

			Rank currentRank = card.getRank();
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
			handType = map.containsValue(2) ? HandType.FULL_HOUSE : HandType.THREE_OF_A_KIND;
		else if (maxOccurrence == 2)
			handType = maxCount >= 2 ? HandType.TWO_PAIRS : HandType.ONE_PAIR; // using maxCount we will know if there is only one pair or two
		else handType = HandType.HIGH_CARD;

		return new HandScore(handType, max);
	}

	/**
	 * Search for kickers in a Hand<br/>
	 * ONLY HAND KICKERS ARE RETURNED, WINDOW CARDS are discarded.
	 *
	 * @param hand
	 * @param handScore
	 * @return kickers that a Hand has to break a possible tie
	 */
	private static List<Card> searchKickers(Hand hand, HandScore handScore) {
		List<Card> kickers = new ArrayList<>();
		Card[] handCard;

		switch (handScore.getHandType()) {
			case FOUR_OF_A_KIND: // 1 kicker max for this case
				handCard = hand.getCards();
				Arrays.sort(handCard, Collections.reverseOrder());
				for (Card card : handCard)
					if (card.getRank().compareTo(handScore.getRank()) != 0) {
						kickers.add(card);
						return kickers; // Return immediately
					}
				break;
			case THREE_OF_A_KIND: // 2 kickers max for this case
			case TWO_PAIRS: // it can be 3 kickers here max for this case
			case ONE_PAIR: // Best hand is constructed with 3 kickers but only hand kickers are considered Shared cards are discarded (means 2 kickers max)
			case HIGH_CARD: // Best hand it can be constructed with 4 kickers but here 2 kickers max cause only Hand kickers are considered
				handCard = hand.getCards();
				Arrays.sort(handCard, Collections.reverseOrder()); // Descending order (higher Rank first)
				for (Card card : handCard)
					if (card.getRank().compareTo(handScore.getRank()) != 0) {
						kickers.add(card);
					}
				break;

		}

		return kickers.isEmpty() ? null : kickers;
	}

	/**
	 * Get the Flush Rank and kickers
	 * It turns out there's kickers in Flush:
	 * Credit: http://poker.stackexchange.com/questions/1501/does-the-top-5-cards-rule-apply-to-a-flush/
	 * @param flushSuit
	 * @param hand
	 * @param ordCards
	 * @return a HandScore specific for Flush HandType
	 */
	private static HandScore getFlushHandScore(Suit flushSuit, Hand hand, Card... ordCards) {
		if (flushSuit == null) throw new IllegalArgumentException("FlushSuit can't be null");
		Rank flushRank = null;
		int cardsCount = 0;

		List<Card> kickers = new ArrayList<>(2);
		for (Card card : ordCards) {
			// save max flush (Represent the rank)
			if (card.getSuit() == flushSuit) {
				if (flushRank == null) flushRank = card.getRank();
				else if (hand.contains(card)) kickers.add(card);
				cardsCount++;
				if (cardsCount == 5)
					return new HandScore(HandType.FLUSH, flushRank, kickers.isEmpty() ? null : kickers);
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

}
