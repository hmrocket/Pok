package com.hmrocket.poker;

import com.hmrocket.poker.ai.bot.RandBot;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hmrocket on 30/10/2015.
 */
public class RoundTest extends TestCase {

	private static final List<Player> PLAYERS = Arrays.asList(new Player[]{new RandBot("Kais", (long) 72e6, (long) 200), //1
			new RandBot("Mhamed", (long) 13e6, (long) 100), //2
			new RandBot("Kevin", 450633L, (long) 100),//3
			new RandBot("Itachi", (long) 10e6, 200),//4
			new RandBot("Yassin", (long) 4e6, 300),//5
			new RandBot("San", (long) 1e6, 50),//6
			new RandBot("Elhem", (long) 480e3, 100),//7
			new RandBot("Sof", (long) 100e3, 100),//8
			new RandBot("M", (long) 100e3, 50)//9
	});

	private Round round;

	public void setUp() throws Exception {
		super.setUp();
		round = new Round(PLAYERS, 0);
	}

	// playersList: special cases Round with 2 players - no players - 1 player
	// player: special cases null, not in the list, player on the edge of the list
	// skippedTurn: special case more than playersList size
	public void testGetLeftPlayer() throws Exception {
		assertTrue(true);
		assertNull(Round.getLeftPlayer(null, null, 0));
		List<Player> playerList = new ArrayList<>();
		// empty list
		assertNull(Round.getLeftPlayer(playerList, PLAYERS.get(0), 0));
		// one player round (make no sens but we will tested)
		playerList.add(PLAYERS.get(0));
		assertEquals(PLAYERS.get(0), Round.getLeftPlayer(playerList, PLAYERS.get(0), 0));
		assertEquals(PLAYERS.get(0), Round.getLeftPlayer(playerList, PLAYERS.get(0), 5));
		// two players in the  game + player on the edge of
		playerList.add(PLAYERS.get(1));
		assertEquals(PLAYERS.get(1), Round.getLeftPlayer(playerList, PLAYERS.get(0), 0));
		assertEquals(PLAYERS.get(0), Round.getLeftPlayer(playerList, PLAYERS.get(1), 0));
		// player doesn't exist in the list
		assertEquals(null, Round.getLeftPlayer(playerList, null, 0));
		assertEquals(null, Round.getLeftPlayer(playerList, PLAYERS.get(2), 0));
		// skipped turn more than
		assertEquals(PLAYERS.get(1), Round.getLeftPlayer(playerList, PLAYERS.get(0), 6));
		assertEquals(PLAYERS.get(1), Round.getLeftPlayer(playerList, PLAYERS.get(1), 3));
		assertEquals(PLAYERS.get(1), Round.getLeftPlayer(PLAYERS, PLAYERS.get(0), PLAYERS.size()));

		// normal case
		assertEquals(PLAYERS.get(2), Round.getLeftPlayer(PLAYERS, PLAYERS.get(0), 1));

	}

	public void testNextTurn() throws Exception {
		// check if first player turn
		assertEquals(round.playerTurn, 0);
		assertEquals(round.players.get(0), PLAYERS.get(0));
		// check next turns
		for (int i = 1; i < PLAYERS.size(); i++) {
			assertEquals(round.nextTurn(), PLAYERS.get(i));
			assertEquals(round.playerTurn, i);
		}
		assertNull("Should be no more player, Round is finshed and nextTurn should return null",
				round.nextTurn());

		// new round from differen start
		round.newRound(PLAYERS.get(5));
		for (int i = 6; i < PLAYERS.size(); i++) {
			assertEquals(round.nextTurn(), PLAYERS.get(i));
			assertEquals(round.playerTurn, i);
		}

		for (int i = 0; i < 5; i++) {
			assertEquals(round.nextTurn(), PLAYERS.get(i));
			assertEquals(round.playerTurn, i);
		}
	}

	public void testIsCompleted() throws Exception {

	}

	public void testReverseRoundSens() throws Exception {

	}

	public void testRemovePlayer() throws Exception {

	}

	public void testAddPlayer() throws Exception {

	}

	public void testTerminateRound() throws Exception {

	}

	public void testNewRound() throws Exception {

	}

	public void testNewRound1() throws Exception {

	}
}