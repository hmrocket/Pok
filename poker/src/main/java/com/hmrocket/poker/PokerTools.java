package com.hmrocket.poker;

import com.hmrocket.poker.card.HandHoldem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by hmrocket on 15/10/2015.
 */
public class PokerTools {

	public static final boolean DEBUG = true;
	private static final int MIN_BUY_IN_MULTIPLYER = 10;
    private static final int MAX_BUY_IN_MULTIPLYER = 200;

	public static long getMinBuyIn(long minBet) {
		return minBet * MIN_BUY_IN_MULTIPLYER;
    }

    public static long getMaxBuyIn(long minBet) {
        return minBet * MAX_BUY_IN_MULTIPLYER;
    }

    /**
     * @param potentialWinners players who has chance of winning, use {@link Player#isOut()}
     * @return one or more winning hand players, null if the potentialWinners is empty or null
     */
    public static List<Player> getWinners(List<Player> potentialWinners) {
        if (potentialWinners == null || potentialWinners.isEmpty())
            return null;

        List<Player> winners = new ArrayList<Player>();
        Player winner = Collections.max(potentialWinners); // find best hand
        do {
            winners.add(winner);
            potentialWinners.remove(winner);
            winner = Collections.max(potentialWinners); // find the next best hand
            // if next best hand is equal to best hand, add it to winners
        } while (winner.compareTo(winners.get(0)) == 0);

        return winners;
    }

    public static Set<Player> findAllInPlayers(Set<Player> playersInGame) {
        Set<Player> players = new HashSet<>(playersInGame);
        Iterator<Player> iterator = players.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getState() != Player.PlayerState.ALL_IN)
                iterator.remove();
        }
        return players;
    }

	/**
	 * Remove all in players
	 *
	 * @param playersInGame list of players
	 * @return return a Set of ALLIN players (player that are removed)
	 */
	public static Set<Player> removeAllInPlayers(Set<Player> playersInGame) {
		Set<Player> allinPlayers = new HashSet<>();
		Iterator<Player> iterator = playersInGame.iterator();
		while (iterator.hasNext()) {
			Player next = iterator.next();
			if (next.getState() == Player.PlayerState.ALL_IN) {
				iterator.remove();
				allinPlayers.add(next);
			}
		}
		return allinPlayers;
	}

    /**
     * @param potentialWinnersInTheGame players who has chance of winning, use {@link Player#isOut()}
     * @return one or more winning hand players, null if the potentialWinners is empty or null
     */
    public static Set<Player> getWinners(Set<Player> potentialWinnersInTheGame) {
        Set<Player> potentialWinners = new HashSet<>();
        if (potentialWinnersInTheGame == null || potentialWinnersInTheGame.isEmpty())
            return potentialWinners;
		else potentialWinners.addAll(potentialWinnersInTheGame);

		if (potentialWinners.size() == 1)
			return potentialWinners;
		else { // there is more than 1 player
			Set<Player> winners = new HashSet<Player>();
			Player firstWinner = Collections.max(potentialWinners); // find best hand
			winners.add(firstWinner);
			potentialWinners.remove(firstWinner);
			// look for other winners
			Player winner;
			do {
				winner = Collections.max(potentialWinners); // find other best hand
				// if next best hand is equal to best hand, add it to winners, if not break
				if (winner.compareTo(firstWinner) != 0)
					break;
				winners.add(winner);
				potentialWinners.remove(winner);
			} while (!potentialWinners.isEmpty());
			return winners;
		}

    }

	/**
	 * @param handHoldemList list of Hands Holdem
	 * @return Winners hands
	 */
	public static List<HandHoldem> getBestHands(List<HandHoldem> handHoldemList) {
		if (handHoldemList == null || handHoldemList.isEmpty()) {
			return null;
		} else if (handHoldemList.size() == 1)
			return handHoldemList;

		ArrayList<HandHoldem> winners = new ArrayList<>();
		HandHoldem winner = Collections.max(handHoldemList); // find best hand
		do {
			winners.add(winner);
			handHoldemList.remove(winner);
			if (handHoldemList.isEmpty()) {
				break;
			}
			winner = Collections.max(handHoldemList); // find the next best hand
			// if next best hand is equal to best hand, add it to winners
		} while (winner.compareTo(winners.get(0)) == 0);

		return winners;
	}

	/**
	 * @param handStrength
	 * @param turn
	 * @param player
	 * @return handStrength / Pot odds
	 */
	public static float calculateRateOfReturn(float handStrength, Turn turn, Player player) {
		//bet of the player not the the added bet (cause turn.getMoneyOnTable() return money on the table of the other players my money (bet) not included
		return calculateRateOfReturn(handStrength, calculatePotOdds(turn, player));
	}

	public static float calculateRateOfReturn(float handStrength, float potOdds) {
		if (DEBUG) System.out.println("ror: " + handStrength + " / " + potOdds);
		return handStrength / potOdds;
	}

	/**
	 * we need to get the current bet and amount to continue (to be able to calculate the added bet)
	 * so we can calculate Pot Odds
	 *
	 * @param turn
	 * @param player
	 * @return (call cost) / (pot value + call cost)
	 */
	public static float calculatePotOdds(Turn turn, Player player) {
		if (DEBUG && turn.getAmountToContinue() - player.getBet() < 0)
			throw new IllegalArgumentException("added bet is negative bet:" + player.getBet() + " amountToCon:" + turn.getAmountToContinue());
		// Caution turn.getMoneyOnTable return the other Players bet and not my bet that's why totalMoney = bet + moneyOnTable + potValue
		return (turn.getAmountToContinue() - player.getBet()) / (float) (turn.getAmountToContinue() + turn.getPotValue() + turn.getMoneyOnTable());
	}

}
