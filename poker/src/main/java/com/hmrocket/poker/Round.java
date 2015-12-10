package com.hmrocket.poker;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * <ul>
 * <li>Generic class that define Round on a list of player</li>
 * <li>The Round is completed when all player has played their turns</li>
 * <li>By default the direction is from right to left</li>
 * </ul>
 */
public class Round {

	/**
	 * Active {@link Player} in this round. Turn direction may varies
	 */
	protected List<Player> players;
	protected int playerTurn; // current turn
	protected int turnLeft; //
	private int turnStart;
	private int turnIncrement = 1;

	protected Round(List<Player> playersOrderedRightLeft, Player playerToStart) {
		init(playersOrderedRightLeft, playersOrderedRightLeft.indexOf(playerToStart), true);
	}

	protected Round(List<Player> playersOrderedRightLeft, int playerToStart) {
		init(playersOrderedRightLeft, playerToStart, true);
	}

	protected Round(List<Player> playersOrdered, boolean rightToLeft) {
		init(playersOrdered, 0, rightToLeft);
	}

	protected Round(List<Player> playersOrdered, int playerTurn, boolean rightToLeft) {
		init(playersOrdered, playerTurn, rightToLeft);
	}

	/**
	 * return the left Player
	 *
	 * @param players        must be ordered from right to left
	 * @param player
	 * @param skippedPlayers positive int represent the number of player skipped before returning left Player
	 * @return Player on the left of <code>player</code>
	 */
	protected static Player getLeftPlayer(List<Player> players, Player player, int skippedPlayers) {
		if (players == null || players.isEmpty())
			return null;

		skippedPlayers %= players.size(); // skipped player without extra complete Rounds
		int nextPlayerIndex = players.indexOf(player) + 1 + skippedPlayers;
		if (nextPlayerIndex == skippedPlayers) //player doesn't exist in the list (index = -1)
			return null;
		else if (nextPlayerIndex >= players.size()) // player is in the end, left player is the first of list
			return players.get(skippedPlayers);
		else return players.get(nextPlayerIndex);
	}

	private void init(List<Player> playersOrderedRightLeft, int playerToStart, boolean rightToLeft) {
		if (playerToStart >= playersOrderedRightLeft.size() || playerToStart < 0)
			throw new InvalidParameterException("Invalid player starter");
		this.players = playersOrderedRightLeft;
		playerTurn = turnStart = playerToStart;
		turnLeft = playersOrderedRightLeft.size() - 1;
		turnIncrement = rightToLeft ? 1 : -1;
	}

	public Player nextTurn() {
		if (isRoundCompleted()) {
			return null;
		} else {
			turnLeft--;
			increment();
			return players.get(playerTurn);
		}
	}

	/**
	 * The Player on the left of <code>player</code> without incrementing the turn
	 * or considering if the Round was completed or not
	 *
	 * @return The Player on the left of <code>player</code>
	 */
	public Player getLeftPlayer(Player player) {
		return getLeftPlayer(player, 0);
	}

	/**
	 * @param player
	 * @param skippedPlayers positive number (can be zero) represent the number of player skipped
	 *                       after <code>player</code>
	 * @return The Player on the left of <code>player</code> after skipping n players
	 */
	public Player getLeftPlayer(Player player, int skippedPlayers) {
		return Round.getLeftPlayer(players, player, skippedPlayers);
	}

	private void increment() {
		playerTurn += turnIncrement;
		if (playerTurn >= players.size()) {
			playerTurn = 0;
		} else if (playerTurn < 0) {
			playerTurn = players.size() - 1;
		}
	}

	protected boolean isCompleted() {
		return isRoundCompleted();
	}

	/**
	 * Assure encapsulation
	 *
	 * @return true if the round is completed, false if there's more turn to be played
	 */
	private boolean isRoundCompleted() {
		if (turnLeft <= 0) {
			return true;
		} else return false;
	}

	protected void reverseRoundSens() {
		turnIncrement *= -1;

	}

	/**
	 * Remove a Player from this round and all the next one.
	 * Unlike {@link Round#addPlayer} remove Player is possible while the Game is still on
	 *
	 * @param player
	 */
	void removePlayer(Player player) {
		throw new UnsupportedOperationException();
		// 3 scenarios
		// player turn didn't come yet
		// player is playing
		// player finished his turn
		// remove player and reset TurnLeft

		//players.remove(player);
		//reset();
	}

	/**
	 * Add a Player to next round
	 * Note: Player can't be added while the Game is still on (Round not completed)
	 *
	 * @param player
	 */
	void addPlayer(Player player, int index) {
		if (isRoundCompleted() == false)
			throw new IllegalStateException("No Player can be add while the round isn't completed");

		// and adjust turn
		players.add(index, player);
		reset();
	}

	private void reset() {
		turnLeft = players.size() - 1;
		playerTurn = turnStart;
	}

	/**
	 * Finish the Round
	 */
	protected void terminateRound() {
		reset();
	}

	/**
	 * Start new round with the same list of player but different start
	 *
	 * @param playerToStart first Player to start this Round
	 */
	protected void newRound(Player playerToStart) {
		reset();
		// turnStart and playerTurn changed
		playerTurn = turnStart = players.indexOf(playerToStart);
	}

	/**
	 * Start new round using the same list of player. First Player to start this Round is the same
	 */
	protected void newRound() {
		reset();
	}

	/**
	 * get all players
	 *
	 * @return players
	 */
	public List<Player> getPlayers() {
		return players;
	}
}
