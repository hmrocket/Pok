package com.hmrocket.poker.card;

/**
 * Created by hmrocket on 04/10/2015.
 */
public class HandScore {
    private HandType handType;
    private Rank rank;


    public HandScore(HandType handType, Rank rank) {
        this.handType = handType;
        this.rank = rank;
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

    /**
     * Compare two HandScore
     * @param handScore
     * @return positive number if the first handScire is higher than the second, 0 if both score equals. Negative number otherwise
     */
    public int compareTo(HandScore handScore) {
        if (this.handType.equals(handScore.handType)) {
            return this.rank.compareTo(handScore.rank);
        } else {
            return handType.compareTo(handType);
        }
    }
}
