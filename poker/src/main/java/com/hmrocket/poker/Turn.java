package com.hmrocket.poker;

import com.hmrocket.poker.ai.PokerPosition;

/**
 * Created by hmrocket on 08/10/2015.
 */
public class Turn {

	private static final int START = 0;
	private static final int END = 1;
	/**
	 * represent the state of the turn used only at start/end turn
	 */
	private int turnState;
	private int playerCount;
	private RoundPhase phase;
	/***
	 * Represent how many player played so far in this Round == playersPlayed
	 * c.a.d playersPlayedBefore me
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
	 * Number of player Folded in this Round
	 */
	private int roundPlayersFolded;
	/**
	 * Number of player went folded on this Round
	 */
	private int roundPlayerRaised;
	/**
	 * pot current value
	 */
	private long potValue;
	/**
	 * money will be added to the pot minus yours
	 * c.a.d money on the table excluded you bet
	 */
	private long moneyOnTable;
	/**
	 * this is very important info, user folded => speedup the game, raised => the bot might react
	 */
	private Player.PlayerState humanState; // TODO implement this when you create HumanPlayer

	public Turn(long minBet, int playerCount) {
		reset(minBet, playerCount);
	}

	/**
	 * Reset Turn Stat
	 *
	 * @param minBet
	 * @param playerCount
	 */
	public void reset(long minBet, int playerCount) {
		this.playerCount = playerCount;
		phase = RoundPhase.PRE_FLOP;
		this.minBet = minBet;
		amountToContinue = minBet;
		setPosition(0);
		roundRally = playersFolded = playersAllIn = 0;
		roundPlayersAllIn = roundPlayerRaised = 0;
		potValue = moneyOnTable = 0;
		humanState = null;
		turnState = END;
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

	public RoundPhase getPhase() {
		return phase;
	}

	public void setPhase(RoundPhase phase) {
		this.phase = phase;
		resetRoundTurn();
	}

	/**
	 * Reset the Round variables
	 */
	private void resetRoundTurn() {
		amountToContinue = 0;
		roundPlayersAllIn = roundPlayerRaised = roundRally = 0;
		potValue = moneyOnTable;
		moneyOnTable = 0;
		humanState = null;
		setPosition(0);
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
		return playerCount - playersFolded - playersAllIn <= roundRally - roundPlayersFolded - roundPlayersAllIn;
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
	 * Number of players will or might enter the pot (everyone who fold in this round is excluded of this count)
	 *
	 * @return number of player that are supposed to be playing this PokerRound except the one who folded
	 * In this Round
	 */
	public int getPokerRoundTurnsCount() {
		return getPlayerCount() - getPlayersFolded() - getPlayersAllIn() + roundPlayersAllIn;
	}

	/**
	 * @return total Player number
	 */
	public int getPlayerCount() {
		return playerCount;
	}

	/**
	 * @return Total number of player folded
	 */
	public int getPlayersFolded() {
		return playersFolded;
	}

	/**
	 * @return total allIN Players
	 */
	public int getPlayersAllIn() {
		return playersAllIn;
	}

	/**
	 * @return BB
	 */
	public long getMinBet() {
		return minBet;
	}

	/**
	 * Call this before calling Player.play()
	 *
	 * @param player         playerTurn
	 */
	public void turnStarted(Player player, int PlayerPosition) {
		if (turnState == START)
			turnState = END;
		else throw new IllegalStateException("Call End after starting a turn");

		// remove play money for the table so the player should always look
		// to the money he might gain
		subMoneyOnTable(player.getBet());
	}

	/**
	 * remove a money from the table
	 *
	 * @param money amount ot be removed
	 */
	protected void subMoneyOnTable(long money) {
		moneyOnTable -= money;
	}

	/**
	 * After a player plays use this method to refresh Turn object
	 * update teh stats about players Folded, AllIn, Raised
	 *
	 * @param player player
	 */
	public void turnEnded(Player player) {
		if (turnState == END)
			turnState = START;
		else throw new IllegalStateException("Call start before ending a turn");

		switch (player.getState()) {
			case ALL_IN:
				incrementPlayersAllIn();
				if (player.didRaise(amountToContinue)) setAmountToContinue(player.getBet());
				break;
			case FOLD:
				incrementPlayersFolded();
				break;
			case RAISE:
				incrementRoundRaise();
				setAmountToContinue(player.getBet());
				break;
			case CALL:
			case CHECK:
				break;
		}
		// increment number of player played
		roundRally++;
		// increment the money on the table (or place back)
		addMoneyOnTable(player.getBet());
	}

	/**
	 * Increment the number of player ALL-IN in this RoundPhase and in total
	 */
	public void incrementPlayersAllIn() {
		roundPlayersAllIn++;
		this.playersAllIn++;
	}

	/**
	 * Increment the number of player folded in this RoundPhase and in total
	 */
	public void incrementPlayersFolded() {
		roundPlayersFolded++;
		this.playersFolded++;
	}

	/**
	 * Increment the number of Raise in this RoundPhase
	 */
	public void incrementRoundRaise() {
		roundPlayerRaised++;
	}

	/**
	 * track the money on the table
	 *
	 * @param money money a player will add to the table
	 */
	protected void addMoneyOnTable(long money) {
		moneyOnTable += money;
	}
}
