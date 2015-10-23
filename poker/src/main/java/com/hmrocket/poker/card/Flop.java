package com.hmrocket.poker.card;

/**
 * Created by hmrocket on 04/10/2015.
 */
public class Flop {
    private Card card1;
    private Card card2;
    private Card card3;

    public static final Flop EMPTY = new Flop(Card.NO_CARD, Card.NO_CARD, Card.NO_CARD);

    public Flop(Card card1, Card card2, Card card3) {
        this.card1 = card1;
        this.card2 = card2;
        this.card3 = card3;
    }

    public Card getCard1() {
        return card1;
    }

    public Card getCard2() {
        return card2;
    }

    public Card getCard3() {
        return card3;
    }

    public Card[] getCards() {
        return new Card[]{card1, card2, card3};
    }

    @Override
    public String toString() {
        return "Flop{" +
                "card1=" + card1 +
                ", card2=" + card2 +
                ", card3=" + card3 +
                '}';
    }
}
