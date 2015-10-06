package com.hmrocket.poker.card;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

/**
 * poker arnaqueur - poker crook
 * Created by hmrocket on 04/10/2015.
 */
public class HandScoreCalculator {

    private HandScoreCalculator() {
    }

    public HandScore getHandScore(Hand hand) {
        if (hand.isPair()) {
            return new HandScore(HandType.ONE_PAIR, hand.getMax().getRank());
        } else {
            return new HandScore(HandType.HIGH_CARD, hand.getMax().getRank());
        }
    }

    public HandScore getHandScore(Hand hand, Flop flop) {
        return calculateHandScore(hand.getCard1(), hand.getCard2(), flop.getCard1(), flop.getCard2(), flop.getCard3());

    }

    public HandScore getHandScoreCalculator(Hand hand, Flop flop, Card turn) {
        return calculateHandScore(hand.getCard1(), hand.getCard2(), flop.getCard1(), flop.getCard2(), flop.getCard3(), turn);
    }

    public HandScore getHandScoreCalculator(Hand hand, Flop flop, Card turn, Card river) {
        return calculateHandScore(hand.getCard1(), hand.getCard2(), flop.getCard1(), flop.getCard2(), flop.getCard3(), turn, river);
    }

    public HandScore getHandScoreCalculator(Hand hand, Card... cards) throws OperationNotSupportedException {
        if (cards.length > 5)
            throw new IllegalArgumentException("no more than 5 cards should be passed");
        // TODO calculate hand type and rank
        throw new OperationNotSupportedException();
    }

    public HandScoreCalculator(HandHoldem handHoldem) throws OperationNotSupportedException {
        // TODO calculate hand type and rank
        throw new OperationNotSupportedException();
    }

    private HandScore calculateHandScore(Card... cards) {
        // Check Flush
        Rank rank;
        Suit suit;
        suit = checkFlush(cards);
        rank = checkStraight(cards);
        HandScore handScore = checkRecurrences(cards);
        if (suit != null && rank != null) {
            HandType handType = rank.equals(Rank.ACE) ? HandType.ROYAL_FLUSH : HandType.STRAIGHT_FLUSH;
            return new HandScore(handType, rank);
        } else if (handScore.getHandType() == HandType.FOUR_OF_A_KIND || handScore.getHandType() == HandType.FULL_HOUSE)
            return handScore;
        else if (suit != null || rank != null) return new HandScore(suit != null ? HandType.FLUSH : HandType.STRAIGHT, rank);
        else return handScore;
    }

    /**
     * @param cards
     * @return {@link Suit} of the flush, null if no flush can be constructed from <code>cards</code>cards
     */
    private Suit checkFlush(Card... cards) {
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
    private Rank getFlushRank(Suit flushSuit, Card... cards) {
        if (flushSuit == null) throw new IllegalArgumentException("FlushSuit can't be null");
        Rank maxRank = null;
        for (Card card : cards) {
            if (!flushSuit.equals(card.getSuit())) continue;
            if (card.getRank().compareTo(maxRank) > 0) {
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
    private Rank checkStraight(Card... cards) {
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
                // start the next loop from card j
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

    private HandScore checkRecurrences(Card... cards) {
        // sort cards based on cards ranks
        Arrays.sort(cards);
        HashMap<Rank, Integer> map = new HashMap<Rank, Integer>();
        for (int i = 0; i < cards.length; i++) {
            int j;
            for (j = i + 1; j < cards.length; j++) {
                if (cards[j].getRank().equals(cards[i].getRank())) {
                    continue;
                } else {
                    map.put(cards[i].getRank(), j - i);
                    i = j - 1;
                    break;
                }

            }
        }
        HandType handType;
        if (map.containsValue(4)) handType = HandType.FOUR_OF_A_KIND;
        else if (map.containsValue(3))
            handType = map.containsValue(2) ? HandType.FULL_HOUSE : HandType.THREE_OF_A_KIND;
        else if (map.containsValue(2)) handType = HandType.TWO_PAIRS;
        else handType = HandType.ONE_PAIR;

        Rank rank;
        switch (handType) {
            case FOUR_OF_A_KIND:
                rank = searchRankOfthisValue(map, 4);
                break;
            case FULL_HOUSE:
            case THREE_OF_A_KIND:
                rank = searchRankOfthisValue(map, 3);
                break;
            case TWO_PAIRS:
            case ONE_PAIR:
                // the first rank is the highest rank of the two pair
                rank = searchRankOfthisValue(map, 2);
                break;
            case HIGH_CARD:
            default:
                rank = searchRankOfthisValue(map, 1);
                break;
        }
        return new HandScore(handType, rank);
    }

    /**
     * Search for the first Rank that has value equal to integer
     * @param map
     * @param integer
     * @return the first Rank that has value equals to integer
     */
    private Rank searchRankOfthisValue(HashMap<Rank, Integer> map, Integer integer) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Rank, Integer> pair = (Map.Entry<Rank, Integer>) it.next();
            if (Integer.compare(pair.getValue(), integer) == 0)
                return pair.getKey();
        }
        return null;
    }
}
