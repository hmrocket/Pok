package com.hmrocket.poker;

import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.util.Arrays;
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


	private static final int PLAYERS_COUNT;
	private static final int SEATS_COUNT;
	private static final int DEALER;
	private static final long MIN_BET;
	private static final Random R;

	private static final List<Player> PLAYERS = Arrays.asList(new Player[]{
			new Player("Kais", (long) 72e6, (long) 2e6), //1
			new Player("Mhamed", (long) 13e6, (long) 2e6), //2
			new Player("Kevin", 450633L, (long) 1e6),//3
			new Player("Itachi", (long) 10e6, 182000L),//4
			new Player("Yassin", (long) 4e6, 100000L),//5
			new Player("San", (long) 1e6, 100000L),//6
			new Player("Elhem", (long) 480e3, 100000L),//7
			new Player("Sof", (long) 100e3, 100000L),//8
			new Player("M", (long) 100e3, 100000L)//9
	});

	private static final List<Integer> PLAYERS_FAVORITE_SEAT = Arrays.asList(new Integer[]{
			0, //PLAYERS[0]
			3, //PLAYERS[1]
			2,//PLAYERS[2]
			1,//PLAYERS[3]
			4,//PLAYERS[4]
			8,//PLAYERS[5]
			6,//PLAYERS[6]
			7,//PLAYERS[7]
			5//PLAYERS[8]
	});

	static {
		PLAYERS_COUNT = new Random().nextInt(10);
		SEATS_COUNT = 9;
		R = new Random();
		DEALER = R.nextInt(SEATS_COUNT);
		MIN_BET = R.nextInt(10000) * 2;
		System.out.println("PLAYERS_COUNT = " + PLAYERS_COUNT);
		System.out.println("SEATS_COUNT  = " + SEATS_COUNT);
//		System.out.println("DEALER = " + DEALER);
		System.out.println("MIN_BET = " + MIN_BET);
		System.out.println("MIN_BUY-IN = " + PokerTools.getMinBuyIn(MIN_BET));
	}


	@Override
	public void setUp() throws Exception {
		super.setUp();
		// Table will be always Nine Seats table
		table = new Table(SEATS_COUNT, MIN_BET);
		for (int i = 0; i < PLAYERS_COUNT; i++) {
			table.addPlayer(PLAYERS.get(i), PLAYERS_FAVORITE_SEAT.get(i));
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
		assertTrue("Table doesn't contains 9 seats", this.tableSeats.size() == SEATS_COUNT);
		assertTrue("Table doesn't have the right size, table current size = " + this.tablePlayers.size(), this.tablePlayers.size() == SEATS_COUNT);

	}

	public void testAddPlayer() throws Exception {
		long minBuyIn = PokerTools.getMinBuyIn(MIN_BET);
		for (int i = 0; i < SEATS_COUNT; i++) {
			Player player = PLAYERS.get(i);
			if (i < PLAYERS_COUNT) {
				if (minBuyIn > player.getCash()) {
					assertFalse(PLAYERS.get(i).getName() + " doesn't have enough buy-in to seat at the table", this.tablePlayers.contains(PLAYERS.get(i)));
					assertTrue("Seat " + PLAYERS.get(i).getName() + " wasn't inserted in the right seat", tableSeats.get(PLAYERS_FAVORITE_SEAT.get(i)).isAvailable());
				} else {
					assertTrue("table doesn't contain an added player called " + PLAYERS.get(i).getName(), this.tablePlayers.contains(PLAYERS.get(i)));
					assertTrue("Player " + PLAYERS.get(i).getName() + " wasn't inserted at seat:" + this.tablePlayers.indexOf(PLAYERS.get(i)), this.tablePlayers.indexOf(PLAYERS.get(i)) == PLAYERS_FAVORITE_SEAT.get(i));
				}
			} else {
				assertFalse("table contain a not added player called " + PLAYERS.get(i).getName(), this.tablePlayers.contains(PLAYERS.get(i)));
				assertTrue("Seat " + PLAYERS.get(i).getName() + " wasn't inserted in the right seat", tableSeats.get(PLAYERS_FAVORITE_SEAT.get(i)).isAvailable());
			}
		}

	}

	public void testRemovePlayer() throws Exception {
		// Remove a Random player

		Player player = PLAYERS.get(R.nextInt(PLAYERS_COUNT));
		table.removePlayer(player);

		getPlayersField();
		assertFalse(player.getName() + " still on the table at Seat:" + tablePlayers.indexOf(player), tablePlayers.contains(player));

	}

	public void testNextDealer() throws Exception {
		for (int i = 0; i < PLAYERS_COUNT; i++) {
			Player player = table.nextDealer();
			// dealer should be Player at Seat i
			Player dealer = PLAYERS.get(PLAYERS_FAVORITE_SEAT.indexOf((i + 1) % PLAYERS_COUNT));
			int playerIndex = PLAYERS.indexOf(player);
			String errorMessage = String.format("Current Dealer is %s index:%d should been %s index:%d",
					player.getName(), playerIndex, dealer.getName(), (i + 1) % PLAYERS_COUNT);
			assertTrue(errorMessage, dealer.equals(player));
		}
	}

}