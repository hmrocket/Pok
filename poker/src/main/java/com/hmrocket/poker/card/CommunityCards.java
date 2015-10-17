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

    public Flop getFlop() {
        return flop;
    }

    public void setFlop(Flop flop) {
        this.flop = flop;
        notifyObservers();
    }

    public Card getTurn() {
        return turn;
    }

    public void setTurn(Card turn) {
        this.turn = turn;
        notifyObservers();
    }

    public Card getRiver() {
        return river;
    }

    public void setRiver(Card river) {
        this.river = river;
        notifyObservers();
    }
}
