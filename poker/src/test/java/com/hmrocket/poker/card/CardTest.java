package com.hmrocket.poker.card;

import junit.framework.TestCase;

import java.util.Random;

/**
 * Created by mhamed on 15-10-21.
 */
public class CardTest extends TestCase {

	Card card;
	Rank rank;
	Suit suit;

	public void setUp() throws Exception {
		super.setUp();
		Random r = new Random();
		rank = Rank.values()[r.nextInt(Rank.values().length)];
		suit = Suit.values()[r.nextInt(Suit.values().length)];
		card = new Card(rank, suit);
		System.out.println("Card: " + card.toString());
	}

	public void testGetSuit() throws Exception {
		assertEquals(suit, card.getSuit());
	}

	public void testSetSuit() throws Exception {
		card.setSuit(Suit.CLUBS);
		assertEquals(Suit.CLUBS, card.getSuit());
	}

	public void testGetRank() throws Exception {
		assertEquals(rank, card.getRank());
	}

	public void testSetRank() throws Exception {
		card.setRank(Rank.ACE);
		assertEquals(Rank.ACE, card.getRank());
	}

	public void testHashCode() throws Exception {
		int equalHashCodeCount = 0;
		for (Rank r : Rank.values()) {
			for (Suit s : Suit.values()) {
				Card c = new Card(r, s);
				if (card.hashCode() == c.hashCode())
					equalHashCodeCount++;
			}
		}

		assertTrue(equalHashCodeCount == 1);

	}

	public void testEquals() throws Exception {
		int equalCount = 0;
		for (Rank r : Rank.values()) {
			for (Suit s : Suit.values()) {
				Card c = new Card(r, s);
				if (card.equals(c))
					equalCount++;
			}
		}
		if (card.equals(null))
			equalCount++;
		if (card.equals(suit))
			equalCount++;

		assertTrue(equalCount == 1);

	}

	public void testEqualRank() throws Exception {

		int equalCount = 0;
		for (Rank r : Rank.values()) {
			for (Suit s : Suit.values()) {
				Card c = new Card(r, s);
				if (card.equalRank(c))
					equalCount++;
			}
		}

		assertTrue(equalCount == Suit.values().length);

	}

	public void testEqualSuit() throws Exception {
		for (Rank r : Rank.values()) {
			for (Suit s : Suit.values()) {
				Card c = new Card(r, s);
				if (card.equalSuit(c))
					assertEquals(card.getSuit(), s);
				else
					assertNotSame(card.getSuit(), s);
			}
		}

	}

	public void testCompareTo() throws Exception {
		for (Rank r : Rank.values()) {
			for (Suit s : Suit.values()) {
				Card c = new Card(r, s);
				assertTrue(card.compareTo(c) == card.getRank().compareTo(c.getRank()));
			}
		}
	}
}