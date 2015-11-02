package com.hmrocket.poker;

import com.hmrocket.poker.ai.bot.RandBot;

import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by mhamed on 15-10-19.
 */
public class TableTest extends TestCase {

	private static final int PLAYERS_COUNT;
	private static final int SEATS_COUNT;
	private static final int DEALER;
	private static final long MIN_BET;
	private static final Random R;
	private static final List<Player> PLAYERS = Arrays.asList(new Player[]{
			new RandBot("Kais", (long) 72e6, (long) 2e6), //1
			new RandBot("Mhamed", (long) 13e6, (long) 2e6), //2
			new RandBot("Kevin", 450633L, (long) 1e6),//3
			new RandBot("Itachi", (long) 10e6, 182000L),//4
			new RandBot("Yassin", (long) 4e6, 100000L),//5
			new RandBot("San", (long) 1e6, 100000L),//6
			new RandBot("Elhem", (long) 480e3, 100000L),//7
			new RandBot("Sof", (long) 100e3, 100000L),//8
			new RandBot("M", (long) 100e3, 100000L) //9
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
		SEATS_COUNT = 9;
		PLAYERS_COUNT = new Random().nextInt(SEATS_COUNT + 1);
		R = new Random();
		DEALER = R.nextInt(SEATS_COUNT);
		MIN_BET = R.nextInt(10000) * 2;
		System.out.println("PLAYERS_COUNT = " + PLAYERS_COUNT);
		System.out.println("SEATS_COUNT  = " + SEATS_COUNT);
//		System.out.println("DEALER = " + DEALER);
		System.out.println("MIN_BET = " + MIN_BET);
		System.out.println("MIN_BUY-IN = " + PokerTools.getMinBuyIn(MIN_BET));
	}

	Table table;
	private List<Player> tablePlayers;
	private int tableSeatsAvailable;
	private List<Seat> tableSeats;
	private int tableDealer;
	private Game tableGame;
	private long tableMinBet;

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
		int seatCount = 9;
		long buyIn = 100;
		long passBuyIn = PokerTools.getMinBuyIn(buyIn);
		table = new Table(seatCount, buyIn);
		Player player;
		// test player with no cash
		player = new RandBot("no money", 1000, 0);
		table.addPlayer(player, 0);
		assertFalse(table.getPlayers().contains(player));
		// test player with not enough buy in
		player = new RandBot("no buy-in", 1000, R.nextInt((int) passBuyIn));
		table.addPlayer(player, seatCount - 1);
		assertFalse(table.getPlayers().contains(player));
		// test player with just enough buy in
		player = new RandBot("=buy-in", 1000, passBuyIn);
		table.addPlayer(player, 0);
		assertTrue(getPlayersField().contains(player));
		// test player with more than enough to buy in
		player = new RandBot("super buy-in", 1000, Math.abs(R.nextInt()) + passBuyIn);
		table.addPlayer(player, 1);
		assertTrue(getPlayersField().contains(player));
		// test player null player
		table.addPlayer(null, R.nextInt(seatCount));
		assertFalse(table.getPlayers().contains(null));
		// test seatId doesn't exist
		IllegalArgumentException e1, e2;
		e1 = e2 = null;
		try {
			table.addPlayer(player, R.nextInt(seatCount) + seatCount);
		} catch (IllegalArgumentException e) {
			e1 = e;
		}
		assertNotNull(e1);
		try {
			table.addPlayer(player, R.nextInt(seatCount) - seatCount);
		} catch (IllegalArgumentException e) {
			e2 = e;
		}
		assertNotNull(e2);
		//test already added player
		int seatsAvailableBefore = getSeatsAvailable();
		table.addPlayer(player, 3);
		assertTrue(seatsAvailableBefore == getSeatsAvailable());
		// test occupied seat
		player = new RandBot(" ", 1000, Math.abs(R.nextInt()) + passBuyIn);
		table.addPlayer(player, 0);
		assertFalse(getPlayersField().contains(player));

	}

	public void testRemovePlayer() throws Exception {
		// Remove a Random player
		int seatCount = 9;
		long buyIn = 100;
		long passBuyIn = PokerTools.getMinBuyIn(buyIn);
		table = new Table(seatCount, passBuyIn);
		Player player = new RandBot("removePlayer", passBuyIn, passBuyIn);
		table.addPlayer(player, R.nextInt(seatCount));
		table.removePlayer(player);
		getPlayersField();
		assertFalse(player.getName() + " still on the table at Seat:" + tablePlayers.indexOf(player), tablePlayers.contains(player));
		//Remove a player doesn't exist
		int count = table.getPlayers().size();
		table.removePlayer(new RandBot("NOBODY", passBuyIn, passBuyIn));
		assertTrue(count == table.getPlayers().size());

	}

	public void testGetPlayerInTheGame() throws Exception {
		List<Player> playersInTheGame = table.getPlayers();
		long minBuyIn = PokerTools.getMinBuyIn(MIN_BET);

		//asset that player in the game is equal or less PLAYERS_COUNT (not all players are added because of the buy-In)
		assertTrue(playersInTheGame.size() <= PLAYERS_COUNT);

		//assert all player added exist in the list
		int playerCouldntBuyIn = 0;
		for (int i = 0; i < PLAYERS_COUNT; i++) {
			Player player = PLAYERS.get(i);
			if (minBuyIn > player.getCash()) { //Players doesn't have enough buy-in
				playerCouldntBuyIn++;
				assertFalse(PLAYERS.get(i).getName() + " doesn't have enough buy-in to seat at the table", this.tablePlayers.contains(PLAYERS.get(i)));
				assertTrue("Seat " + PLAYERS.get(i).getName() + " wasn't inserted in the right seat", tableSeats.get(PLAYERS_FAVORITE_SEAT.get(i)).isAvailable());
			} else {
				assertTrue("table doesn't contain an added player called " + PLAYERS.get(i).getName(), this.tablePlayers.contains(PLAYERS.get(i)));
				// Check the seat id
				assertTrue("Player " + PLAYERS.get(i).getName() + " wasn't inserted at seat:" + this.tablePlayers.indexOf(PLAYERS.get(i)), this.tablePlayers.indexOf(PLAYERS.get(i)) == PLAYERS_FAVORITE_SEAT.get(i));
			}
		}
		String msg = String.format("Player on table: %d, Player couldn't buy-in: %d", playersInTheGame.size(), playerCouldntBuyIn);
		assertTrue(msg, playersInTheGame.size() == PLAYERS_COUNT - playerCouldntBuyIn);

	}

	public void testNextDealer() throws Exception {
		List<Player> playersInTheGame = table.getPlayers();
		for (int i = 0; i < playersInTheGame.size(); i++) {
			Player player = table.nextDealer();
			// dealer should be Player at Seat i
			Player dealer = playersInTheGame.get((i + 1) % playersInTheGame.size());
			String errorMessage = String.format("Current Dealer is %s index:%d should been %s index:%d",
					player.getName(), i, dealer.getName(), (i + 1) % playersInTheGame.size());
			assertTrue(errorMessage, dealer.equals(player));
		}
	}

}