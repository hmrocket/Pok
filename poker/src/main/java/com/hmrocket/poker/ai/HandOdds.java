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

	public float getOdds(HandType handType) {
		if (handType == HandType.ROYAL_FLUSH) {
			handType = HandType.STRAIGHT_FLUSH;
		}
		float strength = 0;
		float[] table = odds[handType.ordinal()];
		int rankCount = table.length;
		for (int rank = 0; rank < rankCount; rank++) {
			float odd = table[rank];
			strength += odd * (handType.ordinal() * rankCount + rank + 1);
		}
		return strength;
	}

	public float getHandStrength() {
		float strength = 0;
		for (int handType = 0; handType < odds.length; handType++) {
			float[] table = odds[handType];
			int length = table.length;
			for (int rank = 0; rank < length; rank++) {
				float odd = table[rank];
				strength += odd * (handType * length + rank + 1);
			}
		}
		return strength;
	}
}
