package com.hmrocket.poker.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hmrocket on 04/10/2015.
 */
public class Deck {

    private List<Card> cards;
	private List<Card> cardsRemoved;
	private int drawn = 0;

    public Deck() {
        cards = new ArrayList<Card>(52);
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
		cardsRemoved = new ArrayList<>();
		Collections.shuffle(cards);
	}

    public void reset() {
        drawn = 0;
		Collections.shuffle(cards);
	}

	public void replaceBurnedCards() {
		if (!cardsRemoved.isEmpty()) {
			cards.addAll(cardsRemoved);
			cardsRemoved.clear();
		}
	}

	public void burn(Card card) {
		int index = cards.indexOf(card);
		if (index != -1) {
			cardsRemoved.add(card);
			cards.remove(index);
			if (index < drawn) drawn--; // if the card was draw already.

		}
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
