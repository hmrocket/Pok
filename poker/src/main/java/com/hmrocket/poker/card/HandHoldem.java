package com.hmrocket.poker.card;

import com.hmrocket.poker.ai.HandOdds;
import com.hmrocket.poker.ai.HandOddsCalculator;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

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
	/**
	 * This represent a list of most important card that constitute this HandScore
	 * This is an optional parameter that is not needed in the process to determine the HandScore
	 */
	private Set<Card> best5Cards;

	public HandHoldem(Hand hand) {
		this.hand = hand;
		communityCardsWeakReference = new WeakReference<CommunityCards>(null);
	}

	public HandHoldem(Card card1, Card card2) {
		this.hand = new Hand(card1, card2);
		communityCardsWeakReference = new WeakReference<CommunityCards>(null);
	}

	public HandHoldem(Card card1, Card card2, CommunityCards communityCards) {
		this.hand = new Hand(card1, card2);
		setCommunityCards(communityCards);
	}

	public HandHoldem(Hand hand, CommunityCards communityCards) {
		this.hand = hand;
		setCommunityCards(communityCards);
	}

	public CommunityCards getCommunityCards() {
		return communityCardsWeakReference.get();
	}

	/**
	 * Set a softReference to a new CommunityCards object,
	 * And delete this from Observers list of the old object
	 * Reset the HandScore
	 *
	 * @param communityCards
	 */
	public void setCommunityCards(CommunityCards communityCards) {
		// we Don't want to update this object if the old object changed
		if (communityCardsWeakReference != null && getCommunityCards() != null)
			getCommunityCards().deleteObserver(this);

		if (communityCards != null) {
			communityCards.addObserver(this);
			this.communityCardsWeakReference = new WeakReference<CommunityCards>(communityCards);
		} else communityCardsWeakReference = new WeakReference<CommunityCards>(null);
		// Community card changed (update) reset the HandScore
		handScore = null;
	}

	public Hand getHand() {
		return hand;
	}

	public Flop getFlop() { // No setter cause this is a reference
		CommunityCards communityCards = communityCardsWeakReference.get();
		return communityCards == null ? null : communityCards.getFlop();
	}

	public Card getTurn() {  // No setter cause this is a reference
		CommunityCards communityCards = communityCardsWeakReference.get();
		return communityCards == null ? null : communityCards.getTurn();
	}

	public Card getRiver() { // No setter cause this is a reference
		CommunityCards communityCards = communityCardsWeakReference.get();
		return communityCards == null ? null : communityCards.getRiver();
	}

	/**
	 * get the percentage that this hand will win against number player
	 *
	 * @param playerCount total players on this game
	 * @return the percentage of winning the game with this hand
	 */
	public int winPercentage(int playerCount) {
		HandOddsCalculator handOddsCalculator = new HandOddsCalculator(100);
		HandOdds handOdds = handOddsCalculator.getHandOdds(playerCount, this);
		return (int) (handOdds.getHandStrength() * 100);
	}

	@Override
	public int compareTo(HandHoldem o) {
		// compareTo should throw a nullPointerException if o is null
//		if (o == null)
//			return 1;
//		else
		return getHandScore().compareTo(o.getHandScore());
	}

	public HandScore getHandScore() {
		if (handScore == null) // Lazy getter (Calculate the score when needed)
			handScore = HandScoreCalculator.getHandScore(hand, communityCardsWeakReference.get());
		return handScore;
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
		String bestCards = null;
		if (getCommunityCards() != null) {
			communityCardsWeakReference = getCommunityCards().toString();
			if (getCommunityCards().getMissingCardCount() == 0)
				bestCards = "Best5: " + Arrays.deepToString(getBest5Cards().toArray());
		} else
			communityCardsWeakReference = "";
		String handScore = this.getHandScore().toString();
		return "{hand=" + hand +
				communityCardsWeakReference +
				handScore +
				bestCards +
				'}';
	}

	public Set<Card> getBest5Cards() {
		if (best5Cards == null) {
			best5Cards = HandScoreCalculator.get5BestCard(getHandScore(), hand, getCommunityCards());
		}
		return best5Cards;
	}
}
