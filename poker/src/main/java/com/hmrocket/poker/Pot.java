package com.hmrocket.poker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by hmrocket on 07/10/2015.
 */
public class Pot {

    private long value;
    // there might be more than 4 equalBets (pre-flop, flop, turn, river)
    private Stack<EqualBet> equalBets;

    public Pot () {

    }

    public void addBet(HashMap<Player, Long> bets) {
        // FIXME This way of adding equal bets might be slow; and it can be optimized
        // check if all  players has bet the same amount (->if not separate bet into two or more)
        // check if the number of players equal the last bet last bet (->merge both equalBets)
        if (isEqualBets(bets)) {
            int PlayersNumberLastBet = this.equalBets.peek().getPlayers().size();
            if (PlayersNumberLastBet == bets.keySet().size()) {
                long newAmount = this.equalBets.peek().getValue() + bets.values().iterator().next();
                this.equalBets.peek().setValue(newAmount);
            }else {
                equalBets.add(new EqualBet(bets.values().iterator().next(), bets.keySet()));
            }
        } else {
            Map.Entry<Player, Long> min = findMin(bets);
            equalBets.add(new EqualBet(min.getValue(), bets.keySet()));
            for (Player player : bets.keySet()) {
                if (player.equals(min.getKey()))
                    bets.remove(player);
                else bets.put(player, bets.get(player) - min.getValue());
            }
            addBet(bets);

        }

    }

    private Map.Entry<Player, Long> findMin(HashMap<Player, Long> bets) {
        Map.Entry<Player, Long> min = null;
        for (Map.Entry<Player, Long> entry : bets.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }
        return min;
    }

    private boolean isEqualBets(HashMap<Player, Long> bets) {
        Iterator<Long> valuesList = bets.values().iterator();
        long amount = valuesList.next();
        while (valuesList.hasNext()) {
            if (amount != valuesList.next())
                return false;
        }
        return true;
    }

    private boolean isBetsEqual(HashMap<Player, Long> bets) {
        Iterator<Long> amountsIterator = bets.values().iterator();
        long amount = amountsIterator.next();
        while (amountsIterator.hasNext()) {
            if (Long.compare(amount, amountsIterator.next()) != 0)
                return false;
        }

        // All bets are equals
        return true;
    }

}
