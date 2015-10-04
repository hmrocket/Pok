package com.hmrocket.poker.card;

import javax.naming.OperationNotSupportedException;

/**
 * Created by hmrocket on 04/10/2015.
 */
public class HandHoldem {
    private Hand hand;
    private Flop flop;
    private Card turn;
    private Card river;

    public HandHoldem(Hand hand) {
        this.hand = hand;
    }

    public HandHoldem(Card card1, Card card2) {
        this.hand = new Hand(card1, card2);
    }

    public void setFlop(Flop flop) {
        this.flop = flop;
    }

    public void setTurn(Card turn) {
        this.turn = turn;
    }

    public void setRiver(Card river) {
        this.river = river;
    }

    public int winPercentage() throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    public HandScoreFactory getreturn() throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }


}
