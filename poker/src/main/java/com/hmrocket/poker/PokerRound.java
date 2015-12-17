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
	private int dealerIndex;

	// Only table or game should create this if we will pass this object to the player
	public PokerRound(List<Player> playersOrderedRightLeft, Player dealer) {
		super(playersOrderedRightLeft, dealer);
	}

	public PokerRound(List<Player> playersOrderedRightLeft, int dealer) {
		super(playersOrderedRightLeft, dealer);
	}

	public PokerRound(long minBet, List<Player> playersOrderedRightLeft, int dealerIndex, RoundEvent roundEvent) {
		super(playersOrderedRightLeft, dealerIndex);
		// must be non null
		this.roundEvent = roundEvent;
		if (roundEvent == null) throw new IllegalArgumentException("round event must be non null");
		this.turn = new Turn(minBet, playersOrderedRightLeft.size());
		this.dealerIndex = dealerIndex;

		if (PokerTools.DEBUG)
			System.out.println("minBet=" + minBet + ", dealerIndex=" + dealerIndex);
	}

	/**
	 * Using the dealer's left player as baseline, get playing position of player in the game.
	 *
	 * @param players     list of player ordered from right to left (clock wise)
	 * @param player      current player
	 * @param dealerIndex last player index
	 * @return get the position of a Player in the game
	 */
	protected static int getPosition(List<Player> players, Player player, int dealerIndex) {
		int indexOf = players.indexOf(player);
		if (indexOf == -1)
			throw new IllegalArgumentException("The player doesn't exist in the list");
		else {
			int positionFromDealer = indexOf - dealerIndex;
			// -1 it because deale risn't the one who start but the one who finish last
			return positionFromDealer > 0 ? positionFromDealer - 1 : players.size() - 1 + positionFromDealer;

		}
	}

	/**
	 * setup the game and start rounds
	 */
	public void startGame() {
		Player dealer = players.get(dealerIndex);
		// TODO check if the event OnBlindPosted should be fired before PRE_FLOP
		Player bigBlind = setup(turn.getMinBet(), dealer);
		startGame(dealer, bigBlind);
	}

	/**
	 * 1) setup round and called amount in that round
	 * 2) Handle the first bets
	 * Add small blind and big blind before Round start
	 * Note: the dealer isn't the one to start first is the big blind player
	 *
	 * @param minBet smallest bet amount
	 * @param dealer button of hte game
	 * @return BigBlind player
	 */
	private Player setup(long minBet, Player dealer) {
		phase = RoundPhase.PRE_FLOP;
		roundEvent.onRound(phase);

		Player smallBlindPlayer = getLeftPlayer(dealer);
		smallBlindPlayer.raise(minBet / 2); // XXX Failed: 2 (nullpointer)

		// big blnd is the last one to finish first round (only first round after it's the dealer)
		Player bigBlindPlayer = getLeftPlayer(smallBlindPlayer);
		bigBlindPlayer.raise(minBet);
		roundEvent.onBlindPosted(smallBlindPlayer, bigBlindPlayer);
		turn.onBlindPosted(smallBlindPlayer, bigBlindPlayer);
		return bigBlindPlayer;
	}

	/**
	 * Play all Poker Rounds until showdown or until all player folded except one
	 *
	 * @param button button or dealer is the Player to act last (last position)
	 */
	protected void startGame(Player button, Player bigBlind) {
		// on the PRE_FLOP big blind Player is the
		if (PokerTools.DEBUG) System.out.println("Start: " + phase);
		this.newRound(bigBlind);
		roundEvent.onRoundFinish(phase, players);
		nextPhase();

		while (!isCompleted()) {
			roundEvent.onRound(phase);
			if (PokerTools.DEBUG) System.out.println("Start: " + phase);
			this.newRound(button); // new poker round (not super new round )
			roundEvent.onRoundFinish(phase, players);
			nextPhase();

		}
		// The game isn't finished yet that's why is not a good idea to call end Of game here
	}

	private void nextPhase() {
		if (phase != RoundPhase.SHOWDOWN) {
			phase = RoundPhase.values()[phase.ordinal() + 1];
			turn.setPhase(phase);
		} else System.out.println("Next Round called at SHOWDOWN");
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
		} while (!super.isCompleted());
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
		while (!nextPlayingPlayer.isPlaying()) {
			nextPlayingPlayer = super.getLeftPlayer(nextPlayingPlayer);
		}
		return nextPlayingPlayer;
	}

	@Override
	protected boolean isCompleted() {

		return phase == RoundPhase.SHOWDOWN || turn.isAllPlayersFoldedExceptOne() || turn.isAllPlayersNotPlayingExceptOne();
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
			// get the position of the player in this game
			// we use dealerIndex and not Button cause button might be bigBlind at first not dealer
			int playerPosition = getPosition(players, playerToStart, dealerIndex);
			roundEvent.onPreTurn(playerToStart, turn);
			turn.turnStarted(playerToStart, playerPosition);
			playerToStart.play(turn); // player play a move
			if (PokerTools.DEBUG) System.out.println(playerToStart);
			if (playerToStart.didRaise(turn.getAmountToContinue())) {
				super.newRound(playerToStart); // New Round not Poker Round
				// nextTurn();
				if (PokerTools.DEBUG) System.out.println("--new Round--");
			}
			// update calledAmount and Start new raising Round
			turn.turnEnded(playerToStart);
			roundEvent.onTurnEnded(playerToStart);
			playerToStart = nextTurn();
		} while (playerToStart != null && !turn.isAllPlayersFoldedExceptOne());
	}

	protected interface RoundEvent {
		/**
		 * Called after a PokerRound was finished
		 *
		 * @param phase   Phase of the PokerRound
		 * @param players All players who played this game
		 * @return if the next round should start or not
		 */
		void onRoundFinish(RoundPhase phase, List<Player> players);

		/**
		 * Called by PokerRound before the player start his turn
		 *
		 * @param player Player current turn
		 * @param @param turn contains info about this turn like the last bet amount and the min raise amount
		 */
		void onPreTurn(final Player player, final Turn turn);

		/**
		 * Called after the Player make a move (decision or move can be, fold, raise, call, check, all-in)
		 *
		 * @param player Player just finished his turn
		 */
		void onTurnEnded(Player player);

		/**
		 * Called when a new RoundPhase about to start
		 * Note called on SHOWDOWN
		 *
		 * @param roundPhase Started RoundPhase
		 */
		void onRound(RoundPhase roundPhase);

		/**
		 * Called after forced bets posted by players
		 *
		 * @param smallBlind Player to the left of the dealer button
		 * @param bigBlind   Player to  the left of the <code>smallBlind</code>
		 */
		void onBlindPosted(Player smallBlind, Player bigBlind);
	}

}

