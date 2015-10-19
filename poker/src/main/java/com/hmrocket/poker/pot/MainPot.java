package com.hmrocket.poker.pot;

import com.hmrocket.poker.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Represent the potential winners money +
 * Created by hmrocket on 18/10/2015.
 */
public class MainPot {
	protected Set<Player> potentialWinners; // potential winners
	protected long value; // represent the total amount (potential winner money + losers money)

	public MainPot(Set<Player> playersInGame) {
		init(playersInGame);
	}

	protected void init(Set<Player> playersInGame) {
		value = 0;
		if (playersInGame != null)
			potentialWinners = playersInGame;
		else {
			if (potentialWinners != null) {
				potentialWinners.clear();
			} else {
				potentialWinners = new HashSet<Player>(); // Init an empty HashMap
			}
		}
	}

	public MainPot() {
		init(null);
	}

	public void reset() {
		value = 0;
		potentialWinners.clear();
	}

}
