package com.hmrocket.poker.card;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by hmrocket on 23/10/2015.
 */
public class HandScoreCalculatorTest extends TestCase {

	/**
	 * Testing getHandScore(Hand)
	 *
	 * @throws Exception
	 */
	public void testGetHandScore() throws Exception {
		//test HandType.HIGH_CARD
		Hand hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.DIAMONDS));
		HandScore handScore = new HandScore(HandType.HIGH_CARD, Rank.JACK,
				Collections.singletonList(hand.getMin()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand));

		//test Pair
		hand.setCard1(new Card(Rank.TEN, Suit.CLUBS));
		handScore = new HandScore(HandType.ONE_PAIR, Rank.TEN);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand));
	}

	/**
	 * Testing getHandScore(Hand, Flop)
	 *
	 * @throws Exception
	 */
	public void testGetHandScore1() throws Exception {
		Hand hand;
		Flop flop;
		HandScore handScore;
		// HIGH_CARD
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.DIAMONDS));
		flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FIVE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		handScore = new HandScore(HandType.HIGH_CARD, Rank.ACE, Arrays.asList(hand.getCards()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop));

		// ONE_PAIR
		flop = new Flop(new Card(Rank.JACK, Suit.HEARTS), new Card(Rank.FIVE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		handScore = new HandScore(HandType.ONE_PAIR, Rank.JACK, Collections.singletonList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop));

		// TWO_PAIRS
		flop = new Flop(new Card(Rank.JACK, Suit.HEARTS), new Card(Rank.TEN, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		handScore = new HandScore(HandType.TWO_PAIRS, Rank.JACK, Collections.singletonList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop));

		// 3 kinds
		flop = new Flop(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.JACK, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		handScore = new HandScore(HandType.THREE_OF_A_KIND, Rank.JACK, Collections.singletonList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop));

		// Straight
		flop = new Flop(new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.NINE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		handScore = new HandScore(HandType.STRAIGHT, Rank.QUEEN);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop));

		// Straight Flush
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.CLUBS));
		handScore.setHandType(HandType.STRAIGHT_FLUSH);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop));

		// Flush
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.CLUBS));
		flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		handScore = new HandScore(HandType.FLUSH, Rank.JACK, Arrays.asList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop));

		// Royal flush
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.CLUBS));
		flop = new Flop(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.KING, Suit.CLUBS), new Card(Rank.QUEEN, Suit.CLUBS));
		handScore = new HandScore(HandType.ROYAL_FLUSH, Rank.ACE);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop));


	}

	/**
	 * Testing getHandScore(Hand, Flop, turn)
	 *
	 * @throws Exception
	 */
	public void testGetHandScore2() throws Exception {
		Hand hand;
		Flop flop;
		Card turn;
		HandScore handScore;
		// HIGH_CARD
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.DIAMONDS));
		flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FIVE, Suit.SPADES), new Card(Rank.THREE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.SPADES);
		handScore = new HandScore(HandType.HIGH_CARD, Rank.JACK, Collections.singletonList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn));

		// ONE_PAIR
		flop = new Flop(new Card(Rank.FIVE, Suit.HEARTS), new Card(Rank.FIVE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.SPADES);
		handScore = new HandScore(HandType.ONE_PAIR, Rank.FIVE, Arrays.asList(hand.getCards()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn));

		// TWO_PAIRS
		flop = new Flop(new Card(Rank.FOUR, Suit.HEARTS), new Card(Rank.ACE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.SPADES);
		handScore = new HandScore(HandType.TWO_PAIRS, Rank.ACE, Arrays.asList(hand.getCards()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn));

		// 3 kinds
		flop = new Flop(new Card(Rank.FOUR, Suit.CLUBS), new Card(Rank.FOUR, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.DIAMONDS);
		handScore = new HandScore(HandType.THREE_OF_A_KIND, Rank.FOUR, Arrays.asList(hand.getCards()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn));

		// Straight
		flop = new Flop(new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		turn = new Card(Rank.NINE, Suit.DIAMONDS);
		handScore = new HandScore(HandType.STRAIGHT, Rank.QUEEN);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn));

		// Straight Flush
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.CLUBS));
		flop = new Flop(new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		turn = new Card(Rank.NINE, Suit.CLUBS);
		handScore.setHandType(HandType.STRAIGHT_FLUSH);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn));

		// Flush
		hand = new Hand(new Card(Rank.JACK, Suit.DIAMONDS), new Card(Rank.TEN, Suit.CLUBS));
		flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		turn = new Card(Rank.NINE, Suit.CLUBS);
		handScore = new HandScore(HandType.FLUSH, Rank.TEN);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn));

		// Royal flush
		hand = new Hand(new Card(Rank.JACK, Suit.SPADES), new Card(Rank.TEN, Suit.CLUBS));
		flop = new Flop(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.KING, Suit.CLUBS), new Card(Rank.QUEEN, Suit.CLUBS));
		turn = new Card(Rank.JACK, Suit.CLUBS);
		handScore = new HandScore(HandType.ROYAL_FLUSH, Rank.ACE);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn));


	}

	/**
	 * Testing getHandScore(Hand, Flop, turn, river)
	 *
	 * @throws Exception
	 */
	public void testGetHandScore3() throws Exception {
		Hand hand;
		Flop flop;
		Card turn;
		Card river;
		HandScore handScore;
		// HIGH_CARD
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.DIAMONDS));
		flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FIVE, Suit.SPADES), new Card(Rank.THREE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.SPADES);
		river = new Card(Rank.SEVEN, Suit.SPADES);
		handScore = new HandScore(HandType.HIGH_CARD, Rank.JACK, Collections.singletonList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn, river));

		// ONE_PAIR
		flop = new Flop(new Card(Rank.FIVE, Suit.HEARTS), new Card(Rank.FIVE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.SPADES);
		river = new Card(Rank.SEVEN, Suit.SPADES);
		handScore = new HandScore(HandType.ONE_PAIR, Rank.FIVE, Arrays.asList(hand.getCards()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn, river));

		// TWO_PAIRS
		flop = new Flop(new Card(Rank.FOUR, Suit.HEARTS), new Card(Rank.ACE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.SPADES);
		river = new Card(Rank.JACK, Suit.DIAMONDS);
		handScore = new HandScore(HandType.TWO_PAIRS, Rank.ACE, Arrays.asList(hand.getCards()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn, river));

		// 3 kinds
		flop = new Flop(new Card(Rank.FOUR, Suit.CLUBS), new Card(Rank.FOUR, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.DIAMONDS);
		river = new Card(Rank.SEVEN, Suit.SPADES);
		handScore = new HandScore(HandType.THREE_OF_A_KIND, Rank.FOUR, Arrays.asList(hand.getCards()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn, river));

		// Straight
		flop = new Flop(new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		turn = new Card(Rank.NINE, Suit.DIAMONDS);
		river = new Card(Rank.SEVEN, Suit.SPADES);
		handScore = new HandScore(HandType.STRAIGHT, Rank.QUEEN);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn, river));

		// Straight Flush // Small Bug found: 5-A Straight ignored
		hand = new Hand(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.FIVE, Suit.CLUBS));
		flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FOUR, Suit.CLUBS), new Card(Rank.THREE, Suit.CLUBS));
		turn = new Card(Rank.ACE, Suit.DIAMONDS);
		river = new Card(Rank.ACE, Suit.SPADES);
		handScore = new HandScore(HandType.STRAIGHT_FLUSH, Rank.FIVE);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn, river));

		// Flush // thanks to this I found a bug (straight + flush doesn't mean == STRAIGHT_FLUSH)
		hand = new Hand(new Card(Rank.JACK, Suit.DIAMONDS), new Card(Rank.TEN, Suit.CLUBS));
		flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		turn = new Card(Rank.NINE, Suit.CLUBS);
		river = new Card(Rank.SEVEN, Suit.SPADES);
		handScore = new HandScore(HandType.FLUSH, Rank.TEN);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn, river));

		// Royal flush
		hand = new Hand(new Card(Rank.JACK, Suit.SPADES), new Card(Rank.TEN, Suit.CLUBS));
		flop = new Flop(new Card(Rank.JACK, Suit.DIAMONDS), new Card(Rank.KING, Suit.CLUBS), new Card(Rank.QUEEN, Suit.CLUBS));
		turn = new Card(Rank.JACK, Suit.CLUBS);
		river = new Card(Rank.ACE, Suit.CLUBS);
		handScore = new HandScore(HandType.ROYAL_FLUSH, Rank.ACE);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop, turn, river));


	}

	/**
	 * Testing getHandScore(Hand, CommunityCard)</br>
	 * Copy all hte test above just warp them in ComunnityCard container
	 *
	 * @throws Exception
	 */
	public void testGetHandScore4() throws Exception {
		Hand hand;
		CommunityCards communityCards = new CommunityCards();
		// XXX testGetHandScore() but with CommunityCard
		//test HandType.HIGH_CARD
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.DIAMONDS));
		HandScore handScore = new HandScore(HandType.HIGH_CARD, Rank.JACK,
				Arrays.asList(new Card[]{hand.getMin()}));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, (CommunityCards) null));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		//test Pair
		hand.setCard1(new Card(Rank.TEN, Suit.CLUBS));
		handScore = new HandScore(HandType.ONE_PAIR, Rank.TEN);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, (CommunityCards) null));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// XXX test same code testGetHandScore1 but using CommunityCard
		Flop flop;
		// HIGH_CARD
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.DIAMONDS));
		flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FIVE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		communityCards = new CommunityCards(flop);
		handScore = new HandScore(HandType.HIGH_CARD, Rank.ACE, Arrays.asList(hand.getCards()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// ONE_PAIR
		flop = new Flop(new Card(Rank.JACK, Suit.HEARTS), new Card(Rank.FIVE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		communityCards.setFlop(flop);
		handScore = new HandScore(HandType.ONE_PAIR, Rank.JACK, Collections.singletonList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// TWO_PAIRS
		flop = new Flop(new Card(Rank.JACK, Suit.HEARTS), new Card(Rank.TEN, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		communityCards.setFlop(flop);
		handScore = new HandScore(HandType.TWO_PAIRS, Rank.JACK, Collections.singletonList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// 3 kinds
		flop = new Flop(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.JACK, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		communityCards.setFlop(flop);
		handScore = new HandScore(HandType.THREE_OF_A_KIND, Rank.JACK, Collections.singletonList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// Straight
		flop = new Flop(new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.NINE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		communityCards.setFlop(flop);
		handScore = new HandScore(HandType.STRAIGHT, Rank.QUEEN);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// Straight Flush
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.CLUBS));
		handScore.setHandType(HandType.STRAIGHT_FLUSH);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// Flush
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.CLUBS));
		flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		communityCards.setFlop(flop);
		handScore = new HandScore(HandType.FLUSH, Rank.JACK, Arrays.asList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// Royal flush
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.CLUBS));
		flop = new Flop(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.KING, Suit.CLUBS), new Card(Rank.QUEEN, Suit.CLUBS));
		communityCards.setFlop(flop);
		handScore = new HandScore(HandType.ROYAL_FLUSH, Rank.ACE);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));


		//XXX test same code testGetHandScore2 but using CommunityCard
		Card turn;
		// HIGH_CARD
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.DIAMONDS));
		flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FIVE, Suit.SPADES), new Card(Rank.THREE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.SPADES);
		communityCards = new CommunityCards(flop, turn, null);
		handScore = new HandScore(HandType.HIGH_CARD, Rank.JACK, Collections.singletonList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// ONE_PAIR
		flop = new Flop(new Card(Rank.FIVE, Suit.HEARTS), new Card(Rank.FIVE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.SPADES);
		communityCards = new CommunityCards(flop, turn, null);
		handScore = new HandScore(HandType.ONE_PAIR, Rank.FIVE, Arrays.asList(hand.getCards()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// TWO_PAIRS
		flop = new Flop(new Card(Rank.FOUR, Suit.HEARTS), new Card(Rank.ACE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.SPADES);
		communityCards = new CommunityCards(flop, turn, null);
		handScore = new HandScore(HandType.TWO_PAIRS, Rank.ACE, Arrays.asList(hand.getCards()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// 3 kinds
		flop = new Flop(new Card(Rank.FOUR, Suit.CLUBS), new Card(Rank.FOUR, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.DIAMONDS);
		communityCards = new CommunityCards(flop, turn, null);
		handScore = new HandScore(HandType.THREE_OF_A_KIND, Rank.FOUR, Arrays.asList(hand.getCards()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// Straight
		flop = new Flop(new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		turn = new Card(Rank.NINE, Suit.DIAMONDS);
		communityCards = new CommunityCards(flop, turn, null);
		handScore = new HandScore(HandType.STRAIGHT, Rank.QUEEN);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// Straight Flush
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.CLUBS));
		flop = new Flop(new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		turn = new Card(Rank.NINE, Suit.CLUBS);
		communityCards = new CommunityCards(flop, turn, null);
		handScore.setHandType(HandType.STRAIGHT_FLUSH);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// Flush
		hand = new Hand(new Card(Rank.JACK, Suit.DIAMONDS), new Card(Rank.TEN, Suit.CLUBS));
		flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		turn = new Card(Rank.NINE, Suit.CLUBS);
		communityCards = new CommunityCards(flop, turn, null);
		handScore = new HandScore(HandType.FLUSH, Rank.TEN, Arrays.asList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// Royal flush
		hand = new Hand(new Card(Rank.JACK, Suit.SPADES), new Card(Rank.TEN, Suit.CLUBS));
		flop = new Flop(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.KING, Suit.CLUBS), new Card(Rank.QUEEN, Suit.CLUBS));
		turn = new Card(Rank.JACK, Suit.CLUBS);
		communityCards = new CommunityCards(flop, turn, null);
		handScore = new HandScore(HandType.ROYAL_FLUSH, Rank.ACE);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));


		// test testGetHandScore3
		Card river;

		// HIGH_CARD
		hand = new Hand(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.DIAMONDS));
		flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FIVE, Suit.SPADES), new Card(Rank.THREE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.SPADES);
		river = new Card(Rank.SEVEN, Suit.SPADES);
		communityCards = new CommunityCards(flop, turn, river);
		handScore = new HandScore(HandType.HIGH_CARD, Rank.JACK, Collections.singletonList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// ONE_PAIR
		flop = new Flop(new Card(Rank.FIVE, Suit.HEARTS), new Card(Rank.FIVE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.SPADES);
		river = new Card(Rank.SEVEN, Suit.SPADES);
		communityCards = new CommunityCards(flop, turn, river);
		handScore = new HandScore(HandType.ONE_PAIR, Rank.FIVE, Arrays.asList(hand.getCards()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// TWO_PAIRS
		flop = new Flop(new Card(Rank.FOUR, Suit.HEARTS), new Card(Rank.ACE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.SPADES);
		river = new Card(Rank.JACK, Suit.DIAMONDS);
		communityCards = new CommunityCards(flop, turn, river);
		handScore = new HandScore(HandType.TWO_PAIRS, Rank.ACE, Arrays.asList(hand.getCards()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// 3 kinds
		flop = new Flop(new Card(Rank.FOUR, Suit.CLUBS), new Card(Rank.FOUR, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		turn = new Card(Rank.FOUR, Suit.DIAMONDS);
		river = new Card(Rank.SEVEN, Suit.SPADES);
		communityCards = new CommunityCards(flop, turn, river);
		handScore = new HandScore(HandType.THREE_OF_A_KIND, Rank.FOUR, Arrays.asList(hand.getCards()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// Straight
		flop = new Flop(new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		turn = new Card(Rank.NINE, Suit.DIAMONDS);
		river = new Card(Rank.SEVEN, Suit.SPADES);
		communityCards = new CommunityCards(flop, turn, river);
		handScore = new HandScore(HandType.STRAIGHT, Rank.QUEEN);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// Straight Flush // Small Bug found: 5-A Straight ignored
		hand = new Hand(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.FIVE, Suit.CLUBS));
		flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FOUR, Suit.CLUBS), new Card(Rank.THREE, Suit.CLUBS));
		turn = new Card(Rank.ACE, Suit.DIAMONDS);
		river = new Card(Rank.ACE, Suit.SPADES);
		communityCards = new CommunityCards(flop, turn, river);
		handScore = new HandScore(HandType.STRAIGHT_FLUSH, Rank.FIVE);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// Flush // thanks to this I found a bug (straight + flush doesn't mean == STRAIGHT_FLUSH)
		hand = new Hand(new Card(Rank.JACK, Suit.DIAMONDS), new Card(Rank.TEN, Suit.CLUBS));
		flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		turn = new Card(Rank.NINE, Suit.CLUBS);
		river = new Card(Rank.SEVEN, Suit.SPADES);
		communityCards = new CommunityCards(flop, turn, river);
		handScore = new HandScore(HandType.FLUSH, Rank.TEN, Arrays.asList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

		// Royal flush
		hand = new Hand(new Card(Rank.JACK, Suit.SPADES), new Card(Rank.TEN, Suit.CLUBS));
		flop = new Flop(new Card(Rank.JACK, Suit.DIAMONDS), new Card(Rank.KING, Suit.CLUBS), new Card(Rank.QUEEN, Suit.CLUBS));
		turn = new Card(Rank.JACK, Suit.CLUBS);
		river = new Card(Rank.ACE, Suit.CLUBS);
		communityCards = new CommunityCards(flop, turn, river);
		handScore = new HandScore(HandType.ROYAL_FLUSH, Rank.ACE);
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));


	}


	public void testRandom() throws Exception {
		// using a lib to test my library
		// generate random hands and test
		Deck deck = new Deck();
		int handToTestInDeck = 52 / 5;
		int totalHandToTest = 1112220; // tested 1M hands in .3s
		CommunityCards communityCards = new CommunityCards();
		Card c1 = new Card(Rank.KING, Suit.HEARTS);
		Card c2 = new Card(Rank.ACE, Suit.HEARTS);
		Hand hand = new Hand(c1, c2);
		deck.burn(c1);
		deck.burn(c2);
		HandHoldem handHoldem = new HandHoldem(hand, communityCards);
		int[][] odds = new int[HandType.ROYAL_FLUSH.ordinal() + 1][Rank.ACE.ordinal() + 1];
		HandScore handScore;
		for (int i = 1; i < totalHandToTest; i++) {
			communityCards.setFlop(deck.dealFlop());
			communityCards.setTurn(deck.drawCard());
			communityCards.setRiver(deck.drawCard());
			handScore = handHoldem.getHandScore();
			if (handScore.getHandType() == HandType.STRAIGHT_FLUSH && handScore.getRank() == Rank.FOUR) {
				Card river = communityCards.getRiver();
				communityCards.setRiver(river);
				handHoldem.getHandScore();
			}
			odds[handScore.getHandType().ordinal()][handScore.getRank().ordinal()]++;
			// System.out.println(handHoldem + handScore.toString());
			//if (i % handToTestInDeck == 0) {
			deck.resetIgnoreBurns();
			//}
		}
		System.out.println(hand);
		int i = 0;
		for (int[] table : odds) {
			System.out.print(HandType.values()[i++].name() + ": ");
			for (int odd : table)
				System.out.print(odd + ", ");
			System.out.println();
		}

	}
}