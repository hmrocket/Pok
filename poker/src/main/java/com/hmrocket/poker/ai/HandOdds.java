package com.hmrocket.poker.ai;

import com.hmrocket.poker.card.HandType;
import com.hmrocket.poker.card.Rank;

/**
 * Created by hmrocket on 26/10/2015.
 */
public class HandOdds {
	private float[][] odds;

	public HandOdds() {
		odds = new float[HandType.STRAIGHT_FLUSH.ordinal() + 1][Rank.ACE.ordinal() + 1];
	}

	public void setOdds(HandType handType, Rank rank, float percentage) {
		if (handType == HandType.ROYAL_FLUSH) {
			handType = HandType.STRAIGHT_FLUSH;
			rank = Rank.ACE;
		}
		odds[handType.ordinal()][rank.ordinal()] = percentage;
	}

	public float getOdds(HandType handType, Rank rank) {
		if (handType == HandType.ROYAL_FLUSH) {
			handType = HandType.STRAIGHT_FLUSH;
			rank = Rank.ACE;
		}

		return odds[handType.ordinal()][rank.ordinal()];
	}


}
