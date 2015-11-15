package com.hmrocket.poker.ai;

import com.hmrocket.poker.Player;
import com.hmrocket.poker.Table;

import java.util.List;
import java.util.Random;

/**
 * Created by hmrocket on 08/11/2015.
 */
public final class BotArenaUtil {

	private static final boolean DEBUG = true;
	private static final Random r = new Random();

	/**
	 * @param tournaments
	 * @param handLimit
	 * @param players
	 * @param buyIn
	 * @param minBet
	 * @return the gain for every player
	 */
	public static final long[] play(int tournaments, int handLimit, final List<Player> players, long buyIn, long minBet) throws InterruptedException {

		long[] gain = new long[players.size()];

		for (int tour = 0; tour < tournaments; tour++) {
			// set up table, hands, dealer
			Table table = new Table(players.size(), minBet);
			for (int i = 0; i < players.size(); i++) {
				Player player = players.get(i);
				player.setCash(buyIn);
				table.addPlayer(player, i);
			}
			if (DEBUG) System.out.println("---> tournament: " + tour);


			// Rule: max 500 Hands
			for (int i = 0; i < 500; i++) {
				table.startGame();
				if (DEBUG) {
					System.out.println("hand:" + i + " playerCount: " + table.getPlayers().size() + " players: \n");
					for (Player player : table.getPlayers()) {
						System.out.println(player);
					}
				}
				if (table.getPlayers().size() == 1)
					break; // we have a winner, the other were busted
			}

			// add gains per players
			long cash;
			for (int i = 0; i < players.size(); i++) {
				cash = players.get(i).getCash();
				gain[i] += cash;
				if (DEBUG)
					if (cash > 1000) System.out.println(players.get(i).getName() + " won " + cash);
			}
		}
		int i = 0;
		if (DEBUG) for (Player player : players) {
			System.out.println(players.get(i).getName() + " won " + gain[i]);
			i++;
		}

		return gain;
	}
}
