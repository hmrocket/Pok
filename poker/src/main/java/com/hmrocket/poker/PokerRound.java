/**
 *
 */
package com.hmrocket.poker;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;


/**
 * @author mhamed
 * @since Oct 8, 2015
 */
public class PokerRound extends Round {


    protected interface RoundCallbacks {
        public void onRoundFinish();

        public void onRaise();
    }

    private HashMap<Player, Long> bets;
    private long calledAmount;


    // Only table or game should create this if we will pass this object to the player
    public PokerRound(List<Player> playersOrderedRightLeft, Player player) {
        super(playersOrderedRightLeft, player);
        bets = new HashMap<>();
    }

    public PokerRound(List<Player> playersOrderedRightLeft, int playerToStart) {
        super(playersOrderedRightLeft, playerToStart);
        bets = new HashMap<>();
    }


    @Override
    public void removePlayer(Player player) {
        super.removePlayer(player);
        // FIXME bet where should it go - should I even remove it ?
        bets.remove(player);
    }

    public void nextTurn(Player player) {

    }

    public void call(Player player) {
        if (bets.containsKey(player)) {
            raise(player, calledAmount);
        } else
            bets.put(player, calledAmount);
    }


    public void placeBet(Player player, long amount) {
        if (bets.containsKey(player)) {
            raise(player, amount);
        } else
            bets.put(player, amount);
    }

    public void raise(Player player, long amount) {
        bets.put(player, bets.get(player) + amount);

    }

    public void fold(Player player) {
        // remove player
        removePlayer(player);
        nextTurn();
    }

}

class Round {

    /**
     * Active {@link Player} in this round. Turn direction may varies
     */
    private List<Player> players;
    private int playerTurn;
    private int turnLeft;
    private int turnStart;
    private int turnIncrement = 1;

    protected Round(List<Player> playersOrderedRightLeft, Player player) {
        init(playersOrderedRightLeft, playersOrderedRightLeft.indexOf(player), true);
    }

    protected Round(List<Player> playersOrderedRightLeft, int playerToStart) {
        init(playersOrderedRightLeft, playerToStart, true);
    }

    private void init(List<Player> playersOrderedRightLeft, int playerToStart, boolean rightToLeft) {
        if (playerToStart >= playersOrderedRightLeft.size() || playerToStart < 0)
            throw new InvalidParameterException("Invalid player starter");
        this.players = playersOrderedRightLeft;
        playerTurn = turnStart = playerToStart;
        turnLeft = playersOrderedRightLeft.size();
        turnIncrement = rightToLeft ? 1 : -1;
    }

    protected Round(List<Player> playersOrdered, boolean rightToLeft) {
        init(playersOrdered, 0, rightToLeft);
    }

    protected Round(List<Player> playersOrdered, int playerTurn, boolean rightToLeft) {
       init(playersOrdered, playerTurn, rightToLeft);
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

    public boolean isCompleted() {
        if (turnLeft <= 0) {
            return true;
        } else return false;
    }

    public void reverseRoundSens() {
        turnIncrement *= -1;

    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void addPlayer(Player player) {
        players.remove(player);
    }

    protected void reset() {
        turnLeft = players.size();
        playerTurn = turnStart;
    }


}
