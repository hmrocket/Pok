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

	private List<Card> drawAllCards() {
		List<Card> cardList = new ArrayList<>();
		List<Card> cardDeckOrder = new ArrayList<>();
		for (Rank rank : Rank.values())
			for (Suit suit : Suit.values())
				cardList.add(new Card(rank, suit));

		//assert all cards exist
		Card card;
		while ((card = deck.drawCard()) != null && cardList.isEmpty() == false) {
			assertTrue(cardList.remove(card));
			cardDeckOrder.add(card);
		}
		//assert no more card available
		assertEquals(deck.drawCard(), null);

		return cardDeckOrder;
	}

	public void testDrawCard() throws Exception {
		drawAllCards();

	}

	public void testBurnCard() throws Exception {
		// burn a Card then check that you will never find it
		Card card = new Card(Rank.FIVE, Suit.DIAMONDS);
		deck.burn(card);
		Card c;
		int deckSize = 0;
		do {
			deckSize++;
			c = deck.drawCard();
			assertNotSame(card, c);
		} while (c != null);
		// check that the deck still 52 in total
		assertTrue(deckSize == 52);
		deck.replaceBurnedCards();
		assertEquals(card, deck.drawCard());
		assertNull(deck.drawCard());

		// 2- burn a already draw Card
		deck.reset();
		card = deck.drawCard();
		deck.burn(card); // no effect
		deckSize = 0;
		do {
			deckSize++;
			c = deck.drawCard();
			assertNotSame(card, c);
		} while (c != null);
		deck.replaceBurnedCards();
		assertEquals("even if the card was drawn should be back", card, deck.drawCard());
	}

	public void testRepalceBurnedCard() throws Exception {
		// is done with burn Card
	}

	public void testResetIgnoreBurns() throws Exception {
		// draw 5 cards then resetIgnoreBurns no card should be lost
		for (int i = 0; i < 5; i++) {
			deck.drawCard();
		}
		deck.resetIgnoreBurns();
		assertEquals(52, drawAllCards().size());
		// burn a Card that already draw out of the Deck
		// and check resetIgnroe has the same effect like reset
		deck.reset();
		Card burned = deck.drawCard();
		deck.burn(burned);
		for (int i = 0; i < 5; i++) {
			deck.drawCard();
		}
		deck.resetIgnoreBurns();
		List<Card> cardList = drawAllCards();
		assertEquals(51, cardList.size());
		/// burn called after the card is out has no effect
		assertFalse("when a card is burned that Card no longer exist in the deck",
				cardList.contains(burned));

		// 3- burn card then test isn't there after reset
		deck.reset();
		deck.burn(burned);
		for (int i = 0; i < 5; i++) {
			deck.drawCard();
		}
		deck.resetIgnoreBurns();
		cardList = drawAllCards();
		assertEquals(51, cardList.size());
		/// burn Card should be returned to the deck
		assertFalse(cardList.contains(burned));

	}

	public void testDealFlop() throws Exception {
		// No need, if drawCard work this should work too
	}
}