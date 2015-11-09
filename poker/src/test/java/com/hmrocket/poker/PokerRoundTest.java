package com.hmrocket.poker;

import com.hmrocket.poker.ai.bot.RandBot;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hmrocket on 08/11/2015.
 */
public class PokerRoundTest extends TestCase {

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

	public void testStartGame() throws Exception {

	}

	public void testIsAllPlayersNotPlayingExceptOne() throws Exception {

	}

	public void testNextTurn() throws Exception {

	}

	public void testGetLeftPlayer() throws Exception {

	}

	public void testGetLeftPlayer1() throws Exception {

	}

	public void testIsCompleted() throws Exception {

	}

	public void testRemovePlayer() throws Exception {

	}

	public void testNewRound() throws Exception {

	}

	public void testPosition() throws Exception {
		int playerPosition;
		playerPosition = PokerRound.getPosition(PLAYERS, PLAYERS.get(1), 0);
		assertEquals(0, playerPosition);

		playerPosition = PokerRound.getPosition(PLAYERS, PLAYERS.get(2), 0);
		assertEquals(1, playerPosition);

		playerPosition = PokerRound.getPosition(PLAYERS, PLAYERS.get(8), 7);
		assertEquals(0, playerPosition);

		playerPosition = PokerRound.getPosition(PLAYERS, PLAYERS.get(0), 8);
		assertEquals(0, playerPosition);

		playerPosition = PokerRound.getPosition(PLAYERS, PLAYERS.get(0), 6);
		assertEquals(2, playerPosition);

		playerPosition = PokerRound.getPosition(PLAYERS, PLAYERS.get(6), 6);
		assertEquals(8, playerPosition);

	}
}