package com.hmrocket.poker.pot;

import com.hmrocket.poker.Player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Side pots develop from the fact that players can only win a maximum of the amount that they wagered from each of the players who call them. Thus any wagers above that amount
 * are placed in a separate pot which is competed for by everyone except for the all-in player.
 * Created by hmrocket on 18/10/2015.
 */
public class SidePot implements Comparable<SidePot> {

	private Set<Player> allInPlayers;
	private long value;
	private long allInValue;


	public SidePot(Player allInPlayer, MainPot mainPot) {
		allInPlayers = new HashSet<Player>();
		allInValue = allInPlayer.getBet();

		value = mainPot.value;
		mainPot.value = 0;

		addBet(allInPlayer);
	}


	/**
	 * read class description
	 *
	 * @param player must be all in player or it will throw an exception
	 * @return true if the player was added to this Side Pot
	 */
	public boolean addAllInPlayer(Player player) {
		if (player != null && player.getState() == Player.PlayerState.ALL_IN) {
			Iterator<Player> iterator = allInPlayers.iterator();
			if (iterator.hasNext() == false || iterator.next().getBet() == player.getBet()) {
				// this player belong to this sidePot
				// http://poker.stackexchange.com/questions/462/how-are-side-pots-built
				allInPlayers.add(player);
				return true;
			} else return false;

		} else
			throw new IllegalArgumentException("Player is either null or doesn't have all in state");
	}

	public Set<Player> getAllInPlayers() {
		return allInPlayers;
	}

	public boolean playerExist(Player player) {
		if (player == null || player.getState() != Player.PlayerState.ALL_IN)
			return false;
		else if (allInPlayers == null || allInPlayers.contains(player) == false)
			return false;
		else return true;
	}

	/**
	 * @return value of the SidePot
	 */
	public long getValue() {
		return value;
	}

	/**
	 * Add player bet to this SidePot (Player Bet will be reduiced after this operation),
	 * If the went all in with the same value he will be added to this pot
	 *
	 * @param player
	 */
	public void addBet(Player player) {
		if (player.getBet() > allInValue) {
			value += allInValue;
			player.setBet(player.getBet() - allInValue);
		} else {
			if (player.getBet() == allInValue && player.getState() == Player.PlayerState.ALL_IN) {
				addAllInPlayer(player);
			}
			value += player.getBet();
			player.setBet(0);
		}
	}

	@Override
	public int compareTo(SidePot o) {
		if (o == null || value > o.value)
			return 1;
		return value == o.value ? 0 : -1;
	}
}
