package com.hmrocket.poker;

import com.hmrocket.poker.ai.bot.RandBot;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by Mhamed on 15-10-26.
 */
public class GameTest extends TestCase implements Game.GameEvent {

	private static final boolean DEBUG = false;

	private static final List<Player> PLAYERS = new ArrayList(Arrays.asList(new RandBot("Kais", (long) 72e6, (long) 100), //1
			new RandBot("Mhamed", (long) 13e6, (long) 150), //2
			new RandBot("Kevin", 450633L, (long) 200),//3
			new RandBot("Itachi", (long) 10e6, 200),//4
			new RandBot("Yassin", (long) 4e6, 200),//5
			new RandBot("San", (long) 1e6, 50),//6
			new RandBot("Elhem", (long) 480e3, 100),//7
			new RandBot("Sof", (long) 100e3, 200),//8
			new RandBot("M", (long) 100e3, 200)//9
	));

	private boolean gameEndedCalled = false;
	private long moneyTotal = 0;
	private Map<Player, Long> playerCashBefore;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		for (Player player : PLAYERS)
			moneyTotal += player.getCash();
	}

	public void testStartNewHand() throws Exception {
		playerCashBefore = new HashMap<>();
		Random r = new Random();
		for (int i = 0; i < 500; i++) {
			int dealer = r.nextInt(PLAYERS.size());
			if (DEBUG) System.out.println(i);
			if (PLAYERS.size() == 1) {
				if (DEBUG) System.out.println(i);
				if (DEBUG) System.out.println("the tournament winner is " + PLAYERS.get(0));
				break;
			}

			Game game = new Game(this);
			gameEndedCalled = false;
			for (Player player : PLAYERS) {
				playerCashBefore.put(player, player.getCash());
			}
			game.startNewHand(2, PLAYERS, dealer);
			assertTrue(gameEndedCalled);
			for (Player player : PLAYERS) {
				assertTrue(player + " still in playing", player.getCash() != 0);
			}
		}
	}

	@Override
	public void gameEnded() {
		gameEndedCalled = true;
		if (DEBUG) System.out.print("gameEnded() called: ");
		long moneyTotal = 0;
		int winnersCount = 0;
		for (Player player : PLAYERS) {
			moneyTotal += player.getCash();
			long moneyDiff = player.getCash() - playerCashBefore.get(player);
			if (DEBUG) System.out.print(player.getName() + ": " + player.getCash() //+ "==>"
					+ (moneyDiff >= 0 ? " won " : " lost ") + moneyDiff + ";");
			if (moneyDiff >= 0) winnersCount++;
		}
		if (DEBUG) System.out.println();
		// if number of winner is 1, the amount should be the same
		// there's some cases where money is divided amount two player or more but there's ony one winner
		if (winnersCount == 1) {
			assertEquals(this.moneyTotal, moneyTotal); // XXX FAILED: money got increased 1
		} else {
			// winners > 1 - Possible Loss of Fraction
			// For example if 2 winner we might have a lost of 50c for one player and 50c to the other
			long lossBecauseOfFraction = this.moneyTotal - moneyTotal;
			if (DEBUG)
				System.out.println(winnersCount + " has won the pot, Loss of Fraction when dividing the pot value ="
						+ lossBecauseOfFraction);
			// consider the loss of the fraction part and do you your test
			// if this.moneyTotal and moneyTotal aren't equal because of possible loss, and not because of bug in Pot class or something else
			// check if the diff(loss) > 0 and loss < winnersCount
			assertTrue(lossBecauseOfFraction >= 0);
			assertTrue(lossBecauseOfFraction < winnersCount);
		}


	}

	@Override
	public void playerBusted(Set<Player> players) {
		if (DEBUG)
			System.out.println("playerBusted() called : " + Arrays.deepToString(players.toArray()));
		for (Player player : players) {
			assertEquals(player + "has enough cash to continue: ", 0, player.getCash());
			PLAYERS.remove(player);
		}
	}
}