package com.hmrocket.poker.card;

import java.lang.ref.WeakReference;
import java.util.Observable;
import java.util.Observer;

import javax.naming.OperationNotSupportedException;

/**
 * Created by hmrocket on 04/10/2015.
 */
public class HandHoldem implements Comparable<HandHoldem>, Observer {
	private Hand hand;
	// for the game I'm designing these maybe shouldn't be weak reference
	// A cheat can change how the value of the cards ?
	// XXX you can change to strong Reference
	private WeakReference<CommunityCards> communityCardsWeakReference;
	private HandScore handScore;

	public HandHoldem(Hand hand) {
		this.hand = hand;
	}

	public HandHoldem(Card card1, Card card2) {
		this.hand = new Hand(card1, card2);
	}

	public HandHoldem(Card card1, Card card2, CommunityCards communityCards) {
		this.hand = new Hand(card1, card2);
		if (communityCards != null) {
			communityCards.addObserver(this);
			// FIXME you might just getting a copy of the object and you and up referring to nothing
			this.communityCardsWeakReference = new WeakReference<CommunityCards>(communityCards);
		}
	}

	public HandHoldem(Hand hand, CommunityCards communityCards) {
		this.hand = hand;
		if (communityCards != null) {
			communityCards.addObserver(this);
			// FIXME you might just getting a copy of the object and you end up referring to nothing
			this.communityCardsWeakReference = new WeakReference<CommunityCards>(communityCards);
		}
	}

	public Flop getFlop() { // No setter cause this is a reference
		CommunityCards communityCards = communityCardsWeakReference.get();
		return communityCards == null ? null : this.communityCardsWeakReference.get().getFlop();
	}

	public Card getTurn() {  // No setter cause this is a reference
		CommunityCards communityCards = communityCardsWeakReference.get();
		return communityCards == null ? null : this.communityCardsWeakReference.get().getTurn();
	}

	public Card getRiver() { // No setter cause this is a reference
		CommunityCards communityCards = communityCardsWeakReference.get();
		return communityCards == null ? null : this.communityCardsWeakReference.get().getRiver();
	}

	public int winPercentage() throws OperationNotSupportedException {
		throw new OperationNotSupportedException();
	}

	public HandScore getHandScore() {
		if (handScore == null) // Lazy getter (Calculate the score when needed)
			if (communityCardsWeakReference == null)
				handScore = HandScoreCalculator.getHandScore(hand);
			else
				handScore = HandScoreCalculator.getHandScore(hand, communityCardsWeakReference.get());
		return handScore;
	}

	@Override
	public int compareTo(HandHoldem o) {
		// compareTo should throw a nullPointerException if o is null
//		if (o == null)
//			return 1;
//		else
			return getHandScore().compareTo(o.getHandScore());
	}

	@Override
	public void update(Observable o, Object arg) {
		// Will be called whenever ComunityCards change
		//Flop, Turn or River has changed, Score changes
		// XXX You might need also to update your week reference here
		handScore = null;
	}

	@Override
	public String toString() {
		String hand = this.hand.toString();
		String communityCardsWeakReference;
		if (this.communityCardsWeakReference != null)
			communityCardsWeakReference = this.communityCardsWeakReference.get().toString();
		else
			communityCardsWeakReference = "";
		String handScore = this.getHandScore().toString();
		return "{hand=" + hand +
				communityCardsWeakReference +
				handScore +
				'}';
	}
}
