package com.hmrocket.poker;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Represent the potential winners money +
 * Created by hmrocket on 18/10/2015.
 */
public class MainPot {
    private Set<Player> potentialWinners; // potential winners
    private long totalValue; // represent the total amount (potential winner money + losers money)

    public MainPot(Collection<Player> playersInGame) {
        totalValue = 0;
        potentialWinners.addAll(playersInGame);

        // look for All In player
        // for every all in you create a side pot
    }

    public List<SidePot> update() { //TODO implemnt this
        return null;
    }
}
