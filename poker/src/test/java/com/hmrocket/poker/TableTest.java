package com.hmrocket.poker;

import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

/**
 * Created by mhamed on 15-10-19.
 */
public class TableTest extends TestCase {

	Table table;
	private List<Player> tablePlayers;
	private int tableSeatsAvailable;
	private List<Seat> tableSeats;
	private int tableDealer;
	private Game tableGame;
	private long tableMinBet;


	private static final int PLAYERS_NUMBER = new Random().nextInt(10);
	private static final int DEALER = new Random().nextInt(10);
	private static final long MIN_BET = new Random().nextInt(10000) * 2;

	private static final Player PLAYERS[] = new Player[]{
			new Player("Kais", (long) 72e6, (long) 2e6), //1
			new Player("Mhamed", (long) 13e6, (long) 2e6), //2
			new Player("Kevin", 450633L, (long) 1e6),//3
			new Player("Itachi", (long) 10e6, 182000L),//4
			new Player("Yassin", (long) 4e6, 100000L),//5
			new Player("San", (long) 1e6, 100000L),//6
			new Player("Elhem", (long) 480e3, 100000L),//7
			new Player("Sof", (long) 100e3, 100000L),//8
			new Player("M", (long) 100e3, 100000L)//9
	};

	private static final int PLAYERS_FAVORITE_SEAT[] = new int[]{
			1, //PLAYERS[0]
			3, //PLAYERS[1]
			8,//PLAYERS[2]
			4,//PLAYERS[3]
			5,//PLAYERS[4]
			6,//PLAYERS[5]
			7,//PLAYERS[6]
			9,//PLAYERS[7]
			2//PLAYERS[8]
	};

	static {
		System.out.println("PLAYERS_NUMBER = " + PLAYERS_NUMBER);
		System.out.println("DEALER = " + DEALER);
		System.out.println("MIN_BET = " + MIN_BET);
	}


	@Override
	public void setUp() throws Exception {
		super.setUp();

		table = new Table(PLAYERS_NUMBER, MIN_BET);
		for (int i = 0; i < PLAYERS_NUMBER; i++) {
			table.addPlayer(PLAYERS[i], PLAYERS_FAVORITE_SEAT[i]);
		}

		// SetUp private variables
		getPlayersField();
		getSeatsField();
		getSeatsAvailable();
		getDealer();
		getGame();
		getMinBet();

	}

	private List<Player> getPlayersField() throws NoSuchFieldException, IllegalAccessException {
		Field field = Table.class.getDeclaredField("players");
		field.setAccessible(true);
		tablePlayers = (List<Player>) field.get(table);
		return tablePlayers;
	}

	private List<Seat> getSeatsField() throws NoSuchFieldException, IllegalAccessException {
		Field field = Table.class.getDeclaredField("seats");
		field.setAccessible(true);
		tableSeats = (List<Seat>) field.get(table);
		return tableSeats;
	}

	private int getSeatsAvailable() throws NoSuchFieldException, IllegalAccessException {
		Field field = Table.class.getDeclaredField("seatsAvailable");
		field.setAccessible(true);
		tableSeatsAvailable = ((Integer) field.get(table)).intValue();
		return tableSeatsAvailable;
	}

	private int getDealer() throws NoSuchFieldException, IllegalAccessException {
		Field field = Table.class.getDeclaredField("dealer");
		field.setAccessible(true);
		tableDealer = ((Integer) field.get(table)).intValue();
		return tableDealer;
	}

	private Game getGame() throws NoSuchFieldException, IllegalAccessException {
		Field field = Table.class.getDeclaredField("game");
		field.setAccessible(true);
		tableGame = ((Game) field.get(table));
		return tableGame;
	}

	private long getMinBet() throws NoSuchFieldException, IllegalAccessException {
		Field field = Table.class.getDeclaredField("minBet");
		field.setAccessible(true);
		tableMinBet = ((Long) field.get(table)).longValue();
		return tableMinBet;
	}


	public void testTable() throws Exception {


	}
	
	public void testAddPlayer() throws Exception {

		assertNotNull(this.tablePlayers);
		assertFalse("table shouldn't be empty", this.tablePlayers.isEmpty());
		assertTrue("table doesn't have the right size, table curent size = " + this.tablePlayers.size(), this.tablePlayers.size() == 9);
		int i = 0;
		for (Player player : PLAYERS) {
			if (i < PLAYERS_NUMBER) {
				assertTrue("table doesn't contain an added player called " + player.getName(), this.tablePlayers.contains(player));
				assertTrue("Player " + player.getName() + " wasn't inserted in the right seat", this.tablePlayers.indexOf(player) == PLAYERS_FAVORITE_SEAT[i]);
			} else {
				assertFalse("table contain a not added player called " + player.getName(), this.tablePlayers.contains(player));
				assertTrue("Seat " + player.getName() + " wasn't inserted in the right seat", tableSeats.get(PLAYERS_FAVORITE_SEAT[i]).isAvailable());
			}
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