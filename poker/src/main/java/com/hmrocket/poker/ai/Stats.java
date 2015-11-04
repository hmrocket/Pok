package com.hmrocket.poker.ai;

import com.hmrocket.poker.Player;
import com.hmrocket.poker.Turn;

/**
 * Created by hmrocket on 02/11/2015.
 */
public class Stats {

	public static void update(Turn turn, Player playerPlayed) {
		if (playerPlayed.isOut()) {
			// TODO increment the playerFolded for turn
		} else if (playerPlayed.isPlaying()) {
			// TODO update increment the playerRaised for turn
			if (playerPlayed.didRaise(turn.getAmountToContinue())) {

			} else {
				// player called
			}
		} else {
			// player not out and not playing ==> AllIn
			// TODO increment AllInPlayers

		}

	}
}
