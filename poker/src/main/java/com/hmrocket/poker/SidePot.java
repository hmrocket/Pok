package com.hmrocket.poker;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Side pots develop from the fact that players can only win a maximum of the amount that they wagered from each of the players who call them. Thus any wagers above that amount
 * are placed in a separate pot which is competed for by everyone except for the all-in player.
 * Created by hmrocket on 18/10/2015.
 */
public class SidePot implements Comparable<SidePot> {

    private Set<Player> allInPlayers;
    private long totalValue;
    private long allInValue;

    public SidePot(Player allInPlayer) {
        totalValue = 0;
        allInPlayers = new HashSet<Player>();
        allInValue = allInPlayer.getBet();
        addAllInPlayer(allInPlayer);
    }

    /**
     * read class description
     *
     * @param player must be all in player or it will throw an exception
     * @return true if the player was added to this Side Pot
     */
    public boolean addAllInPlayer(Player player) {
        if (player != null && player.getState() == Player.PlayerState.ALL_IN) {
            Iterator<Player> iterator = allInPlayers.iterator();
            if (iterator.hasNext() == false || iterator.next().getBet() == player.getBet()) {
                // this player belong to this sidePot
                // http://poker.stackexchange.com/questions/462/how-are-side-pots-built
                allInPlayers.add(player);
                return true;
            } else return false;

        } else
            throw new IllegalArgumentException("Player is either null or doesn't have all in state");
    }

    public Set<Player> getAllInPlayers() {
        return allInPlayers;
    }

    public boolean playerExist(Player player) {
        if (player == null || player.getState() != Player.PlayerState.ALL_IN)
            return false;
        else if (allInPlayers == null || allInPlayers.contains(player) == false)
            return false;
        else return true;
    }

    /**
     * @return busted players if there is any
     */
    public Set<Player> showdown(Set<Player> mainPotWinners) {
        mainPotWinners.addAll(allInPlayers);
        PokerTools.getWinners(mainPotWinners);
    }

    public void setTotalValue(long totalValue) {
        this.totalValue = totalValue;
    }

    public void addBet(Player player) {
        if (player.getBet() > allInValue) {
            totalValue += allInValue;
            player.setBet(player.getBet() - allInValue);
        } else {
            if (player.getBet() == allInValue && player.getState() == Player.PlayerState.ALL_IN) {
                addAllInPlayer(player);
            }
            totalValue += player.getBet();
            player.setBet(0);
        }
    }

    @Override
    public int compareTo(SidePot o) {
        if (o == null || totalValue > o.totalValue)
            return 1;
        return totalValue == o.totalValue ? 0 : -1;
    }
}
