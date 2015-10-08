package com.hmrocket.poker;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by hmrocket on 07/10/2015.
 */
public class EqualBet {

    private long value;
    private Set<Player> players;

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public EqualBet(long value, Set<Player> players) {
        this.value = value;
        this.players = players;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
