package com.hmrocket.poker.card;

import junit.framework.TestCase;

import java.util.Arrays;

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
				Arrays.asList(new Card[]{hand.getMin()}));
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
		handScore = new HandScore(HandType.ONE_PAIR, Rank.JACK, Arrays.asList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop));

		// TWO_PAIRS
		flop = new Flop(new Card(Rank.JACK, Suit.HEARTS), new Card(Rank.TEN, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		handScore = new HandScore(HandType.TWO_PAIRS, Rank.JACK, Arrays.asList(hand.getCard2()));
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, flop));

		// 3 kinds
		flop = new Flop(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.JACK, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS));
		handScore = new HandScore(HandType.THREE_OF_A_KIND, Rank.JACK, Arrays.asList(hand.getCard2()));
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
		handScore = new HandScore(HandType.FLUSH, Rank.JACK);
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
		handScore = new HandScore(HandType.HIGH_CARD, Rank.JACK, Arrays.asList(new Card[]{hand.getCard2()}));
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
		river = new Card(Rank.FOUR, Suit.SPADES);
		handScore = new HandScore(HandType.HIGH_CARD, Rank.JACK, Arrays.asList(new Card[]{hand.getCard2()}));
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
	 * Testing getHandScore(Hand, CommunityCard)</br>
	 * Copy all hte test above just warp them in ComunnityCard container
	 *
	 * @throws Exception
	 */
	public void testGetHandScore4() throws Exception {
		Hand hand;
		Flop flop;
		Card turn;
		Card river;

	}
}