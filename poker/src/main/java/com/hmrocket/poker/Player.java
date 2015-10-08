package com.hmrocket.poker;

import com.hmrocket.poker.card.HandHoldem;

/**
 * Created by hmrocket on 04/10/2015.
 */
public class Player { //TODO what's the needed attribute that player need
    private HandHoldem hand;
    private String name;
    private long bankBalance;
    private long cash;


    @Override // FIXME regenerate
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return !(hand != null ? !hand.equals(player.hand) : player.hand != null);

    }

    @Override // FIXME regenerate
    public int hashCode() {
        return hand != null ? hand.hashCode() : 0;
    }
}
