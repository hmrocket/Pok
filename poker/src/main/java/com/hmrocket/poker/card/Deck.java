package com.hmrocket.poker.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The deck of 52 French playing Cards
 * @author hmrocket
 * @since 04/10/2015
 * @see Card
 */
public class Deck {

    private List<Card> cards;
	private List<Card> cardsRemoved;
	private int drawn = 0;

	/**
	 * Create a deck with 52 Cards without JOKER(s)
	 * Note: you can create a JOKER card and override some of Card methods. (but for sure it will have a major change everywhere)
	 */
	public Deck() {
		cards = new ArrayList<>(52);
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				cards.add(new Card(rank, suit));
            }
        }
		cardsRemoved = new ArrayList<>();
		Collections.shuffle(cards);
	}

	/**
	 * Reset Deck to 52 Card
	 */
	public void reset() {
		reset(false);
	}

	private void reset(boolean ignoreBurnedCards) {
		drawn = 0;
		if (!ignoreBurnedCards) replaceBurnedCards();
		Collections.shuffle(cards);
	}

	/**
	 * Reset Deck without push back burned Card(s), Deck size will be 52 or less Card
	 */
	public void resetIgnoreBurns() {
		reset(true);
	}

	/**
	 * Add burned Card(s) to the bottom of Deck
	 */
	public void replaceBurnedCards() {
		if (!cardsRemoved.isEmpty()) {
			cards.addAll(cardsRemoved);
			cardsRemoved.clear();
		}
	}

	/**
	 * Burn a specific Card from the deck
	 * @param card Card to discard from the Deck
	 */
	public void burn(Card card) {
		int index = cards.indexOf(card);
		if (index != -1) {
			cardsRemoved.add(card);
			cards.remove(index);
			if (index < drawn) drawn--; // if the card was draw already.

		}
	}

	/**
	 * @return number of card that are left in the deck
	 */
	public int getLeftCardsCount() {
		return cards.size() - drawn;
	}

	/**
	 * Draw the Top Card
	 * @return Top Card of the Deck, <code>null</code> if the Deck is empty
	 */
	public Card drawCard() {
		if (drawn == cards.size()) {
			return null;
		}
		return cards.get(drawn++);
	}

	/**
	 * Deal three Card at once.
	 * Note: no checking if deck has less three cards.
	 * @return Flop
	 */
	public Flop dealFlop() {
		return new Flop(drawCard(), drawCard(), drawCard());
	}

	/**
	 * Deal a hand (two cards).
	 * Note: No checking, if deck has less two card.
	 *
	 * @return Hand constructed from two top cards of the Deck
	 */
	public Hand dealHand() {
		return new Hand(drawCard(), drawCard());
	}
}
