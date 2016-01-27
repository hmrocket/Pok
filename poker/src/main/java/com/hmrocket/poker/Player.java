package com.hmrocket.poker;

import com.hmrocket.poker.card.HandHoldem;

import java.util.Comparator;

/**
 * Created by hmrocket on 04/10/2015.
 */
public abstract class Player implements Comparable<Player> {
	protected HandHoldem handHoldem;
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
				return Long.valueOf(o1.cash).compareTo(o2.cash);
			}
        }
    };
	protected Seat seat;
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
				return Long.valueOf(o1.bet).compareTo(o2.bet);
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
		if (handHoldem == null && o.handHoldem == null || isOut() && o.isOut())
			return 0; // return 0 if both player has not hands or both of them folded
		else if (handHoldem == null || this.isOut()) // lose automatically if he folded
			return -1;
		else if (o.handHoldem == null || o.isOut()) // lose automatically if he folded
			return 1;
		return handHoldem.compareTo(o.handHoldem);
	}

	/**
	 * @return true if the player has folded or inactive
	 */
	public boolean isOut() {
		return state == PlayerState.FOLD || state == PlayerState.INACTIVE || state == PlayerState.Zzz;
	}

    public long getCash() {
        return cash;
    }

    public void setCash(long cash) {
        this.cash = cash;
    }

	public long getBankBalance() {
		return bankBalance;
	}

	public void setBankBalance(long bankBalance) {
		this.bankBalance = bankBalance;
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

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public HandHoldem getHandHoldem() {
		return handHoldem;
	}

	public void setHandHoldem(HandHoldem handHoldem) {
		this.handHoldem = handHoldem;
	}

    public void fold() {
        state = PlayerState.FOLD;
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

	public void check() {
		state = PlayerState.CHECK;
	}

	public void allIn() {
		addBet(cash);
		state = PlayerState.ALL_IN;
	}

	private void addBet(long amount) {
		//not protected from any of this situation (amount > cash or amount < 0)
		cash -= amount;
		bet += amount;
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

	/**
	 * This method will check if the amount fall under what status allin, Raise, Call, check, or Fold
	 * <ul>
	 * <li>It's an allin if the amount is equal to {@link #getBet() bet} + {@link #getCash() cash}</li>
	 * <li>It's a raise/allin if the amount is higher or equal to MinRaise</li>
	 * <li>It's a call/check/allin if the amount is lower than minRaise but higher than minCall</li>
	 * <li>It's a fold otherwise</li>
	 * </ul>
	 * Note
	 *
	 * @param amount the bet amount
	 * @param turn   object containing the <code>minRaiseAmount</code> and <code>minAmountToContinue</code>
	 *               needed to decide which move is correct with that bet
	 */
	public void autoMove(long amount, Turn turn) {
		if (amount >= bet + cash) {
			allIn();
		} else if (amount >= turn.getMinRaise()) {
			raise(amount);
		} else if (amount >= turn.getAmountToContinue()) {
			call(turn.getAmountToContinue());
		} else fold();
	}

	/**
	 * Request an Action
	 *
	 * @param turn game stats and info related to the turn
	 */
	public abstract void play(Turn turn);

	@Override
	public String toString() {
		return name + "{" +
				handHoldem +
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
