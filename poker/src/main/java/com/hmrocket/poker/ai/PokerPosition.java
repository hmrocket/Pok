package com.hmrocket.poker.ai;

/**
 * Created by hmrocket on 02/11/2015.
 */
public enum PokerPosition {
	EARLY,
	MID_POSITION,
	LATE,
	BLINDS;

	/**
	 * On pre-flop phase, Determine the PokerPosition of a player basing on his position in the game and players count</br>
	 *<code>if(playersCount == 2) return position == 0 ? MID_POSITION : BLINDS; </code>
	 * @param playersCount
	 * @param position
	 * @return PokerPosition (just the last 2 players On PRE_FLOP are BLINDS the rest fallow the same
	 * 				rule as {@link #getPosition(int, int)
	 */
	public static PokerPosition getPositionOnPreFlop(int playersCount, int position) {
		if (playersCount == position)
			throw new IllegalArgumentException("positions starts from 0 to " + --playersCount);
		if (playersCount < 3) {
			return position == 0 ? MID_POSITION : BLINDS;
		} else {
			if (position + 2 >= playersCount)
				// if the player is in the last two position he belongs to the Blinds position
				return BLINDS;
				// it's like removing the 2 last Blinds players
				// and applying the other rules other
			else return getPosition(playersCount - 2, position);
		}
	}

	/**
	 * Determine the PokerPosition of a player basing on his position in the game and players count
	 *
	 * @param playersCount
	 * @param position
	 * @return PokerPosition if <code>playersCount % 3 ==0</code> all position group should contains
	 * 						same number of Player, otherwise we will prioritize the
	 * 					MID_POSITION then EARLY
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