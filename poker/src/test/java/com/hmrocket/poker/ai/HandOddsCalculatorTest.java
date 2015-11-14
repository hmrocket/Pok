package com.hmrocket.poker.ai;

import com.hmrocket.poker.card.Card;
import com.hmrocket.poker.card.CommunityCards;
import com.hmrocket.poker.card.Flop;
import com.hmrocket.poker.card.Hand;
import com.hmrocket.poker.card.HandHoldem;
import com.hmrocket.poker.card.HandType;
import com.hmrocket.poker.card.Rank;
import com.hmrocket.poker.card.Suit;

import junit.framework.TestCase;

import java.util.Random;

/**
 * Created by hmrocket on 07/11/2015.
 */
public class HandOddsCalculatorTest extends TestCase {

	HandOddsCalculator handOddsCalculator;
	int n = 1000;

	public void setUp() throws Exception {
		super.setUp();
		handOddsCalculator = new HandOddsCalculator(n);
	}

	public void testGetHandOdds() throws Exception {
		HandOdds handOdds = handOddsCalculator.getHandOdds(2, new Hand(new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.ACE, Suit.HEARTS)));
		float strength = 0;
		for (HandType handType : HandType.values()) {
			strength += handOdds.getOdds(handType);
		}
		assertEquals("HS: " + handOdds.getHandStrength() + " isn't equal to " + strength,
				0, Float.compare(strength, handOdds.getHandStrength()));

	}

	public void testGetHandOdds1() throws Exception {
		Hand hand = new Hand(new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.TEN, Suit.DIAMONDS));
		Flop flop = new Flop(new Card(Rank.KING, Suit.DIAMONDS), new Card(Rank.QUEEN, Suit.DIAMONDS), new Card(Rank.JACK, Suit.DIAMONDS));
		CommunityCards communityCards = new CommunityCards(flop);
		Random r = new Random();
		HandOdds handOdds = handOddsCalculator.getHandOdds(r.nextInt(8) + 2, new HandHoldem(hand, communityCards));
		assertEquals(1f, handOdds.getHandStrength());
	}

	public void testGetHandOdds2() throws Exception {
		Hand hand = new Hand(new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.ACE, Suit.HEARTS));
		HandHoldem handHoldem = new HandHoldem(hand);
		Random r = new Random();
		HandOdds handOdds = handOddsCalculator.getHandOdds(r.nextInt(4) + 2, handHoldem);
		assertEquals(-1, Float.compare(0.2f, handOdds.getHandStrength()));
	}
}