package com.hmrocket.poker;

import com.hmrocket.poker.card.HandHoldem;

import java.util.Comparator;

/**
 * Created by hmrocket on 04/10/2015.
 */
public abstract class Player implements Comparable<Player> { //TODO what's the needed attribute that player need
	protected HandHoldem hand;
	protected String name;
	protected long bankBalance;
	protected long cash;
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
	protected long bet;
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
    private PlayerState state;

    public Player(String name, long bankBalance, long cash) {
        this.name = name;
        this.bankBalance = bankBalance;
        this.cash = cash;
    }

    @Override
    public int compareTo(Player o) {
        if (hand == null && o.hand == null || isOut() && o.isOut())
            return 0; // return 0 if both player has not hands or both of them folded
        else if (this.isOut()) // lose automatically if he folded
            return -1;
        else if (o.isOut()) // lose automatically if he folded
            return 1;
        return hand.compareTo(o.hand);
    }

    public long getCash() {
        return cash;
    }

    /**
     * @return the player bet
     */
    public long getBet() {
        return bet;
    }

    public void setBet(long bet) {
        this.bet = bet;
    }

	public String getName() {
		return name;
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
     * @return true if the player has folded or inactive
     */
    public boolean isOut() {
        return state == PlayerState.FOLD || state == PlayerState.INACTIVE || state == PlayerState.Zzz;
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

    public HandHoldem getHand() {
        return hand;
    }

    public void setHand(HandHoldem hand) {
        this.hand = hand;
    }

    public void fold() {
        state = PlayerState.FOLD;
    }

	private void addBet(long amount) {
		//not protected from any of this situation (amount > cash or amount < 0)
		cash -= amount;
        bet += amount;
    }

    public void addCash(long amount) {
        if (amount < 0) {
            System.out.println("Amount can be negative");
            return;
        }
        cash += amount;
        // bet = 0;
    }

    /**
     * @param amount over all amount to bet (Not the amount to add to your bet
     */
    public void raise(long amount) {
		long addValue = amount - bet;
		if (addValue < 0) return;
		if (addValue == 0) check();
		else if (addValue >= cash) {
			allIn();
        } else if (amount != bet) {
            addBet(addValue);
            state = PlayerState.RAISE;
        } else
            call(amount);
    }

    /**
     * @param amount Total amount to bet to continue
     */
    public void call(long amount) {
		long addValue = amount - bet;
		if (addValue < 0) return;
		if (addValue == 0) {
			check();
		} else if (cash > addValue) {
			addBet(addValue);
			state = PlayerState.CALL;
        } else allIn();
    }

    public void allIn() {
		addBet(cash);
		state = PlayerState.ALL_IN;
    }

    public void check() {
        state = PlayerState.CHECK;
    }

	/**
	 * Request an Action
	 *
	 * @param amountToContinue the amount a player must bet to continue
	 */
	public abstract void play(long amountToContinue);

	@Override
	public String toString() {
		return name + "{" +
				hand +
				", cash=" + cash +
				", bet=" + bet +
				", state=" + state +
				'}';
	}

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
}
