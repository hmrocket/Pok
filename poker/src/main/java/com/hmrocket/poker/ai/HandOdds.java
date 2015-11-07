package com.hmrocket.poker.ai;

import com.hmrocket.poker.card.HandType;

import java.util.Arrays;

/**
 * Holder of winning percentages
 * Created by hmrocket on 26/10/2015.
 */
public class HandOdds {
	/**
	 * represent the ods of a specific HandType
	 */
	private float[] odds;
	/**
	 * Game simulation count
	 */
	private int n;

	/**
	 * @param n Game simulation count
	 */
	public HandOdds(int n) {
		odds = new float[HandType.ROYAL_FLUSH.ordinal() + 1];
		this.n = n;
	}

	/**
	 * @param handType   winning HandType
	 * @param winsCounts numbers of time you win the game with
	 */
	public void setOdds(HandType handType, int winsCounts) {
		odds[handType.ordinal()] = winsCounts;
	}


	/**
	 * occurrence of winning with specific HandType / simulation count (n)
	 * @param handType
	 * @return percentage of winning with a specific HandType
	 */
	public float getOdds(HandType handType) {
		return odds[handType.ordinal()] / n;
	}

	/**
	 * occurrence of winning with all HandType / simulation count (n)
	 * @return the percentage of winning a game
	 */
	public float getHandStrength() {
		float strength = 0;
		for (float oddsCount : odds) {
			strength += oddsCount;
		}
		return strength / n;
	}

	/**
	 * Reset the odds
	 */
	public void reset() {
		Arrays.fill(odds, 0);
	}

	/**
	 * Increment odds of winning with this HandTye
	 *
	 * @param handType winning hand type
	 */
	public void wins(HandType handType) {
		odds[handType.ordinal()] += 1;
	}
}
