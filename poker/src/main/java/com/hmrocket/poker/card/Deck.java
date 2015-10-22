package com.hmrocket.poker.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hmrocket on 04/10/2015.
 */
public class Deck {

    private List<Card> cards;
    private int drawn = 0;

    public Deck() {
        cards = new ArrayList<Card>(52);
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(cards);
    }

    public void reset() {
        drawn = 0;
        Collections.shuffle(cards);
    }

	/**
	 * @return Top Card in the deck, {@link Card#NO_CARD} if the Deck is empty
	 */
	public Card drawCard() {
        if (drawn == 52) {
            return Card.NO_CARD;
        }
        return cards.get(drawn++);
    }

    public Flop dealFlop() {
        return new Flop(drawCard(), drawCard(), drawCard());
    }
}
