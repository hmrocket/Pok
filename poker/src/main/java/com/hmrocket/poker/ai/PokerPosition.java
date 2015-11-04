package com.hmrocket.poker.ai;

/**
 * Created by hmrocket on 02/11/2015.
 */
public enum PokerPosition {
	EARLY,
	MID_POSITION,
	LATE;

	/**
	 * Determine the PokerPosition of a player basing on his position in the game and players count
	 *
	 * @param playersCount
	 * @param position
	 * @return PokerPosition
	 */
	public static PokerPosition getPosition(int playersCount, int position) {
		if (playersCount == position)
			throw new IllegalArgumentException("positions starts from 0 to " + --playersCount);
		if (playersCount < 3) {
			return position == 0 ? EARLY : LATE;
		} else {
			int gap = playersCount / 3;
			int extra = playersCount % 3; // we will place extra people in the first
			if (position < gap + extra / 2)
				return EARLY;
			else if (position < gap * 2 + extra)
				return MID_POSITION;
			else return LATE;

		}
	}
}
