package com.hmrocket.poker;

import com.hmrocket.poker.card.CommunityCards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by hmrocket on 07/10/2015.
 */
public class Table implements GameEvent {

    private List<Player> players;
    private int seatsAvailable;
    private List<Seat> seats;
	private int dealer;
	private Game game;
    private long minBet;
	private GameEvent lisGameEvent;

	/**
	 * Create a poker Table
	 *
	 * @param capacity number of Seat the table has
	 * @param minBet   the smallest bet amount allowed
	 */
	public Table(int capacity, long minBet) {
        this.players = new ArrayList<Player>(capacity);
        this.seats = new ArrayList<Seat>(capacity);
        for (int i = 0; i < capacity; i++) {
			seats.add(new Seat(i));
			players.add(null);
        }
        this.seatsAvailable = capacity;
        this.minBet = minBet;
        this.game = new Game(this);
	}

	/**
	 * Proportionately distribute players around the table
	 * the already sitting players won't be removed
	 *
	 * @param players collection of players to add to the table
	 */
	public void populate(Collection<Player> players) {
		// preference placement is on the right then left
		int[] seatIdsOrganized = null;
		if (players.size() <= 5) {
			int[] seatIdsOrganized5Max = new int[]{0, 5, 4, 7, 2};
			seatIdsOrganized = seatIdsOrganized5Max;
		} else if (players.size() <= 7) {
			int[] seatIdsOrganized7Max = new int[]{0, 5, 4, 8, 1, 6, 3};
			seatIdsOrganized = seatIdsOrganized7Max;
		}
		int i = 0;
		for (Player p : players) {
			addPlayer(p, seatIdsOrganized == null ? i : seatIdsOrganized[i]);
			i++;
		}
	}

	/**
	 * Add a new Player to Table
	 * @param player Player to add to the Table
	 * @param seatId Player's picked Seat id (position on the table not the game)
	 */
	public void addPlayer(Player player, int seatId) {
		if (seatId >= seats.size() || seatId < 0) {
			throw new IllegalArgumentException("No seat available at index " + seatId);
		} else if (player == null)
			System.out.println("Player is null");
		else if (seatsAvailable <= 0 || !seats.get(seatId).isAvailable())
			System.out.println("Seat Not available for " + player.getName());
		else if (players.contains(player))
			System.out.println(player.getName() + " exist and already added");
		else if (player.getCash() < PokerTools.getMinBuyIn(minBet))
			System.out.println(player.getName() + " doesn't have enough to buy-in");
		else {
			seatsAvailable--; // no need to check if (players.get(seatId) == null) cause the seat is available
			players.set(seatId, player);
			Seat seat = seats.get(seatId);
			seat.setStatus(Seat.Status.UNAVAILABLE);
			player.setSeat(seat);
		}
	}

	/**
	 * Start new poker Game
	 */
	public void startGame() {
		Player PlayerDealer = nextDealer();
		List<Player> playersInTheGame = getPlayers();
		game.startNewHand(minBet, playersInTheGame, playersInTheGame.indexOf(PlayerDealer));

	}

	/**
	 * Increment the dealer
	 *
	 * @return a Player who became a dealer
	 */
	protected Player nextDealer() {
		int currentDealer = dealer;

		//Increment dealer until find a non-null player
		for (dealer++; dealer < players.size(); dealer++) {
			Player player = players.get(dealer);
			if (player == null || player.getState() == Player.PlayerState.INACTIVE)
				continue;
			else
				return player;
		}

		for (dealer = 0; dealer <= currentDealer; dealer++) {
			Player player = players.get(dealer);
			if (player == null)
				continue;
			else
				return player;
		}

		return null;
	}

	/**
	 * Get Players on the Table
	 * @return list of added Players (ordered from right to left)
	 */
	public List<Player> getPlayers() {
		List<Player> playersInGame = new ArrayList<>(players);
		Iterator<Player> iterator = playersInGame.iterator();
		while (iterator.hasNext()) {
			if (iterator.next() == null)
				iterator.remove();
		}
		// Return a list of Player in the game (ordered from right to left)
		return playersInGame;
	}

	/**
	 * Get the dealer
	 *
	 * @return dealer seatId
	 */
	public int getDealer() {
		return dealer;
	}

	/**
	 * Set a GameEvent listener
	 *
	 * @param lisGameEvent
	 */
	public void setGameEventListener(GameEvent lisGameEvent) {
		this.lisGameEvent = lisGameEvent;
	}

	@Override
	public void gameEnded() {
		// Update the list of players of this new hand
		//startGame();
		if (lisGameEvent != null) lisGameEvent.gameEnded();
	}

    @Override
    public void playerBusted(Set<Player> playersBusted) {
        // Player doesn't have enough cash to Buy-In
        Iterator<Player> iterator = playersBusted.iterator();
        while (iterator.hasNext())
            removePlayer(iterator.next());

		if (lisGameEvent != null) lisGameEvent.playerBusted(playersBusted);
	}

	@Override
	public void gameWinners(boolean last, boolean isShowdown, Set<Player> winners, long winAmount) {
		// Table just need a callback when a player doesn't have enough money to bet and should be removed from the table
		if (lisGameEvent != null) lisGameEvent.gameWinners(last, isShowdown, winners, winAmount);
	}

	@Override
	public void onPreTurn(Player player, Turn turn) {
		if (lisGameEvent != null) lisGameEvent.onPreTurn(player, turn);
	}

	@Override
	public void onTurnEnded(Player player) {
		if (lisGameEvent != null) lisGameEvent.onTurnEnded(player);
	}

	@Override
	public void onRound(RoundPhase roundPhase) {
		if (lisGameEvent != null) lisGameEvent.onRound(roundPhase);
	}

	@Override
	public void onBlindPosted(Player smallBlind, Player bigBlind) {
		if (lisGameEvent != null) lisGameEvent.onBlindPosted(smallBlind, bigBlind);
	}

	@Override
	public void onShowdown(Set<Player> potentialWinners) {
		if (lisGameEvent != null) lisGameEvent.onShowdown(potentialWinners);
	}

	@Override
	public void onPotChanged(long potValue) {
		if (lisGameEvent != null) lisGameEvent.onPotChanged(potValue);
	}

	@Override
	public void onCommunityCardsChange(RoundPhase roundPhase, CommunityCards communityCards) {
		if (lisGameEvent != null) lisGameEvent.onCommunityCardsChange(roundPhase, communityCards);
	}

	/**
	 * Remove a Player from the Table
	 *
	 * @param player to remove
	 */
	public void removePlayer(Player player) {
		int seatIndex;
		if (players != null && (seatIndex = players.indexOf(player)) != -1) {
			seatsAvailable++;
			seats.get(seatIndex).setStatus(Seat.Status.AVAILABLE);
			players.set(seatIndex, null);
			// TODO if player is active must inform the game
			// game.removePlayer()
		}
	}

}
