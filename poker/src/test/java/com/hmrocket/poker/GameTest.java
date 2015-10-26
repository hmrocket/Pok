package com.hmrocket.poker;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Mhamed on 15-10-26.
 */
public class GameTest extends TestCase implements Game.GameEvent {

	private static final List<Player> PLAYERS = Arrays.asList(new Player("Kais", (long) 72e6, (long) 200), //1
			new Player("Mhamed", (long) 13e6, (long) 100), //2
			new Player("Kevin", 450633L, (long) 100),//3
			new Player("Itachi", (long) 10e6, 200),//4
			new Player("Yassin", (long) 4e6, 300),//5
			new Player("San", (long) 1e6, 50),//6
			new Player("Elhem", (long) 480e3, 100),//7
			new Player("Sof", (long) 100e3, 100),//8
			new Player("M", (long) 100e3, 50)//9
	);

	private boolean gameEndedCalled = false;

	public void testStartNewHand() throws Exception {
		Game game = new Game(this);
		gameEndedCalled = false;
		game.startNewHand(2, PLAYERS, 0);
		assertTrue(gameEndedCalled);
	}

	@Override
	public void gameEnded() {
		gameEndedCalled = true;
		System.out.println("gameEnded() called");
		long moneyTotal = 0;
		for (Player player : PLAYERS)
			moneyTotal += player.getCash();

		assertEquals(1200, moneyTotal);

	}

	@Override
	public void playerBusted(Set<Player> players) {
		System.out.println("playerBusted() called");
		for (Player player : players)
			assertEquals(0, player.getCash());
	}
}