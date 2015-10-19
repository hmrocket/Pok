package com.hmrocket.poker.pot;

import com.hmrocket.poker.Player;
import com.hmrocket.poker.PokerTools;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Created by hmrocket on 07/10/2015.
 */
public class Pot {

	private MainPot mainPot;
	private Stack<SidePot> sidePots;
	private long sidePotsTotalValue;


	public Pot() {
		mainPot = new MainPot();
		sidePots = new Stack<SidePot>();
		sidePotsTotalValue = 0;
	}

	public Pot(List<Player> playersInTheGame) {
		mainPot = new MainPot();
		sidePots = new Stack<SidePot>();
		setup(playersInTheGame);
	}

	/**
	 * empty the Pot
	 */
	public void reset() {
		mainPot.reset();
		sidePots.clear();
		sidePotsTotalValue = 0;
	}

	public void setup(List<Player> playersInTheGame) {
		reset();
		// we will add all players only once
		mainPot.init(playersInTheGame);
	}

	/**
	 * @return Pot total worth
	 */
	public long getValue() {
		return sidePotsTotalValue + mainPot.value;
	}

	public void update() {
		if (mainPot.potentialWinners.isEmpty()) // XXX no worries mainPot.potentialWinners will be never null
			return;

		//////// Set Up the sidePots first, If there is any new ones

		Set<Player> allInPlayers = PokerTools.findAllInPlayers(mainPot.potentialWinners);
		Player allInPlayer;

		while (allInPlayers.isEmpty() == false) {
			allInPlayer = Collections.min(allInPlayers, Player.BET_COMPARATOR);
			allInPlayers.remove(allInPlayer);
			mainPot.potentialWinners.remove(allInPlayer); // even if you didn't removed it it's okay (he's bet is already 0)

			// SidePot is created from min allInPlayer and MainPot cause the value of of MainPot will be transferred to the SidePot
			SidePot sidePot = new SidePot(allInPlayer, mainPot);

			for (Player player : mainPot.potentialWinners) {
				sidePot.addBet(player);
			}

			sidePots.add(sidePot);
			sidePotsTotalValue += sidePot.getValue();
		}

		// add pot to the main pot
		Iterator<Player> iterator = mainPot.potentialWinners.iterator();
		Player player;
		while (iterator.hasNext()) {
			player = iterator.next();
			mainPot.value += player.getBet();
			player.setBet(0);
			if (player.isPlaying() == false)
				iterator.remove();
		}
	}


	/**
	 * Distribute level pot To Winners
	 *
	 * @return busted players if there's any
	 */
	public Set<Player> distributeToWinners() {
		Set<Player> busted = new HashSet<Player>();
		Set<Player> levelWinners;

		// Get Winners of MainPot
		levelWinners = PokerTools.getWinners(mainPot.potentialWinners);
		distribute(levelWinners, mainPot.value);

		//As long there is SidePot, (money) on the table, Add cash to winners
		while (sidePots.isEmpty() == false) {
			SidePot sidePot = sidePots.pop();
			// Add MainPot winners with SidePot all in players and re-evaluate the winners
			levelWinners.addAll(sidePot.getAllInPlayers());
			levelWinners = PokerTools.getWinners(levelWinners);
			distribute(levelWinners, sidePot.getValue());

			// every SidePot - check if there is busted players
			// (players went all among and lost)
			for (Player player : sidePot.getAllInPlayers()) {
				if (levelWinners.contains(player))
					busted.add(player);
			}
		}

		return busted;
	}

	/**
	 * Distribute the winning value equally on winners
	 *
	 * @param winners  Set of player who has won the Pot
	 * @param potValue Value of winning
	 */
	private void distribute(Set<Player> winners, long potValue) {
		// distribute level pot To Winners
		long levelWinValue = potValue / winners.size(); // Every Winner will have this amount of money
		for (Player winner : winners) {
			winner.addCash(levelWinValue);
		}

	}


}
