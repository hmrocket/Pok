package com.hmrocket.poker.card;

import junit.framework.TestCase;

/**
 * Created by mhamed on 15-10-22.
 */
public class HandTest extends TestCase {

	private final static Card card0;
	private final static Card card1;

	static {
		Deck deck = new Deck();
		card0 = deck.drawCard();
		card1 = deck.drawCard();
		System.out.println("Setup Hand: " + card0+card1);
	}

	private Hand hand;

	public void setUp() throws Exception {
		super.setUp();
		hand = new Hand(card0, card1);
	}

	public void testGetCard1() throws Exception {
		assertEquals(card0, hand.getCard1());
	}

	public void testSetCard1() throws Exception {
		Card card = new Card(Rank.ACE, Suit.CLUBS);
		hand.setCard1(card);
		assertEquals(card, hand.getCard1());
	}

	public void testGetCard2() throws Exception {
		assertEquals(card1, hand.getCard2());
	}

	public void testSetCard2() throws Exception {
		Card card = new Card(Rank.ACE, Suit.CLUBS);
		hand.setCard2(card);
		assertEquals(card, hand.getCard2());
	}


	public void testGetCards() throws Exception {
		Card[] cards = hand.getCards();
		assertEquals(card0, cards[0]);
		assertEquals(card1, cards[1]);
	}

	public void testEquals() throws Exception {

		for (int i = 0; i < (int) 1e6; i++) {
			Deck deck = new Deck();
			Hand hand1 = new Hand(deck.drawCard(), deck.drawCard());
			Hand hand2 = new Hand(deck.drawCard(), deck.drawCard());
			assertNotSame(hand1, hand2);
			Hand handB = new Hand(hand1.getCard2(), hand1.getCard1());
			assertEquals(hand1, handB);
		}
	}

	public void testHashCode() throws Exception {
		Deck deck = new Deck();
		Hand hand1 = new Hand(deck.drawCard(), deck.drawCard());
		Hand hand2 = new Hand(deck.drawCard(), deck.drawCard());
		assertNotSame(hand1.hashCode(), hand2.hashCode());
		Hand handB = new Hand(hand1.getCard2(), hand1.getCard1());
		assertEquals("hand1= " + hand1.toString() + " hand2=" + handB.toString(),
				hand1.hashCode(), handB.hashCode()); // XXX Failed: 1
	}

	public void testCompareTo() throws Exception {
		// go through all the possible hands and test compareTo result
		for(Rank rank : Rank.values())
			for (Suit suit: Suit.values())
				for(Rank rank2 : Rank.values())
					for (Suit suit2: Suit.values()) {
						Card card1 = new Card(rank, suit);
						Card card2 = new Card(rank2, suit2);
						Hand handCompareTo = new Hand(card1, card2);
						HandScore handScore = HandScoreCalculator.getHandScore(hand);
						HandScore handScore2 = HandScoreCalculator.getHandScore(handCompareTo);
						assertEquals(handScore.compareTo(handScore2), hand.compareTo(handCompareTo));
//						if(handScore)
//							assertTrue(hand.compareTo(handCompareTo) == 0);
//						else if (hand.getMax().compareTo(card1) < 0)
//							assertTrue(hand.compareTo(handCompareTo) < 0);
//						assertTrue(hand.compareTo(handCompareTo) > 0);

					}


		Deck deck = new Deck();
		Hand hand1 = new Hand(deck.drawCard(), deck.drawCard());
		Hand hand2 = new Hand(deck.drawCard(), deck.drawCard());
		Hand hand3 = new Hand(deck.drawCard(), deck.drawCard());

		//Assert equality
		assertTrue(hand1.compareTo(hand2) != 0);
		Hand handB = new Hand(hand1.getCard2(), hand1.getCard1());
		assertTrue(hand1.compareTo(handB) == 0);


	}

	public void testGetMax() throws Exception {
		int compareTo = hand.getMax().compareTo(hand.getMin());
		assertTrue(compareTo > 0 || compareTo == 0 && hand.getMax().compareTo(hand.getMin()) == 0);
	}

	public void testGetMin() throws Exception {
		//Same as testGetMax
	}

	public void testIsPair() throws Exception {
		assertEquals(hand.isPair(), card0.compareTo(card1) == 0);
	}
}