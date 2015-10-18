package com.hmrocket.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Created by hmrocket on 07/10/2015.
 */
public class Pot {

    private long value;
    // there might be more than 4 equalBets (pre-flop, flop, turn, river)
    private Stack<EqualBet> equalBets;

    public Pot() {
        equalBets = new Stack<EqualBet>();
        reset();
    }

    public void reset() {
        value = 0;
        equalBets.removeAllElements();
    }

    public void addBet(HashMap<Player, Long> bets) {
        // FIXME This way of adding equal bets might be slow; and it can be optimized
        // check if all  players has bet the same amount (->if not separate bet into two or more)
        // check if the number of players equal the last bet last bet (->merge both equalBets)
        if (isEqualBets(bets)) {
            int PlayersNumberLastBet = this.equalBets.peek().getPlayers().size();
            if (PlayersNumberLastBet == bets.size()) {
                long newAmount = this.equalBets.peek().getValue() + bets.values().iterator().next();
                this.equalBets.peek().setValue(newAmount);
            } else {
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

    public boolean isEmpty() {
        return equalBets.isEmpty();
    }

    /**
     * Distribute level pot To Winners
     *
     * @return busted players if there's any
     */
    public Set<Player> distributeToWinners() {
        EqualBet equalBet;
        Set<Player> losers = null;
        Player potentialWinner;
        List<Player> levelWinners = new ArrayList<Player>();

        // As long there is EqualBet (money) in the pot, Add cash to winners
        while (!isEmpty()) {
            levelWinners.clear();
            equalBet = equalBets.pop(); // Remove a layer of EqualBet from pot
            losers = equalBet.getPlayers();
            potentialWinner = Collections.max(losers);
            while (potentialWinner != null) {
                // potentialWinner is a levelWinner, remove it form losers
                levelWinners.add(potentialWinner);
                losers.remove(potentialWinner);
                // see if there is another winner with a same high score
                Player multipleWinners = Collections.max(losers);
                potentialWinner = potentialWinner.compareTo(multipleWinners) == 0 ? multipleWinners : null;
            }
            // distribute level pot To Winners
            long levelWinValue = equalBet.getValue() * equalBet.count() / levelWinners.size(); // Every Winner will have this amount of money
            for (Player winner : levelWinners) {
                winner.addCash(levelWinValue);
            }
        }

        // first level of the pot - check if there is busted players
        // (players went all among loser)

        if (losers != null || losers.isEmpty()) {
            return null;
        }
        Iterator<Player> iterator = losers.iterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            if (player.getState() != Player.PlayerState.ALL_IN)
                losers.remove(player);
        }
        return losers;
    }


}
