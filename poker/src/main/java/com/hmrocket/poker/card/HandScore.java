package com.hmrocket.poker.card;

import java.util.List;

/**
 * To determine the winner HandScore class hold 3 keyValue of the Rank of a Hand ({@link HandType HandType}, {@link Rank Rank} , {@link Card kickers})
 * Created by hmrocket on 04/10/2015.
 */
public class HandScore implements Comparable<HandScore> {

    private HandType handType;
    /**
     * The Rank of the HandType
     */
    private Rank rank;
    /**
     * The kickers are Cards included in the winnerHand, that does not itself take part in determining the rank of the hand,
     * but that may be used to break ties between hands of the same rank
     * Credit: https://en.wikipedia.org/wiki/Kicker_(poker)
     */
    private List<Card> kickers;

    /**
     * @param handType Hand Type
     * @param rank     best Rank of the HandType
     */
    public HandScore(HandType handType, Rank rank) {
        this.handType = handType;
        this.rank = rank;
        this.kickers = null;
    }

    /**
     * Kickers are the leftover cards after a hand is declared. They determine who wins if players have the same hand. Since not all hands have "leftovers", kickers only apply to four-of-a-kind, three-of-a-kind, two pair, one pair, and high card situations.
     *
     * @param handType
     * @param rank     rank of the HandType
     * @param kickers  the leftover cards if there is any
     * @see <a href="http://www.texasholdem-poker.com/kickers">kickers</a>
     */
    public HandScore(HandType handType, Rank rank, List<Card> kickers) {
        this.handType = handType;
        this.rank = rank;
        this.kickers = kickers;
    }

    public HandType getHandType() {
        return handType;
    }

    public Rank getRank() {
        return rank;
    }

    public void setHandType(HandType handType) {
        this.handType = handType;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public List<Card> getKickers() {
        return kickers;
    }

    public void setKickers(List<Card> kickers) {
        this.kickers = kickers;
    }

    /**
     * Compare two HandScore
     * @param handScore
     * @return positive number if the first handScire is higher than the second, 0 if both score equals. Negative number otherwise
     */
    @Override
    public int compareTo(HandScore handScore) {
        if (this.handType.equals(handScore.handType)) {
            int compareTo = this.rank.compareTo(handScore.rank);
            if (compareTo != 0)
                return compareTo;
            else
                return compareKickers(handScore);
        } else {
            return handType.compareTo(handScore.handType);
        }
    }

    private boolean isEmpty(List<Card> kicks) {
        return kicks == null || kicks.isEmpty();
    }

    private int compareKickers(HandScore handScore) {
        if (isEmpty(kickers) && isEmpty(handScore.kickers)) //both kickers empty
            return 0; // Same score
        else if (isEmpty(kickers) ^ isEmpty(handScore.kickers)) // one empty the other not
            return isEmpty(kickers) ? -1 : 1; //empty kickers low scores
        else // kickers cCrd must be in order
            for (int i = 0; i < kickers.size(); i++) {
                int compareToKicker = kickers.get(i).compareTo(handScore.kickers.get(i));
                if (compareToKicker == 0)
                    continue;
                else return compareToKicker;
            }
        // All kickers are equal
        return 0;
    }

    @Override
    public String toString() {
        return "HandScore{" +
                "handType=" + handType +
                ", rank=" + rank +
                ", kickers=" + (kickers == null ? 0 : kickers.size()) +
                '}';
    }
}
