package com.hmrocket.poker.card;

/**
 * Created by HmRocket on 04/10/2015.
 */
public enum Suit {
    SPADES,
    HEARTS,
    DIAMONDS,
    CLUBS;

	@Override
	public String toString() {
		switch (this) {
			case CLUBS:
				return "♣";
			case DIAMONDS:
				return "♦";
			case HEARTS:
				return "♥";
			case SPADES:
				return "♠";
			default:
				return null;
		}
	}
}
