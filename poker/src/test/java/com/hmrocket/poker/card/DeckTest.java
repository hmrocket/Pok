package com.hmrocket.poker.card;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mhamed on 15-10-22.
 */
public class DeckTest extends TestCase {

	Deck deck;

	public void setUp() throws Exception {
		super.setUp();
		deck = new Deck();
	}

	public void testReset() throws Exception {
		List<Card> cardList = drawAllCards();
		deck.reset();
		// assert that the deck was reset and all card exist
		List<Card> cardList1 = drawAllCards();

		// assert the Deck was shuffled
		assertFalse(Arrays.deepEquals(cardList.toArray(), cardList1.toArray()));


	}

	public void testDrawCard() throws Exception {
		drawAllCards();

	}

	private List<Card> drawAllCards() {
		List<Card> cardList = new ArrayList<>(52);
		List<Card> cardDeckOrder = new ArrayList<>(52);
		for (Rank rank : Rank.values())
			for (Suit suit : Suit.values())
				cardList.add(new Card(rank, suit));

		//assert all cards exist
		while (cardList.isEmpty() == false) {
			Card card = deck.drawCard();
			assertTrue(cardList.remove(card));
			cardDeckOrder.add(card);
		}
		//assert no more card available
		assertEquals(deck.drawCard(), Card.NO_CARD);

		return cardDeckOrder;
	}
}