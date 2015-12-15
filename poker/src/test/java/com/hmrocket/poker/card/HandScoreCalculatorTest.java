package com.hmrocket.poker.card;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

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
		handScore = new HandScore(HandType.FLUSH, Rank.TEN);
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
		handScore = new HandScore(HandType.FLUSH, Rank.TEN); // No kickers cause Ten is the rank of the flush (not a kicker)
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));
		communityCards.setRiver(new Card(Rank.ACE, Suit.CLUBS));
		handScore = new HandScore(HandType.FLUSH, Rank.ACE, Arrays.asList(hand.getCard2())); // this case Ten is a kicker it belong to the flush
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

	/**
	 * 1- test flush with no kicker cause the high rank of the flush is in the hand
	 * 2- test flush with no kicker cause no card in the hand belong to the best hand
	 * 3- nomal case suited hand
	 *
	 * @throws Exception
	 */
	public void testFlushScore() throws Exception {
		// Flush - 1
		Hand hand = new Hand(new Card(Rank.JACK, Suit.DIAMONDS), new Card(Rank.TEN, Suit.CLUBS));
		Flop flop = new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		Card turn = new Card(Rank.NINE, Suit.CLUBS);
		Card river = new Card(Rank.SEVEN, Suit.SPADES);
		CommunityCards communityCards = new CommunityCards(flop, turn, river);
		HandScore handScore = new HandScore(HandType.FLUSH, Rank.TEN); // No kickers cause Ten is the rank of the flush (not a kicker)
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));
		// Flush - 3
		communityCards.setRiver(new Card(Rank.ACE, Suit.CLUBS));
		handScore = new HandScore(HandType.FLUSH, Rank.ACE, Arrays.asList(hand.getCard2())); // this case Ten is a kicker it belong to the flush
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));
		// Flush - 2
		hand = new Hand(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.THREE, Suit.CLUBS));
		flop = new Flop(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.EIGHT, Suit.CLUBS));
		turn = new Card(Rank.NINE, Suit.CLUBS);
		river = new Card(Rank.SEVEN, Suit.CLUBS);
		communityCards = new CommunityCards(flop, turn, river);
		handScore = new HandScore(HandType.FLUSH, Rank.ACE); // No kickers cause (2)
		assertEquals(handScore, HandScoreCalculator.getHandScore(hand, communityCards));

	}

	public void testRandom() throws Exception {
		// using a lib to test my library
		// generate random hands and test
		Deck deck = new Deck();
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

	public void testRandom2() {
		Deck deck = new Deck();
		int totalHandToTest = 100000; // tested 100k hands
		Hand hand;
		CommunityCards cc = new CommunityCards();
		for (int i = 0; i < totalHandToTest; ++i) {
			if (i % 10 == 0) {
				deck.reset();
				cc.setFlop(deck.dealFlop());
				cc.setTurn(deck.drawCard());
				cc.setRiver(deck.drawCard());
			}
			hand = new Hand(deck.drawCard(), deck.drawCard());

			HandScore handScore = HandScoreCalculator.getHandScore(hand, cc);
			// verify that the HandScore make sense
			verifyHandScore(handScore, hand, cc);
		}
	}

	/**
	 * Check if the score of this handHoldem make sense for example if one pair has no other pair and no possible straight or flush
	 *
	 * @param handScore handScore for this hand
	 * @param hand      two pair of cards
	 * @param cc        community card or shared card among player
	 */
	private void verifyHandScore(HandScore handScore, Hand hand, CommunityCards cc) {
		List<Card> cards = new ArrayList<>();
		cards.add(hand.getCard1());
		cards.add(hand.getCard2());
		cards.add(cc.getRiver());
		cards.add(cc.getTurn());
		cards.add(cc.getFlop().getCard1());
		cards.add(cc.getFlop().getCard2());
		cards.add(cc.getFlop().getCard3());
		Collections.sort(cards, Collections.reverseOrder());
		Rank[] ranks = Rank.values();
		switch (handScore.getHandType()) {
			case HIGH_CARD:
				// check that the higher card mach rank higher
				assertTrue(handScore + ", " + hand + ", " + cc, Collections.max(cards).getRank() == handScore.getRank());
				// check other rank occurrence
				for (Rank r : ranks) {
					assertTrue(handScore + ", " + hand + ", " + cc, rankOccurrence(r, cards) <= 1);
				}
				// check no flush and no straight
				assertTrue(handScore + ", " + hand + ", " + cc, !isFlush(cards));
				assertTrue(handScore + ", " + hand + ", " + cc, !isStraight(cards));
				break;
			case ONE_PAIR:
				// check that the rank it's occurrence is just two times

				// check other rank occurrence
				for (Rank r : ranks) {
					if (r.equals(handScore.getRank()))
						assertTrue(handScore + ", " + hand + ", " + cc, rankOccurrence(handScore.getRank(), cards) == 2);
					else
						assertTrue(handScore + ", " + hand + ", " + cc, rankOccurrence(r, cards) <= 1);
				}
				// check no flush and no straight
				assertTrue(handScore + ", " + hand + ", " + cc, !isFlush(cards));
				assertTrue(handScore + ", " + hand + ", " + cc, !isStraight(cards));
				break;
			case TWO_PAIRS:
				// check that the rank it's occurrence is just two times
				assertTrue(handScore + ", " + hand + ", " + cc, rankOccurrence(handScore.getRank(), cards) == 2);
				// check other rank occurrence
				for (Rank r : ranks) {
					int rankOccurrence = rankOccurrence(r, cards);
					assertTrue(handScore + ", " + hand + ", " + cc, rankOccurrence <= 2);
					// if the occurrence is 2 check that the rank is lower or equal to handScore rank
					if (rankOccurrence == 2)
						assertTrue(handScore + ", " + hand + ", " + cc, handScore.getRank().compareTo(r) >= 0);
				}
				// check no flush and no straight
				assertTrue(handScore + ", " + hand + ", " + cc, !(isFlush(cards) || isStraight(cards)));
				break;
			case THREE_OF_A_KIND:
				// check other rank occurrence
				for (Rank r : ranks) {
					// check that the rank it's occurrence is just three times
					if (r.equals(handScore.getRank()))
						assertTrue(handScore + ", " + hand + ", " + cc, rankOccurrence(r, cards) == 3);
					else
						assertTrue(handScore + ", " + hand + ", " + cc, rankOccurrence(r, cards) <= 1);
				}
				// check no flush and no straight
				assertTrue(handScore + ", " + hand + ", " + cc, !(isFlush(cards) || isStraight(cards)));
				return;
			case FOUR_OF_A_KIND:
				// check other rank occurrence
				assertTrue(handScore + ", " + hand + ", " + cc, rankOccurrence(handScore.getRank(), cards) == 4);
				// check no straight
				assertTrue(handScore + ", " + hand + ", " + cc, !(isStraight(cards)));
				return;
			case FULL_HOUSE:
				// check other rank occurrence
				boolean occurrence2AtLeastOnce = false; // rank occurrred two times
				boolean occurrence3TwoTimes = false; // two ranks occurred three times
				for (Rank r : ranks) {
					// check that the rank it's occurrence is just three times
					if (r.equals(handScore.getRank()))
						assertTrue(handScore + ", " + hand + ", " + cc, rankOccurrence(r, cards) == 3);
					else {
						int rankOccurrence = rankOccurrence(r, cards);
						if (rankOccurrence == 2)
							occurrence2AtLeastOnce = true;
						else if (rankOccurrence == 3) {
							// the rank of the handScore must be bigger
							if (r != handScore.getRank()) {
								assertTrue(handScore + ", " + hand + ", " + cc, handScore.getRank().compareTo(r) > 0);
								occurrence3TwoTimes = true;
							}

						} else
							assertTrue(handScore + ", " + hand + ", " + cc, rankOccurrence <= 1);
					}
				}
				assertTrue(handScore + ", " + hand + ", " + cc, occurrence2AtLeastOnce || occurrence3TwoTimes);
				// check no straight
				assertTrue(handScore + ", " + hand + ", " + cc, !(isStraight(cards)));
				return;
			case STRAIGHT:
				// check no flush but a straight
				assertTrue(handScore + ", " + hand + ", " + cc, !isFlush(cards));
				assertTrue(handScore + ", " + hand + ", " + cc, isStraight(cards));
				// check no Full house and no four of kind
				boolean occurrence3AtLeastOnce = occurrence2AtLeastOnce = false;
				int occurrence3 = 0;
				for (Rank r : ranks) {
					int rankOccurrence = rankOccurrence(r, cards);
					if (rankOccurrence == 4) assertTrue(handScore + ", " + hand + ", " + cc, false);
					else if (rankOccurrence == 3) {
						occurrence3AtLeastOnce = true;
						occurrence3++;
					} else if (rankOccurrence == 2) occurrence2AtLeastOnce = true;
				}
				assertTrue(handScore + ", " + hand + ", " + cc, !((occurrence2AtLeastOnce || occurrence3 >= 2) && occurrence3AtLeastOnce));
				return;
			case FLUSH:
				// check no flush but no straight with same flush
				assertTrue(handScore + ", " + hand + ", " + cc, isFlush(cards));
				// check no Full house and no four of kind
				occurrence3AtLeastOnce = occurrence2AtLeastOnce = false;
				for (Rank r : ranks) {
					int rankOccurrence = rankOccurrence(r, cards);
					if (rankOccurrence == 4) assertTrue(handScore + ", " + hand + ", " + cc, false);
					else if (rankOccurrence == 3) occurrence3AtLeastOnce = true;
					else if (rankOccurrence == 2) occurrence2AtLeastOnce = true;
				}
				assertTrue(handScore + ", " + hand + ", " + cc, !(occurrence2AtLeastOnce && occurrence3AtLeastOnce));
				// assert no straight with same suit as the flush
				// remove non flush card (same suit card )and check for straight
				Suit suit = HandScoreCalculator.checkFlush(cards.toArray(new Card[7]));
				// be very careful when you iterate through a list (with an index and ) and removing item using index (index can changes)
				Iterator<Card> iterator = cards.iterator();
				while (iterator.hasNext()) {
					if (iterator.next().getSuit() != suit)
						iterator.remove();
				}
				assertTrue(handScore + ", " + hand + ", " + cc, !isStraight(cards));
				return;
			case ROYAL_FLUSH:
				assertTrue(handScore + ", " + hand + ", " + cc, handScore.getRank() == Rank.ACE);
			case STRAIGHT_FLUSH:
				// check flush and straight
				assertTrue(handScore + ", " + hand + ", " + cc, isFlush(cards));
				suit = HandScoreCalculator.checkFlush(cards.toArray(new Card[cards.size()]));
				// be very careful when you iterate through a list (with an index and ) and removing item using index (index can changes)
				iterator = cards.iterator();
				while (iterator.hasNext()) {
					if (iterator.next().getSuit() != suit)
						iterator.remove();
				}
				assertTrue(handScore + ", " + hand + ", " + cc, isStraight(cards));
				return;
			default:
				break;
		}
	}

	/**
	 * @param rank  rank to be searched
	 * @param cards cards will be searched
	 * @return number of occurrence of the Rank
	 */
	private int rankOccurrence(Rank rank, List<Card> cards) {
		int o = 0;
		for (Card c : cards) {
			if (c.getRank().compareTo(rank) == 0)
				o++;
		}
		return o;
	}

	/**
	 * check if there's any flush
	 *
	 * @param cards cards to check
	 * @return true if there's 5 or more card with same suit, false otherwise
	 */
	public boolean isFlush(List<Card> cards) {
		for (Suit s :
				Suit.values()) {
			if (suitOccurrence(s, cards) >= 5)
				return true;
		}
		return false;
	}

	/**
	 * check if there's a straight
	 *
	 * @param orderedCards reserve ordered list of card
	 * @return true if there's a straight, false otherwise
	 */
	public boolean isStraight(List<Card> orderedCards) {
		LinkedHashSet<Rank> setRank = new LinkedHashSet<>();
		for (Card c : orderedCards) {
			setRank.add(c.getRank());
		}

		// will check straight from the first/second/Third card and we are expecting the reset to be consecutive
		// don't forget there's 7 cards so if there's a straight it must start at the first or second or third card if not there might be no straight (last check is 5 straight might start from fourth)
		int straightStart = 1;

		// the straight (5 card consecutive) can be at the start, middle, end or (end-start for 5-A)
		Rank previousRank = null;
		for (Rank rank : setRank) {
			if (previousRank != null && previousRank.ordinal() - rank.ordinal() == 1) {
				straightStart++;
				//if (i <= setRank.size() - 1) straightMiddle++;
				//if (i <= setRank.size() - 2) straightEnd++;

				// four card are left
				//if (i <= 4) straight5A++;
			} else {
				straightStart = 1;
			}

			previousRank = rank;
			if (straightStart == 5)
				return true;
			if (straightStart == 4 && setRank.iterator().next().compareTo(Rank.ACE) == 0 && rank == Rank.TWO)
				return true;
		}
		return false;
	}

	/**
	 * calculate the the occurrence of specific Suit
	 *
	 * @param suit  suit to be searched
	 * @param cards cards will be searched
	 * @return number of occurrence of a suit
	 */
	private int suitOccurrence(Suit suit, List<Card> cards) {
		int o = 0;
		for (Card c : cards) {
			if (c.getSuit().compareTo(suit) == 0)
				o++;
		}
		return o;
	}

	public void testRegression() {
		// HandScore{handType=STRAIGHT_FLUSH, rank=9, kickers=}, 9♦:7♠, CommunityCards{flop=Flop{card1=4♦, card2=8♦, card3=5♦}, turn=6♦, river=Q♣}
		// what it should be: {handType=FLUSH, rank=9, kickers=
		Hand hand = new Hand(new Card(Rank.NINE, Suit.DIAMONDS), new Card(Rank.SEVEN, Suit.SPADES));
		CommunityCards cc = new CommunityCards();
		cc.setFlop(new Flop(new Card(Rank.FOUR, Suit.DIAMONDS), new Card(Rank.EIGHT, Suit.DIAMONDS), new Card(Rank.FIVE, Suit.DIAMONDS)));
		cc.setTurn(new Card(Rank.SIX, Suit.DIAMONDS));
		cc.setRiver(new Card(Rank.QUEEN, Suit.CLUBS));
		HandScore handScore = HandScoreCalculator.getHandScore(hand, cc);
		assertEquals(HandType.FLUSH, handScore.getHandType());
		assertEquals(Rank.NINE, handScore.getRank());
		assertNull(handScore.getKickers());

		//HandScore{handType=STRAIGHT_FLUSH, rank=6, kickers=}, 5♥:J♥, CommunityCards{flop=Flop{card1=6♥, card2=K♦, card3=2♥}, turn=4♥, river=3♠}
		//what it should be: {handType=FLUSH, rank=J, kickers=5♥
		hand = new Hand(new Card(Rank.FIVE, Suit.HEARTS), new Card(Rank.JACK, Suit.HEARTS));
		cc = new CommunityCards();
		cc.setFlop(new Flop(new Card(Rank.SIX, Suit.HEARTS), new Card(Rank.KING, Suit.DIAMONDS), new Card(Rank.TWO, Suit.HEARTS)));
		cc.setTurn(new Card(Rank.FOUR, Suit.HEARTS));
		cc.setRiver(new Card(Rank.THREE, Suit.SPADES));
		handScore = HandScoreCalculator.getHandScore(hand, cc);
		assertEquals(HandType.FLUSH, handScore.getHandType());
		assertEquals(Rank.JACK, handScore.getRank());
		assertNotNull(handScore.getKickers());
		ArrayList<Card> kickers = new ArrayList<>(1);
		kickers.add(new Card(Rank.FIVE, Suit.HEARTS));
		assertEquals(new HandScore(HandType.FLUSH, Rank.JACK, kickers), handScore);
		// HandScore{handType=STRAIGHT_FLUSH, rank=5, kickers=}, 4♣:3♣, CommunityCards{flop=Flop{card1=2♣, card2=A♥, card3=J♣}, turn=8♣, river=5♣}
		// What it should be: {handType=FLUSH, rank=J, kickers=4♣:3♣
		hand = new Hand(new Card(Rank.FOUR, Suit.CLUBS), new Card(Rank.THREE, Suit.CLUBS));
		cc = new CommunityCards();
		cc.setFlop(new Flop(new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.JACK, Suit.CLUBS)));
		cc.setTurn(new Card(Rank.EIGHT, Suit.CLUBS));
		cc.setRiver(new Card(Rank.FIVE, Suit.CLUBS));
		handScore = HandScoreCalculator.getHandScore(hand, cc);
		assertEquals(HandType.FLUSH, handScore.getHandType());
		assertEquals(Rank.JACK, handScore.getRank());
		assertNotNull(handScore.getKickers());
		kickers = new ArrayList<>(2);
		kickers.add(hand.getCard1());
		kickers.add(hand.getCard2());
		assertEquals(new HandScore(HandType.FLUSH, Rank.JACK, kickers), handScore);

		//HandScore{handType=FLUSH, rank=7, kickers=[5♣]}, 5♣:9♠, CommunityCards{flop=Flop{card1=7♣, card2=3♣, card3=4♣}, turn=6♣, river=6♥}
		// What it should be: handType=STRAIGHT_FLUSH, rank=7, kickers=[5♣]}
		hand = new Hand(new Card(Rank.FIVE, Suit.CLUBS), new Card(Rank.NINE, Suit.SPADES));
		cc = new CommunityCards();
		cc.setFlop(new Flop(new Card(Rank.SEVEN, Suit.CLUBS), new Card(Rank.THREE, Suit.CLUBS), new Card(Rank.FOUR, Suit.CLUBS)));
		cc.setTurn(new Card(Rank.SIX, Suit.CLUBS));
		cc.setRiver(new Card(Rank.SIX, Suit.HEARTS));
		handScore = HandScoreCalculator.getHandScore(hand, cc);
		assertEquals(HandType.STRAIGHT_FLUSH, handScore.getHandType());
		assertEquals(Rank.SEVEN, handScore.getRank());
		assertNull(handScore.getKickers());

		// HandScore{handType=FLUSH, rank=6, kickers=},  4♦:2♦, CommunityCards{flop=Flop{card1=5♥, card2=5♦, card3=2♥}, turn=6♦, river=3♦}
		// What it should be: handType=STRAIGHT_FLUSH, rank=6, kickers=
		hand = new Hand(new Card(Rank.FOUR, Suit.DIAMONDS), new Card(Rank.TWO, Suit.DIAMONDS));
		cc = new CommunityCards();
		cc.setFlop(new Flop(new Card(Rank.FIVE, Suit.HEARTS), new Card(Rank.FIVE, Suit.DIAMONDS), new Card(Rank.TWO, Suit.HEARTS)));
		cc.setTurn(new Card(Rank.SIX, Suit.DIAMONDS));
		cc.setRiver(new Card(Rank.THREE, Suit.DIAMONDS));
		handScore = HandScoreCalculator.getHandScore(hand, cc);
		assertEquals(HandType.STRAIGHT_FLUSH, handScore.getHandType());
		assertEquals(Rank.SIX, handScore.getRank());
		assertNull(handScore.getKickers());


		//HandScore{handType=ONE_PAIR, rank=5, kickers=[A♦, 3♥]}, 3♥:A♦, CommunityCards{flop=Flop{card1=5♠, card2=8♦, card3=6♠}, turn=5♦, river=4♠}
		List<Card> cards = new ArrayList<>();
		cards.add(new Card(Rank.THREE, Suit.HEARTS));
		cards.add(new Card(Rank.ACE, Suit.DIAMONDS));
		cards.add(new Card(Rank.FIVE, Suit.SPADES));
		cards.add(new Card(Rank.EIGHT, Suit.DIAMONDS));
		cards.add(new Card(Rank.SIX, Suit.SPADES));
		cards.add(new Card(Rank.FIVE, Suit.DIAMONDS));
		cards.add(new Card(Rank.FOUR, Suit.SPADES));
		Collections.sort(cards, Collections.reverseOrder());

		assertFalse(isFlush(cards));
		assertFalse(isStraight(cards));

		// HandScore{handType=FLUSH, rank=Q, kickers=[J♣]}, J♣:Q♣, CommunityCards{flop=Flop{card1=8♠, card2=6♣, card3=9♥}, turn=10♣, river=9♣}
		// Suit suit = HandScoreCalculator.checkFlush(cards.toArray(new Card[7]));
		cards = new ArrayList<>();
		cards.add(new Card(Rank.JACK, Suit.CLUBS));
		cards.add(new Card(Rank.QUEEN, Suit.CLUBS));
		cards.add(new Card(Rank.EIGHT, Suit.SPADES));
		cards.add(new Card(Rank.SIX, Suit.CLUBS));
		cards.add(new Card(Rank.NINE, Suit.HEARTS));
		cards.add(new Card(Rank.TEN, Suit.CLUBS));
		cards.add(new Card(Rank.NINE, Suit.CLUBS));
		Collections.sort(cards, Collections.reverseOrder());
		Suit suit = HandScoreCalculator.checkFlush(cards.toArray(new Card[cards.size()]));
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).getSuit() != suit)
				cards.remove(i);
		}
		assertTrue(handScore + ", " + hand + ", " + cc, !isStraight(cards));
	}
}