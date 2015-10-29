/**
 *
 */
package com.hmrocket.poker;

import java.security.InvalidParameterException;
import java.util.List;


/**
 * Make sure that everyone has bet the right amount
 * Handle the rounds
 *
 * @author mhamed
 * @since Oct 8, 2015
 */
public class PokerRound extends Round {


    private long[] calledAmountByRound;
    private RoundPhase phase;
    private RoundEvent roundEvent;
    // Only table or game should create this if we will pass this object to the player
    public PokerRound(List<Player> playersOrderedRightLeft, Player dealer) {
        super(playersOrderedRightLeft, dealer);
    }

    public PokerRound(List<Player> playersOrderedRightLeft, int dealer) {
        super(playersOrderedRightLeft, dealer);
    }

	public PokerRound(long minBet, List<Player> playersOrderedRightLeft, int dealerIndex, RoundEvent roundEvent) {
		super(playersOrderedRightLeft, dealerIndex);
		this.roundEvent = roundEvent;
		setup(minBet);
		if (PokerTools.DEBUG)
			System.out.println("minBet=" + minBet + ", dealerIndex=" + dealerIndex);
	}

    /**
     * 1) setup round and called amount in that round
     * 2) Handle the first bets
     * Add small blind and big blind before Round start
     * Note: the dealer isn't the one to start first is the big blind player
     */
    private void setup(long minBet) {
        phase = RoundPhase.PRE_FLOP;
		calledAmountByRound = new long[RoundPhase.getBetRoundsCount()];
		calledAmountByRound[phase.ordinal()] = minBet;

        Player smallBlindPlayer = super.nextTurn(); // I prefer using super rather this for no reason
		smallBlindPlayer.raise(minBet / 2); // XXX Failed: 1 (nullpointer)

        // big blind is the one to start the game
        Player bigBlindPlayer = super.nextTurn(); // I prefer using super rather this for no reason
		bigBlindPlayer.raise(minBet);
		startGame(bigBlindPlayer);
    }

    /**
     * Play all Poker Rounds until showdown or until all player folded except one
     * @param startPlayer b
     */
    protected void startGame(Player startPlayer) {

		do {
			if (PokerTools.DEBUG) System.out.println("Start: " + phase);
			this.newRound(startPlayer); // new poker round (not super new round )
			if (roundEvent != null) roundEvent.onRoundFinish(phase, players);
			nextPhase();

		} while (isCompleted() == false);

    }

	private void nextPhase() {
		if (phase != RoundPhase.RIVER)
            phase =  RoundPhase.values()[phase.ordinal() + 1];
        else System.out.println("Next Round called at River");
    }

    /**
     * Poker Round only ends when (1) only one player is left or
     * (2) all remaining players have matched the highest total bet made during the round.
     *
     * @param playerToStart first Player to start this Round
     */
    @Override
    protected void newRound(Player playerToStart) {
        super.newRound(playerToStart);
        do {
            // FIXME either
			playerToStart.play(calledAmountByRound[phase.ordinal()]); // player play a move
			if (PokerTools.DEBUG) System.out.println(playerToStart);
			if (playerToStart.didRaise(calledAmountByRound[phase.ordinal()])) {
                // update calledAmount and Start new raising Round
				calledAmountByRound[phase.ordinal()] = playerToStart.getBet();
				super.newRound(playerToStart); // New Round not Poker Round
				// nextTurn();
				if (PokerTools.DEBUG) System.out.println("--new Round--");
			}
            playerToStart = nextTurn();
		} while (playerToStart != null);
	}

    /**
     * @return next Player still in the game
     */
    @Override
    public Player nextTurn() {
        Player player = null;
        while ((player = super.nextTurn()) != null &&
                player.isPlaying() == false) {
            // loop until you find a player active
        }
        return player;
    }

	private boolean isAllPlayersExceptOneFolded() {
		int numberOfPlayerNotOut = 0;
		for (Player player :
				players) {
			if (player.isOut() == false) {
				numberOfPlayerNotOut++;
				if (numberOfPlayerNotOut > 1) return false;
			}
		}
		return true;
	}

	@Override
	protected boolean isCompleted() {

		return phase == RoundPhase.SHOWDOWN || isAllPlayersExceptOneFolded();
	}

	@Override
	void removePlayer(Player player) {
    // The plan was to remove player who folded or quit, now changed (the pot will handle that)
    // Don't remove player
        super.removePlayer(player);
        // If I remove folded player... bet where should it go - should I even remove it ?
        // bets.remove(player);
    }

    protected interface RoundEvent {
        public void onRoundFinish(RoundPhase phase, List<Player> players);

        public void onRaise();

    }

}

/**
 * <ul>
 * <li>Generic class that define Round on a list of player</li>
 * <li>The Round is completed when all player has played their turns</li>
 * <li>By default the direction is from right to left</li>
 * </ul>
 */
class Round {

    /**
     * Active {@link Player} in this round. Turn direction may varies
     */
    protected List<Player> players;
    private int playerTurn; // current turn
    private int turnLeft; //
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
     * @param players must be ordered from right to left
     * @param player
     * @return Player on the left of <code>player</code>
     */
    protected static Player getLeftPlayer(List<Player> players, Player player) {
        if (players == null || players.isEmpty())
            return null;

        int nextPlayerIndex = players.indexOf(player) + 1;
        if (nextPlayerIndex == 0) //player doesn't exist in the list (index = -1)
            return null;
        else if (nextPlayerIndex == players.size()) // player is in the end, left player is the first of list
            return players.get(0);
        else return players.get(nextPlayerIndex);
    }

    private void init(List<Player> playersOrderedRightLeft, int playerToStart, boolean rightToLeft) {
        if (playerToStart >= playersOrderedRightLeft.size() || playerToStart < 0)
            throw new InvalidParameterException("Invalid player starter");
        this.players = playersOrderedRightLeft;
        playerTurn = turnStart = playerToStart;
        turnLeft = playersOrderedRightLeft.size();
        turnIncrement = rightToLeft ? 1 : -1;
    }

    public Player nextTurn() {
        if (isCompleted()) {
            return null;
        } else {
            turnLeft--;
            increment();
            return players.get(playerTurn);
        }
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
        if (turnLeft <= 0) {
            return true;
        } else return false;
    }

    protected void reverseRoundSens() {
        turnIncrement *= -1;

    }

    /** Remove a Player from this round and all the next one.
     *  Unlike {@link Round#addPlayer} remove Player is possible while the Game is still on
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

    /** Add a Player to next round
     *  Note: Player can't be added while the Game is still on (Round not completed)
     * @param player
     */
    void addPlayer(Player player, int index) {
        if (isCompleted() == false)
            throw new IllegalStateException("No Player can be add while the round isn't completed");

        // and adjust turn
        players.add(index, player);
        reset();
    }

    private void reset() {
        turnLeft = players.size();
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

}
