package com.hmrocket.poker.pot;

import com.hmrocket.poker.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represent the potential winners money +
 * Created by hmrocket on 18/10/2015.
 */
public class MainPot {
	protected Set<Player> potentialWinners; // potential winners
	protected long value; // represent the total amount (potential winner money + losers money)

	public MainPot(List<Player> playersInGame) {
		init(playersInGame);
	}

	protected void init(List<Player> playersInGame) {
		potentialWinners = new HashSet<Player>(); // Init an empty HashMap
		value = 0;
		if (playersInGame != null) {
			potentialWinners.addAll(playersInGame);
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
