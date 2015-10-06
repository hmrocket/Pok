package com.hmrocket.poker.card;

import javax.naming.OperationNotSupportedException;

/**
 * Created by hmrocket on 04/10/2015.
 */
public class HandScoreCalculator {

    private HandScoreCalculator() {
    }

    public HandScoreCalculator(Hand hand) throws OperationNotSupportedException {
        // TODO calculate hand type and rank
        throw new OperationNotSupportedException();
    }

    public HandScoreCalculator(Hand hand, Flop flop) throws OperationNotSupportedException {
        // TODO calculate hand type and rank
        throw new OperationNotSupportedException();
    }

    public HandScoreCalculator(Hand hand, Flop flop, Card turn, Card river) throws OperationNotSupportedException {
        // TODO calculate hand type and rank
        throw new OperationNotSupportedException();
    }

    public HandScoreCalculator(Hand hand, Card... cards) throws OperationNotSupportedException {
        if (cards.length > 5)
            throw new IllegalArgumentException("no more than 5 cards should be passed");
        // TODO calculate hand type and rank
        throw new OperationNotSupportedException();
    }

    public HandScoreCalculator(HandHoldem handHoldem) throws OperationNotSupportedException {
        // TODO calculate hand type and rank
        throw new OperationNotSupportedException();
    }

}
