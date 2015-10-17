package com.hmrocket.poker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by hmrocket on 07/10/2015.
 */
public class Table implements Game.GameEvent {

    private List<Player> players;
    private int seatsAvailable;
    private List<Seat> seats;
    private int dealer;
    private Game game;
    private long minBet;

    public static void main(String[] args) {
        Table table = new Table(9, 1000L);
        Player kais = new Player("Kais", (long) 2e6, (long) 2e6);
        Player mhamed = new Player("Mhamed", (long) 1e7, (long) 2e6);
        Player kevin = new Player("Kevin", 450633L, (long) 1e6);
        table.addPlayer(kais, 0);
        table.addPlayer(mhamed, 3);
        table.addPlayer(kevin, 6);

        table.startGame();
        // plays next hand
        Player itachi = new Player("Itachi", (long) 10e7, 182000L);
        table.addPlayer(itachi, 8);
        Player yassin = new Player("Yassin", (long) 4e6, 100000L);
        table.addPlayer(yassin, 2);
        table.removePlayer(itachi);
        table.addPlayer(itachi, 1);
    }

    public Table(int capacity, long minBet) {
        this.players = new ArrayList<Player>(capacity);
        this.seats = new ArrayList<Seat>(capacity);
        for (int i = 0; i < capacity; i++) {
            seats.add(new Seat());
            players.add(null);
        }
        this.seatsAvailable = capacity;
        this.minBet = minBet;
        this.game = new Game(this);
    }

    private int nextDealer() {
        if (seatsAvailable == players.size() || seatsAvailable == players.size() - 1)
            return dealer;
        while (dealer++ < players.size()) {
            if (players.get(dealer) == null)
                continue;
            else return dealer;
        }
        dealer = -1;
        return nextDealer();
    }

    public void addPlayer(Player player, int seatId) {
        if (seatId >= players.size() || seatId < 0) {
            throw new IllegalArgumentException("No seat available at index " + seatId);
        } else if (seatsAvailable <= 0 || !seats.get(seatId).isAvailable())
            System.out.println("Seat Not available");
        else if (players.contains(player))
            System.out.println("Player exist and already added");
        else if (player.getCash() < PokerTools.getMinBuyIn(minBet))
            System.out.println("Player exist and already added");
        else {
            players.add(seatId, player);
            seats.get(seatId).setStatus(Seat.Status.UNAVAILABLE);
            seatsAvailable--;
        }
    }

    public void removePlayer(Player player) {
        int seatIndex;
        if (players != null && (seatIndex = players.indexOf(player)) > 0) {
            seatsAvailable++;
            seats.get(seatIndex).setStatus(Seat.Status.AVAILABLE);
            players.set(seatIndex, null);
            // TODO if player is active must inform the game
            // game.removePlayer()
        }
    }

    public void startGame() {
        int playerIndex = nextDealer();
        game.startNewHand(minBet, players, playerIndex);

    }

    @Override
    public void gameEnded() {
        // Update the list of players of this new hand
        startGame();
    }

    @Override
    public void playerBusted(Set<Player> playersBusted) {
        // Player doesn't have enough cash to Buy-In
        Iterator<Player> iterator = playersBusted.iterator();
        while (iterator.hasNext())
            removePlayer(iterator.next());
    }
}
