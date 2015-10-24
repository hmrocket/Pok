package com.hmrocket.poker.card;

import com.hmrocket.poker.PokerTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * poker arnaqueur - poker crook
 * Created by hmrocket on 04/10/2015.
 */
public final class HandScoreCalculator {

    private HandScoreCalculator() {
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


    public static HandScore getHandScore(Hand hand, Flop flop) {
		if (flop == null) {
			return getHandScore(hand);
		} else {
			return calculateHandScore(hand, flop.getCard1(), flop.getCard2(), flop.getCard3());
		}

	}

	public static HandScore getHandScore(Hand hand, Flop flop, Card turn) {
		if (turn != null) {
			return calculateHandScore(hand, flop.getCard1(), flop.getCard2(), flop.getCard3(), turn);
		} else {
			return HandScoreCalculator.getHandScore(hand, flop);
		}
	}

	public static HandScore getHandScore(Hand hand, Flop flop, Card turn, Card river) {
		if (river != null) {
			return calculateHandScore(hand, flop.getCard1(), flop.getCard2(), flop.getCard3(), turn, river);
		} else {
			return getHandScore(hand, flop, turn);
		}
	}

	public static HandScore getHandScore(Hand hand, CommunityCards communityCards) {
		if (communityCards != null) {
			return getHandScore(hand, communityCards.getFlop(), communityCards.getTurn(), communityCards.getRiver());
		} else {
			return getHandScore(hand);
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

        // Check Flush
        Rank rank;
        Suit suit;
        suit = checkFlush(cards);
        rank = checkStraight(cards);
        HandScore handScore = checkRecurrences(cards);
        if (suit != null && rank != null) {
            HandType handType = rank.equals(Rank.ACE) ? HandType.ROYAL_FLUSH : HandType.STRAIGHT_FLUSH;
			return new HandScore(handType, rank); //No kickers for this case
		} else if (handScore.getHandType() == HandType.FOUR_OF_A_KIND || handScore.getHandType() == HandType.FULL_HOUSE) {
			handScore.setKickers(searchKickers(hand, handScore));
			return handScore;
		} else if (suit != null || rank != null)
            return new HandScore(suit != null ? HandType.FLUSH : HandType.STRAIGHT, rank);
		else { // TWO PAIR or ONE_PAIR or HIGH_CARD
			handScore.setKickers(searchKickers(hand, handScore));
			return handScore;
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
     * Find max Rank of the Flush
     *
     * @param flushSuit
     * @param cards
     * @return
     */
    private static Rank getFlushRank(Suit flushSuit, Card... cards) {
        if (flushSuit == null) throw new IllegalArgumentException("FlushSuit can't be null");
        Rank maxRank = cards[0].getRank();
        for (int i = 1; i < cards.length; i++) {
            Card card = cards[i];
            if (!flushSuit.equals(card.getSuit())) continue;
            if (maxRank.compareTo(card.getRank()) < 0) {
                maxRank = card.getRank();
            }
        }
        return maxRank;
    }

    /**
     * Search if there is more than 5 consecutive cards (straight)
     *
     * @param cards
     * @return the higher card rank of the straight, null otherwise
     */
    private static Rank checkStraight(Card... cards) {
        // Card implements comparable. Rank Comparative
        Arrays.sort(cards);
        for (int i = cards.length - 1; i >= 0; i--) {
            Rank rank = cards[i].getRank();
            int consecutiveCards = 1;
            int j;
            for (j = i - 1; j >= 0; j--) {
                Rank nextCardRank = cards[j].getRank();
                if (rank.ordinal() - nextCardRank.ordinal() == 0) { // if same rank
                    continue;
                } else if (rank.ordinal() - nextCardRank.ordinal() == 1) { // if cards[i] and cards[j] are consecutive
                    consecutiveCards++;
                } else {
                    break;
                }
            }
            if (consecutiveCards < 4) {
                // startNewHand the next loop from card j
                i = j;
                continue;
            } else {
                boolean straightFrom5to1 = (consecutiveCards == 4
                        && rank == Rank.FIVE && cards[cards.length - 1].getRank() == Rank.ACE);
                if (straightFrom5to1)
                    consecutiveCards++;
                if (consecutiveCards > 4) return rank;
            }
        }
        return null;
    }

    private static HandScore checkRecurrences(Card... cards) {
        // sort cards based on cards ranks
        Arrays.sort(cards);
		// Card Ranks, occurrence
		LinkedHashMap<Card, Integer> map = new LinkedHashMap<>();
		for (int i = 0; i < cards.length; i++) {
			for (int j = i + 1; j < cards.length; j++) {
				if (cards[j].compareTo(cards[i]) == 0) { // Equal ranks
					continue; // Calculating how many occurrences
				} else {
					map.put(cards[i], j - i);
					i = j - 1; // next loop we start from j
					break;
				}
			}
        }

		HandType handType;
		if (map.containsValue(4)) handType = HandType.FOUR_OF_A_KIND;
		else if (map.containsValue(3))
            handType = map.containsValue(2) ? HandType.FULL_HOUSE : HandType.THREE_OF_A_KIND;
        else if (map.containsValue(2))
            handType = HandType.ONE_PAIR; // HandType can be TWO_PAIRS Also we don't know at this point
        else handType = HandType.HIGH_CARD;

        Rank rank;
		Card[] kickers;
		switch (handType) {
			case FOUR_OF_A_KIND:
				rank = searchCardOfthisValue(map, 4).getRank();
				break;
			case FULL_HOUSE:
            case THREE_OF_A_KIND:
				rank = searchCardOfthisValue(map, 3).getRank();
				break;
			case TWO_PAIRS:
            case ONE_PAIR:
                // the first rank is the highest rank of the two pair
				rank = searchCardOfthisValue(map, 2).getRank();
				if (PokerTools.DEBUG) {
					map.remove(rank);
					Rank rank2 = searchCardOfthisValue(map, 2).getRank();
					if (rank2 != null) {
						handType = HandType.TWO_PAIRS;
						if (rank.compareTo(rank2) < 0)
							throw new IllegalStateException("From my comment algorithm rank1 should be higher than rank2");
					}
				}
				break;
			case HIGH_CARD:
            default:
				rank = searchCardOfthisValue(map, 1).getRank();
				break;
        }
        return new HandScore(handType, rank);
    }

    /**
     * Search for the first Rank that has value equal to integer
     *
     * @param map
     * @param integer
	 * @return the first Card that has value equals to integer
	 */
	private static Card searchCardOfthisValue(HashMap<Card, Integer> map, Integer integer) {
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Card, Integer> pair = (Map.Entry<Card, Integer>) it.next();
			if (Integer.compare(pair.getValue(), integer) == 0)
				return pair.getKey();
		}
        return null;
    }

	/**
	 * Search for kickers in a Hand<br/>
	 * ONLY HAND KICKERS ARE RETURNED, WINDOW CARDS are discorded.
	 *
	 * @param hand      represent a map of card and there occurrence in the hand (LinkedHashMap keeps the order of the insertion which is mean it will be ordered too)
	 * @param handScore
	 * @return kickers that a Hand has to break a possible tie
	 */
	private static List<Card> searchKickers(Hand hand, HandScore handScore) {
		List<Card> kickers = new ArrayList<>();
		Card[] handCard;

		switch (handScore.getHandType()) {
			case FOUR_OF_A_KIND: // 1 kicker max for this case
				handCard = hand.getCards();
				Arrays.sort(handCard);
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
				Arrays.sort(handCard);
				for (Card card : handCard)
					if (card.getRank().compareTo(handScore.getRank()) != 0) {
						kickers.add(card);
					}
				break;

		}

		return kickers.isEmpty() ? null : kickers;
	}

}
