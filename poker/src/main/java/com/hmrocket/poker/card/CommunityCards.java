package com.hmrocket.poker.card;

import java.util.Observable;

/**
 * Community card poker refers to any game of poker that uses community cards
 * (also called "shared cards" or "window cards"),
 * which are cards dealt face up in the center of the table and shared by all players
 * Created by hmrocket on 16/10/2015.
 */
public class CommunityCards extends Observable {
    private Flop flop;
    private Card turn;
    private Card river;

    public CommunityCards() {
    }

    public CommunityCards(Flop flop) {
        this.flop = flop;
    }

    public CommunityCards(Flop flop, Card turn, Card river) {
        this.flop = flop;
        this.turn = turn;
        this.river = river;
    }

	/**
	 * Create a new object from a CommunityCards
	 * @param communityCards not null CommunityCards
	 */
    public CommunityCards(CommunityCards communityCards) {
        this.flop = communityCards.flop;
        this.turn = communityCards.turn;
		this.river = communityCards.river;
    }

    public Flop getFlop() {
        return flop;
    }

    public void setFlop(Flop flop) {
        this.flop = flop;
        setChangedAndNotify();
    }

    private void setChangedAndNotify() {
        setChanged();
        notifyObservers();
    }

    public Card getTurn() {
        return turn;
    }

    public void setTurn(Card turn) {
        this.turn = turn;
        setChangedAndNotify();
    }

    public Card getRiver() {
        return river;
    }

    public void setRiver(Card river) {
        this.river = river;
        setChangedAndNotify();
    }

    @Override
    public String toString() {
        return "CommunityCards{" +
                "flop=" + flop +
                ", turn=" + turn +
                ", river=" + river +
                '}';
    }
}
