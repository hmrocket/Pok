package com.hmrocket.poker.card;

import junit.framework.TestCase;

import javax.naming.OperationNotSupportedException;

/**
 * Created by mhamed on 15-10-23.
 */
public class HandHoldemTest extends TestCase {

	private Deck deck;
	private Hand hand;
	private CommunityCards communityCards;

	static {

	}

	private HandHoldem handHoldem;

	public void setUp() throws Exception {
		super.setUp();
		deck = new Deck();
		hand = new Hand(deck.drawCard(), deck.drawCard());
		System.out.println(hand.toString());
		communityCards = new CommunityCards();
		handHoldem = new HandHoldem(hand, communityCards);
	}

	public void testGetFlop() throws Exception {
		// test CommunityCard Flop match HandHoldem's Flop
		// In different situations
		assertEquals(communityCards.getFlop(), handHoldem.getFlop());
		// test setting a new flop
		Flop flop = deck.dealFlop();
		communityCards.setFlop(flop);
		assertEquals(flop, handHoldem.getFlop());
		// test a null flop
		communityCards.setFlop(null);
		assertNull(handHoldem.getFlop());
	}

	public void testGetTurn() throws Exception {
		// test turn cards matchs
		// In different situations
		assertEquals(communityCards.getTurn(), handHoldem.getTurn());
		// test setting a new flop
		Card turn = deck.drawCard();
		communityCards.setTurn(turn);
		assertEquals(turn, handHoldem.getTurn());
		// test a null flop
		communityCards.setTurn(null);
		assertNull(handHoldem.getTurn());
	}

	public void testGetRiver() throws Exception {
		// test river cards matchs
		// In different situations
		assertEquals(communityCards.getRiver(), handHoldem.getRiver());
		// test setting a new flop
		Card river = deck.drawCard();
		communityCards.setRiver(river);
		assertEquals(river, handHoldem.getRiver());
		// test a null flop
		communityCards.setRiver(null);
		assertNull(handHoldem.getRiver());
	}

	public void testWinPercentage() throws Exception {
		try {
			handHoldem.winPercentage();
			assertTrue("winPercentage() didn't throw an OperationNotSupportedException", false);
		} catch (Exception e) {
			assertTrue(e instanceof OperationNotSupportedException);
		}
	}

	public void testGetHandScore() throws Exception {
		// test HandHoldem's HandScore for different situations

		// Start by the Pre-Flop Score
		assertEquals(HandScoreCalculator.getHandScore(hand), handHoldem.getHandScore());
		// HandScore test after the Flop
		Flop flop = deck.dealFlop();
		communityCards.setFlop(flop);
		assertEquals(flop, handHoldem.getFlop());
		// test a null flop
		communityCards.setFlop(null);
		assertNull(handHoldem.getFlop());

	}

	public void testCompareTo() throws Exception {

	}

	public void testUpdate() throws Exception {

	}
}