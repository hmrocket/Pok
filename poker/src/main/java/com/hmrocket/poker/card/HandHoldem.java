package com.hmrocket.poker.card;

import javax.naming.OperationNotSupportedException;

/**
 * Created by hmrocket on 04/10/2015.
 */
public class HandHoldem implements Comparable<HandHoldem> {
	private Hand hand;
	// for the game I'm designing these aren't weak reference
	// A cheat can change how the value of the cards ?
	// XXX you can change to weakReference
	private Flop flop;
	private Card turn;
	private Card river;
	private HandScore handScore;

	public HandHoldem(Hand hand) {
		this.hand = hand;
	}

	public HandHoldem(Card card1, Card card2) {
		this.hand = new Hand(card1, card2);
	}

	public void setFlop(Flop flop) {
		this.flop = flop;
		handScore = null; // Card changed, Score changes
	}

	public void setTurn(Card turn) {
		this.turn = turn;
		handScore = null; // Card changed, Score changes
	}

	public void setRiver(Card river) {
		this.river = river;
		handScore = null; //River changed, Score changes
	}

	public int winPercentage() throws OperationNotSupportedException {
		throw new OperationNotSupportedException();
	}

	public HandScore getHandScore() {
		if (handScore == null) // Lazy getter (Calculate the score when needed)
			handScore = HandScoreCalculator.getHandScoreCalculator(hand, flop, turn, river);
		return handScore;
	}

	@Override
	public int compareTo(HandHoldem o) {
		if (o == null)
			return 1;
		else
			return handScore.compareTo(o.handScore);
	}
}
