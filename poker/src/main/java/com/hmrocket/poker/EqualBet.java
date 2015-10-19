package com.hmrocket.poker;

import java.util.Set;

/**
 * Represent total value of bets in that round and potential winners
 * Created by hmrocket on 07/10/2015.
 */
public class EqualBet {

    private long value;
    /**
     * potential Winners in that Round (use Player.isOut() == false)
     */
    private Set<Player> players;

    public EqualBet(long value, Set<Player> players) {
        this.value = value;
        this.players = players;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    /**
     *
     * @return the number of player who has bet the same amount
     */
    public int count() {
        return players.size();
    }

}
