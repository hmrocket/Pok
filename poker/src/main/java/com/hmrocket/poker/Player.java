package com.hmrocket.poker;

import com.hmrocket.poker.card.HandHoldem;

import java.util.Comparator;
import java.util.Random;

/**
 * Created by hmrocket on 04/10/2015.
 */
public class Player implements Comparable<Player> { //TODO what's the needed attribute that player need
    private HandHoldem hand;
    private String name;
    private long bankBalance;
    private long cash;
    private long bet;
    private PlayerState state;


    public enum PlayerState {
        INACTIVE,
        ACTIVE,
        CHECK,
        FOLD,
        RAISE,
        CALL,
        ALL_IN,
        Zzz // sleeping this for The Cosby
    }

    @Override
    public int compareTo(Player o) {
        if (hand == null && o == null || state == PlayerState.FOLD && o.state == state)
            return 0; // return 0 if both player has not hands or both of them folded
        else if (state == PlayerState.FOLD) // lose automatically if he folded
            return -1;
        else if (o.state == PlayerState.FOLD) // lose automatically if he folded
            return 1;
        return hand.compareTo(o.hand);
    }

    /**
     * Player Bet Comparator
     */
    public static Comparator<Player> BET_COMPARATOR = new Comparator<Player>() {
        @Override
        public int compare(Player o1, Player o2) {
            if (o1 == null && o2 == null)
                return 0;
            else if (o2 == null)
                return 1;
            else if (o1 == null)
                return -1;
            else {
                return Long.compare(o1.bet, o2.bet);
            }
        }
    };

    /**
     * Player cash Comparator, Useful to determine all-in amount
     */
    public static Comparator<Player> CASH_COMPARATOR = new Comparator<Player>() {
        @Override
        public int compare(Player o1, Player o2) {
            if (o1 == null && o2 == null)
                return 0;
            else if (o2 == null)
                return 1;
            else if (o1 == null)
                return -1;
            else {
                return Long.compare(o1.cash, o2.cash);
            }
        }
    };

    public Player(String name, long bankBalance, long cash) {
        this.name = name;
        this.bankBalance = bankBalance;
        this.cash = cash;
    }

    public long getCash() {
        return cash;
    }

    /**
     * All in not consider as  just waiting for the end of the match
     *
     * @return
     */
    public boolean isPlaying() {
        return state != PlayerState.ALL_IN && state != PlayerState.INACTIVE
                && state != PlayerState.FOLD && state != PlayerState.Zzz;
    }

    /**
     *
     * @param calledAmount called amount so far not just in the current Round
     * @return true if the player raised the bet
     */
    public boolean didRaise(long calledAmount) {
        return state == PlayerState.RAISE ||state == PlayerState.ALL_IN && (bet > calledAmount);
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void setHand(HandHoldem hand) {
        this.hand = hand;
    }

    public HandHoldem getHand() {
        return hand;
    }

    public void fold() {
        state = PlayerState.FOLD;
    }

    private long bet(long amount) {
        cash -= amount;
        bet += amount;
        return amount;
    }

    public void addCash(long amount) {
        if (amount < 0) {
            System.out.println("Amount can be negative");
            return;
        }
        cash += amount;
        // bet = 0;
    }

    public long raise(long amount) {
        amount = bet(amount);
        state = PlayerState.RAISE;
        return amount;
    }

    public void call(long amount) {
        if (cash > amount) {
            bet(amount);
            state = PlayerState.CALL;
        } else allIn();
    }

    public void allIn() {
        bet(cash);
        state = PlayerState.ALL_IN;
    }

    public void check() {
        state = PlayerState.CHECK;
    }

    // DEBUGGING
    public void play(long amountToContinue) {
        Random r = new Random();
        int action = r.nextInt(5);
        switch (action) {
            case 0:
                fold();
                break;
            case 1:
                long raiseAmount = r.nextInt((int) cash);
                if (amountToContinue > raiseAmount) fold();
                else raise(raiseAmount);
                break;
            case 2:
                long callAmount = r.nextInt((int) cash);
                if (amountToContinue > callAmount) fold();
                else call(amountToContinue);
                break;
        }
    }

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
