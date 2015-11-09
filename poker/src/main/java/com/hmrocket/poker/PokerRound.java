/**
 *
 */
package com.hmrocket.poker;

import java.util.List;


/**
 * Make sure that everyone has bet the right amount
 * Handle the rounds
 *
 * @author mhamed
 * @since Oct 8, 2015
 */
public class PokerRound extends Round {

    private RoundPhase phase;
    private RoundEvent roundEvent;
	private Turn turn;

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
		this.turn = new Turn(minBet, playersOrderedRightLeft.size());
		setup(minBet, playersOrderedRightLeft.get(dealerIndex));
		if (PokerTools.DEBUG)
			System.out.println("minBet=" + minBet + ", dealerIndex=" + dealerIndex);
	}

    /**
     * 1) setup round and called amount in that round
     * 2) Handle the first bets
     * Add small blind and big blind before Round start
     * Note: the dealer isn't the one to start first is the big blind player
	 * @param minBet
	 * @param dealer
	 */
	private void setup(long minBet, Player dealer) {
		phase = RoundPhase.PRE_FLOP;

		Player smallBlindPlayer = getLeftPlayer(dealer);
		smallBlindPlayer.raise(minBet / 2); // XXX Failed: 2 (nullpointer)

		// big blind is the last one to finish first round (only first round after it's the dealer)
		Player bigBlindPlayer = getLeftPlayer(smallBlindPlayer);
		bigBlindPlayer.raise(minBet);
		startGame(dealer, bigBlindPlayer);
	}

    /**
     * Play all Poker Rounds until showdown or until all player folded except one
	 * @param button button or dealer is the Player to act last (last position)
	 */
	protected void startGame(Player button, Player bigBlind) {
		// on the PRE_FLOP big blind Player is the
		if (PokerTools.DEBUG) System.out.println("Start: " + phase);
		this.newRound(bigBlind);
		if (roundEvent != null) roundEvent.onRoundFinish(phase, players);
		nextPhase();

		while (phase != RoundPhase.SHOWDOWN && !isAllPlayersNotPlayingExceptOne()) {
			if (PokerTools.DEBUG) System.out.println("Start: " + phase);
			this.newRound(button); // new poker round (not super new round )
			if (roundEvent != null) roundEvent.onRoundFinish(phase, players);
			nextPhase();

		}
		// TODO it will be better to call end Of game here after all this is start game method
		// and this is where it end.
	}

	private void nextPhase() {
		if (phase != RoundPhase.SHOWDOWN) {
			phase =  RoundPhase.values()[phase.ordinal() + 1];
			turn.setPhase(phase);
		}
		else System.out.println("Next Round called at Showdown");
	}

	protected boolean isAllPlayersNotPlayingExceptOne() {
		int numberOfPlayerPlaying = 0;
		for (Player player :
				players) {
			if (player.isPlaying()) {
				numberOfPlayerPlaying++;
				if (numberOfPlayerPlaying > 1) return false;
			}
		}
		return true;
	}

    /**
     * @return next Player still in the game
     */
    @Override
    public Player nextTurn() {
        Player nextPlayer = null;
        do {
			// loop until you find a player active
			Player player = super.nextTurn();
			if (player == null)
			break;
			else if (player.isPlaying()) {
				nextPlayer = player;
				break;
			}
			// while Round not finished continue
        } while (super.isCompleted() == false);
        return nextPlayer;
    }

	/**
	 * Get left <u>not out Player</u>
	 *
	 * @param player
	 * @return Get left Playing player or simply next turn without incrementing the turn
	 */
	@Override
	public Player getLeftPlayer(Player player) {
		return getLeftPlayer(player, 0);
	}

	/**
	 * The <u>Playing</u> Player on the left of <code>player</code> without incrementing the turn
	 * or considering if the Round was completed or not
	 *
	 * @param player
	 * @param skippedPlayers positive number (can be zero) represent the number of player skipped
	 *                       after <code>player</code>
	 * @return The Playing Player on the left of <code>player</code> after skipping n players
	 */
	@Override
	public Player getLeftPlayer(Player player, int skippedPlayers) {
		Player nextPlayingPlayer = super.getLeftPlayer(player, skippedPlayers);
		while (nextPlayingPlayer.isPlaying() == false) {
			nextPlayingPlayer = super.getLeftPlayer(nextPlayingPlayer);
		}
		return nextPlayingPlayer;
	}

	@Override
	protected boolean isCompleted() {

		return super.isCompleted() || phase == RoundPhase.SHOWDOWN || isAllPlayersNotPlayingExceptOne();
	}

	@Override
	void removePlayer(Player player) {
		// The plan was to remove player who folded or quit, now changed (the pot will handle that)
		// Don't remove player
		super.removePlayer(player);
		// If I remove folded player... bet where should it go - should I even remove it ?
		// bets.remove(player);
	}

	/**
	 * Poker Round only ends when (1) only one player is left or
	 * (2) all remaining players have matched the highest total bet made during the round.
	 *
	 * @param button the player who acts last on that Round
	 */
	@Override
	protected void newRound(Player button) {
		Player playerToStart = getLeftPlayer(button);
		super.newRound(playerToStart);
		do {
			turn.turnStarted(playerToStart, super.playerTurn);
			playerToStart.play(turn.getAmountToContinue()); // player play a move
			if (PokerTools.DEBUG) System.out.println(playerToStart);
			if (playerToStart.didRaise(turn.getAmountToContinue())) {
				super.newRound(playerToStart); // New Round not Poker Round
				// nextTurn();
				if (PokerTools.DEBUG) System.out.println("--new Round--");
			}
			// update calledAmount and Start new raising Round
			turn.turnEnded(playerToStart);
			playerToStart = nextTurn();
		} while (playerToStart != null);
	}

    protected interface RoundEvent {
        public void onRoundFinish(RoundPhase phase, List<Player> players);

        public void onRaise();

    }

}

