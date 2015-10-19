package com.hmrocket.poker;

import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by mhamed on 15-10-19.
 */
public class TableTest extends TestCase {

	Table table;
	private List<Player> players;
	private List<Player> playersTest;
	private int seatsAvailable;
	private List<Seat> seats;
	private int dealer;
	private Game game;
	private long minBet;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		table = new Table(9, 1000L);
		Player kais = new Player("Kais", (long) 2e6, (long) 2e6);
		Player mhamed = new Player("Mhamed", (long) 1e7, (long) 2e6);
		Player kevin = new Player("Kevin", 450633L, (long) 1e6);
		playersTest.add(kais);playersTest.add(mhamed);playersTest.add(kevin);
		table.addPlayer(kais, 0);
		table.addPlayer(mhamed, 3);
		table.addPlayer(kevin, 6);

		Field field = Table.class.getDeclaredField("players");
		field.setAccessible(true);
		players = (List<Player>) field.get(table);

	}

	public void testAddPlayer() throws Exception {
		assertNotNull(players);
		assertFalse("table shouldn't be empty", players.isEmpty());
		assertTrue("table doesn't have the right size, table curent size = " + players.size(), players.size() == 9);
		for (Player player : playersTest) {
			assertTrue("table dosen't contain an added player called " + player.getName() ,players.contains(player));
		}

	}

	public void testRemovePlayer() throws Exception {

	}

	public void testStartGame() throws Exception {
//		table.startGame();
//		// plays next hand
//		Player itachi = new Player("Itachi", (long) 10e7, 182000L);
//		table.addPlayer(itachi, 8);
//		Player yassin = new Player("Yassin", (long) 4e6, 100000L);
//		table.addPlayer(yassin, 2);
//		table.removePlayer(itachi);
//		table.addPlayer(itachi, 1);

	}
}