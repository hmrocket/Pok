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
}
