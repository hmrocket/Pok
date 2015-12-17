package com.hmrocket.poker.card;

import java.util.Observable;

/**
 * Community card poker refers to any game of poker that uses community cards
 * (also called "shared cards" or "window cards"),
 * which are cards dealt face up in the center of the table and shared by all players
 * Created by hmrocket on 16/10/2015.
 */
public class CommunityCards extends Observable {

	public static final int TEXAS_HOLDEM_COUNT = 5;
	private int missingCardCount;
	private Flop flop;
	private Card turn;
    private Card river;

    public CommunityCards() {
		missingCardCount = TEXAS_HOLDEM_COUNT;
	}

    public CommunityCards(Flop flop) {
        this.flop = flop;
		missingCardCount = -1;
	}

    public CommunityCards(Flop flop, Card turn, Card river) {
        this.flop = flop;
        this.turn = turn;
        this.river = river;
		missingCardCount = -1;
	}

    public Flop getFlop() {
        return flop;
    }

    public void setFlop(Flop flop) {
        this.flop = flop;
        setChangedAndNotify();
    }

    private void setChangedAndNotify() {
		missingCardCount = -1;
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

	/**
	 * get the count of Cards missing
	 *
	 * @return the number of card missing from CommunityCard
	 */
	public int getMissingCardCount() {
		// lazy getter, if missingCardCount changed recalculate
		if (missingCardCount == -1) {
			missingCardCount = TEXAS_HOLDEM_COUNT;
			if (flop != null)
				missingCardCount -= 3;
			if (turn != null)
				missingCardCount -= 1;
			if (river != null)
				missingCardCount -= 1;
		}

		return missingCardCount;
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
