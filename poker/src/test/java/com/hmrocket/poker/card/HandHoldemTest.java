package com.hmrocket.poker.card;

import junit.framework.TestCase;

import java.lang.reflect.Field;

import javax.naming.OperationNotSupportedException;

/**
 * Created by mhamed on 15-10-23.
 */
public class HandHoldemTest extends TestCase {

	static {

	}

	private Deck deck;
	private Hand hand;
	private CommunityCards communityCards;
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

	public void testGetCommunityCard() throws Exception {
		// assert the same object
		// Object equal (community equal wasn't overridden
		assertEquals(communityCards, handHoldem.getCommunityCards());

		// test null community card
		handHoldem.setCommunityCards(null);
		assertNull(handHoldem.getCommunityCards());

	}

	/***
	 * 1- test setCommunityCards for null object
	 * 2- test setCommunityCards point to correct new object
	 * 3- test that the old CommunityCard no longer update the observer handHoldem
	 *
	 * @throws Exception
	 */
	public void testSetCommunityCards() throws Exception {
		// test when CommunityCard is set to null everything is working
		handHoldem.setCommunityCards(null);
		assertEquals(HandScoreCalculator.getHandScore(hand), handHoldem.getHandScore());

		/// we will test if testSetCommunityCards work properly by
		// setting a new CommunityCard object
		communityCards = new CommunityCards();
		handHoldem.setCommunityCards(communityCards);

		// testGetHandScore work properly or not
		// by checking that HandHoldem handscore was updated for testGetHandScore (working correctly without errors)
		testGetHandScore();

		// final test: (3)
		communityCards = new CommunityCards(new Flop(new Card(Rank.ACE, Suit.SPADES), new Card(Rank.ACE, Suit.CLUBS),
				new Card(Rank.KING, Suit.SPADES)));
		handHoldem = new HandHoldem(new Hand(new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.ACE, Suit.HEARTS)), communityCards);
		// handScore is no longer null
		handHoldem.getHandScore();
		CommunityCards cc = new CommunityCards(new Flop(new Card(Rank.TEN, Suit.SPADES), new Card(Rank.TEN, Suit.CLUBS),
				new Card(Rank.KING, Suit.SPADES)));
		handHoldem.setCommunityCards(cc);
		// use reflexion to assure that the handscore is changed to null after set
		Field field = HandHoldem.class.getDeclaredField("handScore");
		field.setAccessible(true);
		assertNull(field.get(handHoldem));
		// make sure the handScore is two pair and not Four of Kind
		HandScore handScore = new HandScore(HandType.TWO_PAIRS, Rank.ACE);
		assertEquals(handScore, handHoldem.getHandScore());
		// change Old community card
		communityCards.setTurn(new Card(Rank.FOUR, Suit.DIAMONDS));
		assertNotNull(field.get(handHoldem));
	}

	public void testGetHandScore() throws Exception {
		// test HandHoldem's HandScore for different situations

		// Start by the Pre-Flop Score
		assertEquals(HandScoreCalculator.getHandScore(hand), handHoldem.getHandScore());
		// HandScore test after the Flop
		Flop flop = deck.dealFlop();
		communityCards.setFlop(flop); // Make sure that HandHoldem (Observant) is updated
		assertEquals(flop, handHoldem.getFlop());
		assertEquals(HandScoreCalculator.getHandScore(hand, flop), handHoldem.getHandScore());
		// test a null flop
		communityCards.setFlop(null); // Make sure that HandHoldem's (Observant) score is updated
		assertNull(handHoldem.getFlop());
		assertEquals(HandScoreCalculator.getHandScore(hand), handHoldem.getHandScore());

		// HandScore test after Turn
		Card turn = deck.drawCard();
		communityCards.setFlop(flop);
		communityCards.setTurn(turn);
		assertEquals(turn, handHoldem.getTurn()); // Make sure that HandHoldem (Observant) is updated
		assertEquals(HandScoreCalculator.getHandScore(hand, flop, turn), handHoldem.getHandScore());
		// test a null turn
		communityCards.setTurn(null);
		assertNull(handHoldem.getTurn());
		assertEquals(HandScoreCalculator.getHandScore(hand, flop), handHoldem.getHandScore());


		// HandScore test after River
		Card river = deck.drawCard();
		communityCards.setFlop(flop);
		communityCards.setTurn(turn);
		communityCards.setRiver(river);
		assertEquals(river, handHoldem.getRiver()); // Make sure that HandHoldem (Observant) is updated
		assertEquals(HandScoreCalculator.getHandScore(hand, flop, turn, river), handHoldem.getHandScore());
		// test a null river
		communityCards.setRiver(null);
		assertNull(handHoldem.getRiver());
		assertEquals(HandScoreCalculator.getHandScore(hand, flop, turn), handHoldem.getHandScore());

		// test when communityCard is null
		// TODO when you set communityCards to null handScore isn't updated right away so be careful
		// XXX but this shouldn't be a problem cause when you set communityCards to null we don't need
		// XXX communityCards or handScore anymore
		// communityCards = null;
		// assertEquals(HandScoreCalculator.getHandScore(hand), handHoldem.getHandScore());
		// assertNull(handHoldem.getFlop());
		// assertNull(handHoldem.getTurn());
		// assertNull(handHoldem.getFlop());


	}

	public void testWinPercentage() throws Exception {
		try {
			handHoldem.winPercentage();
			assertTrue("winPercentage() didn't throw an OperationNotSupportedException", false);
		} catch (Exception e) {
			assertTrue(e instanceof OperationNotSupportedException);
		}
	}

	public void testCompareTo() throws Exception {
		//XXX test when two handHoldem are equal
		assertTrue(handHoldem.compareTo(handHoldem) == 0);
		assertTrue(handHoldem.compareTo(new HandHoldem(hand, communityCards)) == 0);

		handHoldem = new HandHoldem(new Hand(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.ACE, Suit.DIAMONDS)), communityCards);
		HandHoldem handHoldem = new HandHoldem(new Hand(new Card(Rank.ACE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS)), communityCards);
		// Equal test & hashCode
		assertNotSame(handHoldem, this.handHoldem);
		assertNotSame(handHoldem.hashCode(), this.handHoldem.hashCode());
		assertTrue(this.handHoldem.compareTo(handHoldem) == 0);
		communityCards.setFlop(deck.dealFlop());
		assertTrue(this.handHoldem.compareTo(handHoldem) == 0);
		communityCards.setTurn(deck.drawCard());
		assertTrue(this.handHoldem.compareTo(handHoldem) == 0);
		communityCards.setTurn(deck.drawCard());
		// XXX Fail 1
		assertTrue(this.handHoldem.compareTo(handHoldem) == 0);


		// XXX test hand stronger than the other
		Flop flop = new Flop(new Card(Rank.KING, Suit.CLUBS), new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.JACK, Suit.CLUBS));
		Card turn = deck.drawCard();
		Card river = deck.drawCard();
		CommunityCards communityCards = new CommunityCards();

		HandHoldem strongHand = new HandHoldem(new Hand(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.TEN, Suit.CLUBS)), communityCards);
		HandHoldem weakerHand = new HandHoldem(new Hand(new Card(Rank.TWO, Suit.SPADES), new Card(Rank.KING, Suit.HEARTS)), communityCards);
		assertTrue(strongHand + " isn't > " + weakerHand, strongHand.compareTo(weakerHand) > 0);
		communityCards.setFlop(flop);
		assertTrue(strongHand + " isn't > " + weakerHand, strongHand.compareTo(weakerHand) > 0);
		communityCards.setTurn(turn);
		assertTrue(strongHand + " isn't > " + weakerHand, strongHand.compareTo(weakerHand) > 0);
		communityCards.setRiver(river);
		assertTrue(strongHand + " isn't > " + weakerHand, strongHand.compareTo(weakerHand) > 0);

	}

	public void testUpdate() throws Exception {
		// embarked to testGetHandScore
		//we checked that HandHoldem was updated by Checking GethandScore() is updated
	}
}