package com.hmrocket.poker;

import com.hmrocket.poker.ai.PokerPosition;

/**
 * Created by hmrocket on 08/10/2015.
 */
public class Turn {

	private final int playerCount;
	private RoundPhase phase;
	/***
	 * Represent how many player played so far in this Round == playersPlayed
	 */
	private int roundRally;
	/**
	 * The amount of money a player need to put on the table or go all-in to be in the game
	 */
	private long amountToContinue;
	/**
	 * the min amount to bet
	 */
	private long minBet;
	/**
	 * position of the player from 0 -> playerCount -1
	 * This position represent the position in the Game and not for a specific Round
	 * for specific Player, while playing a Hand the position should be always the same,
	 * it shouldn't change during the Rounds
	 */
	private int position;
	/**
	 * Early, Midle, late d
	 */
	private PokerPosition pokerPosition;
	/**
	 * NUmber of player folded on this Game (a.k.a Hand)
	 */
	private int playersFolded;
	/**
	 * Number of player went all in this Game (ak.a Hand)
	 */
	private int playersAllIn;
	/**
	 * Number of player went all in this Round
	 */
	private int roundPlayersAllIn;
	/**
	 * Number of player went folded on this Round
	 */
	private int roundPlayerRaised;
	/**
	 * pot current value
	 */
	private long potValue;
	/**
	 * money will be added to the pot
	 */
	private long moneyOnTable;
	/**
	 * this is very important info, user folded => speedup the game, raised => the bot might react
	 */
	private Player.PlayerState humanState;

	public Turn(long minBet, int playerCount) {
		this.playerCount = playerCount;
		phase = RoundPhase.PRE_FLOP;
		amountToContinue = minBet;
		setPosition(0);

	}

	private void setPokerPosition() {
		if (phase == RoundPhase.PRE_FLOP)
			this.pokerPosition = PokerPosition.getPositionOnPreFlop(playerCount, position);
		else
			this.pokerPosition = PokerPosition.getPosition(playerCount, position);
	}

	public long getAmountToContinue() {
		return amountToContinue;
	}

	public void setAmountToContinue(long amountToContinue) {
		this.amountToContinue = amountToContinue;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
		setPokerPosition();
	}

	public void incrementPosition() {
		this.position++;
		setPokerPosition();
	}

	public RoundPhase getPhase() {
		return phase;
	}

	public void setPhase(RoundPhase phase) {
		this.phase = phase;
		resetRoundTurn();
	}

	private void resetRoundTurn() {
		amountToContinue = 0;
		roundPlayersAllIn = 0;
		humanState = null;
		position = 0;
	}

	public PokerPosition getPokerPosition() {
		return pokerPosition;
	}

	public long getMoneyOnTable() {
		return moneyOnTable;
	}

	/**
	 * Check if has been a re-raise after you in this phase.
	 *
	 * @return true if the player is actually playing again on the same RoundPhase, false otherwise
	 */
	public boolean isRaisedAfter() {
		return playerCount - playersFolded - playersAllIn <= roundRally;
	}

	/**
	 * Check if someone raised before you in this phase
	 *
	 * @return true if someone played before you in this phase and raised, false otherwise
	 */
	public boolean isRaisedBefore() {
		return roundPlayerRaised > 0;
	}

	public boolean isEveryoneFoldOnPreflop() {
		return playersFolded == roundRally;
	}

	public long getPotValue() {
		return potValue;
	}

	/**
	 * get the number of player will play in this Round
	 * @return the number of player that are supposed to be playing this PokerRound
	 */
	public int getPokerRoundTurnsCount() {
		return getPlayerCount() - getPlayersFolded() - getPlayersAllIn() + roundPlayersAllIn;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public int getPlayersFolded() {
		return playersFolded;
	}

	public void setPlayersFolded(int playersFolded) {
		this.playersFolded = playersFolded;
	}

	public int getPlayersAllIn() {
		return playersAllIn;
	}

	public void setPlayersAllIn(int playersAllIn) {
		this.playersAllIn = playersAllIn;
	}

	public long getMinBet() {
		return minBet;
	}
}
