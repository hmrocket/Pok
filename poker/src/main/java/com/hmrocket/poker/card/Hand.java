package com.hmrocket.poker.card;

import java.util.List;

/**
 * Created by hmrocket on 04/10/2015.
 */
public class Hand {

    private Card card1;
    private Card card2;

    public Hand(Card card1, Card card2) {
        this.card1 = card1;
        this.card2 = card2;
    }

    public Hand(List<Card> cards) {
        this.card1 = cards.get(0);
        this.card2 = cards.get(1);
    }

    public Card getCard1() {
        return card1;
    }

    public void setCard1(Card card1) {
        this.card1 = card1;
    }

    public Card getCard2() {
        return card2;
    }

    public void setCard2(Card card2) {
        this.card2 = card2;
    }

    public Card[] getCards() {
        return new Card[]{card1, card2};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hand hand = (Hand) o;

        return this.compareTo(hand) == 0;
    }

    @Override
    public int hashCode() {
        int result = getMax().hashCode();
        result = 31 * result + getMin().hashCode();
        return result;
    }

    /**
     * Comapre two hands against each other without including other cards.
     *
     * @param o hand of two cards
     * @return 1 if the frist hand have a higher odds to win, -1 if the second.
     * 0 if both card has the same chance of winning
     */
    public int compareTo(Hand o) {
        if (isPair()) {
            if (o.isPair()) {
                return this.card1.compareTo(o.card1);
            } else {
                return 1;
            }
        } else {
            if (o.isPair()) {
                return -1;
            } else {
                int result = getMax().compareTo(o.getMax());
                if (result == 0) {
                    return getMin().compareTo(o.getMin());
                } else {
                    return result;
                }
            }
        }

    }

    /**
     * @return the high ranked card
     */
    public Card getMax() {
        if (card1.compareTo(card2) > 0) {
            return card1;
        } else {
            return card2;
        }
    }

    /**
     * @return the low ranked card
     */
    public Card getMin() {
        if (card1.compareTo(card2) > 0) {
            return card2;
        } else {
            return card1;
        }
    }

    public boolean isPair() {
        return card1.getRank() == card2.getRank();
    }
}
