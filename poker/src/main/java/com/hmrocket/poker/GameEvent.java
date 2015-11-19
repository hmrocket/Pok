package com.hmrocket.poker;

import java.util.Set;

/**
 * Created by hmrocket on 19/11/2015.
 */
public interface GameEvent { // Table is expecting a callback when game ends and when a player doesn't have enough money to bet
	public void gameEnded();

	public void playerBusted(Set<Player> player);

	/**
	 * Called by Pot whenever there's some got a money from the pot
	 *
	 * @param last    if false winner are just LevelWinner, ther will be other callback of gameWinners, true if these winner(s) win the last pot
	 * @param winners can be level winners (side pot winner) or game winners
	 */
	public void gameWinners(boolean last, Set<Player> winners);
}
