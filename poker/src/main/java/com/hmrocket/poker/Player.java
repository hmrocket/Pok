package com.hmrocket.poker;

import com.hmrocket.poker.card.HandHoldem;

import java.util.Random;

/**
 * Created by hmrocket on 04/10/2015.
 */
public class Player { //TODO what's the needed attribute that player need
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

    public Player(String name, long bankBalance, long cash) {
        this.name = name;
        this.bankBalance = bankBalance;
        this.cash = cash;
    }

    public long getCash() {
        return cash;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void setHand(HandHoldem hand) {
        this.hand = hand;
    }

    public void fold() {
        state = PlayerState.FOLD;
    }

    private void bet(long amount) {
        cash -= amount;
        bet += amount;
    }

    public void raise(long amount) {
        bet(amount);
        state = PlayerState.RAISE;
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
                long raiseAmount = r.nextInt((int) cash) ;
                if (amountToContinue > raiseAmount) fold();
                else raise(raiseAmount);
                break;
            case 2:
                long callAmount = r.nextInt((int) cash) ;
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
