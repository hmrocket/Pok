package com.hmrocket.poker.card;

public class Card implements Comparable<Card> {

    private Rank rank;
    private Suit suit;

    public static final Card NO_CARD = new Card(null, null) {
        @Override
        public Rank getRank() {
            throw new IllegalStateException("NO_CARD has no rank.");
        }

        @Override
        public Suit getSuit() {
            throw new IllegalStateException("NO_CARD has no suit.");
        }

        @Override
        public int compareTo(Card o) {
            throw new IllegalStateException("NO_CARD comparison.");
        }
    };

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    @Override
    public int hashCode() {
        int result = rank.hashCode();
        result = 31 * result + suit.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (rank != card.rank) return false;
        if (suit != card.suit) return false;

        return true;
    }

    @Override
    public int compareTo(Card card) {
        return this.getRank().compareTo(card.rank);
    }
}
