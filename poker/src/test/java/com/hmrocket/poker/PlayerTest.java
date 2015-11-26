package com.hmrocket.poker;

import com.hmrocket.poker.card.Card;
import com.hmrocket.poker.card.Hand;
import com.hmrocket.poker.card.HandHoldem;
import com.hmrocket.poker.card.Rank;
import com.hmrocket.poker.card.Suit;

import junit.framework.TestCase;

import java.util.Random;

/**
 * @since 25/Nov/2015 - mhamed
 */
public class PlayerTest extends TestCase {

	public static final Player.PlayerState[] OFF_STATE = new Player.PlayerState[]{
			Player.PlayerState.Zzz, Player.PlayerState.INACTIVE, Player.PlayerState.FOLD
	};
	public static final Player.PlayerState[] ON_STATE = new Player.PlayerState[]{
			Player.PlayerState.ALL_IN, Player.PlayerState.ACTIVE, Player.PlayerState.CHECK, Player.PlayerState.CALL, Player.PlayerState.RAISE
	};
	public static final Player.PlayerState[] PLAYING_STATE = new Player.PlayerState[]{
			Player.PlayerState.ACTIVE, Player.PlayerState.CHECK, Player.PlayerState.CALL, Player.PlayerState.RAISE
	};
	HandHoldem handHoldem;
	private Player player;

	public void setUp() throws Exception {
		super.setUp();
		player = new PlayerImp("test", 1000, 100);
		handHoldem = new HandHoldem(new Hand(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.ACE, Suit.HEARTS)));
	}

	/**
	 * 1- test bot hand is null
	 * 2- test one hand is null the other is not
	 * 3- test equal hands
	 * 4- test state
	 *
	 * @throws Exception
	 */
	public void testCompareTo() throws Exception {
		Player player2 = new PlayerImp("test", 1000, 100);
		assertEquals("both hand is null", 0, player.compareTo(player2));
		player.setHandHoldem(handHoldem);
		assertEquals("test one hand is null the other is not", 1, player.compareTo(player2));
		assertEquals("test one hand is null the other is not", -1, player2.compareTo(player));
		HandHoldem handHoldem2 = new HandHoldem(new Hand(new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.ACE, Suit.SPADES)));
		player2.setHandHoldem(handHoldem2);
		assertEquals("test equal hands", 0, player.compareTo(player2));
		assertEquals("test equal hands", 0, player2.compareTo(player));

		//test state
		Random r = new Random();

		for (int i = 0; i < 10; i++) { // both P have the On state
			Player.PlayerState state1 = ON_STATE[r.nextInt(ON_STATE.length)];
			Player.PlayerState state2 = ON_STATE[r.nextInt(ON_STATE.length)];
			player.setState(state1);
			player2.setState(state2);
			assertEquals("test state: 1:" + player + " 2:" + player2, 0, player.compareTo(player2));
			assertEquals("test state: 1:" + player + " 2:" + player2, 0, player2.compareTo(player));
		}

		for (int i = 0; i < 10; i++) { // both P have the Off state
			Player.PlayerState state1 = OFF_STATE[r.nextInt(OFF_STATE.length)];
			Player.PlayerState state2 = OFF_STATE[r.nextInt(OFF_STATE.length)];
			player.setState(state1);
			player2.setState(state2);
			assertEquals("test state: 1:" + player + " 2:" + player2, 0, player.compareTo(player2));
			assertEquals("test state: 1:" + player + " 2:" + player2, 0, player2.compareTo(player));
		}

		for (int i = 0; i < 10; i++) {// one P have On state the other OFF
			Player.PlayerState state1 = OFF_STATE[r.nextInt(OFF_STATE.length)];
			Player.PlayerState state2 = ON_STATE[r.nextInt(ON_STATE.length)];
			player.setState(state1);
			player2.setState(state2);
			assertEquals("test state: 1:" + player + " 2:" + player2, -1, player.compareTo(player2));
			assertEquals("test state: 1:" + player + " 2:" + player2, 1, player2.compareTo(player));
		}

		// no need to go futher testing the Hand.compareTo() cause it's out of Player scope
	}

	/**
	 * testing all the state of out
	 * @throws Exception
	 */
	public void testIsOut() throws Exception {
		for (Player.PlayerState state : OFF_STATE) {
			player.setState(state);
			assertTrue(state.name(), player.isOut());
		}
	}

	public void testGetCash() throws Exception {
		// no testing value
	}

	public void testSetCash() throws Exception {
		// no testing value
	}

	public void testGetBet() throws Exception {
		// no testing value
	}

	public void testSetBet() throws Exception {
		// no testing value
	}

	public void testGetName() throws Exception {
		// no testing value
	}

	/**
	 * testing all the state of playing
	 *
	 * @throws Exception
	 */
	public void testIsPlaying() throws Exception {
		for (Player.PlayerState state : PLAYING_STATE) {
			player.setState(state);
			assertTrue(state.name(), player.isPlaying());
		}
	}

	/**
	 * test 5 situation when player:
	 * 1- check (didRaise false)
	 * 2- call (didRaise false)
	 * 3- allIn below or equal (didRaise false)
	 * 4- allIn above (didRaise true)
	 * 5- raise
	 *
	 * @throws Exception
	 */
	public void testDidRaise() throws Exception {
		long cash = player.getCash();
		player.check();
		assertFalse(player.didRaise(0));
		player.call(20);
		assertFalse(player.didRaise(20));
		//3
		player.allIn();
		assertFalse(player.didRaise(cash));
		assertFalse(player.didRaise(cash + 1 + new Random().nextInt()));
		//4
		assertTrue(player.didRaise(cash - 1));
		assertTrue(player.didRaise(cash - 1 - new Random().nextInt()));
		//5
		player.addCash(cash);
		player.raise(cash / 2);
		assertTrue(player.didRaise(cash / 4));

	}

	public void testGetState() throws Exception {
		// no test value
	}

	public void testSetState() throws Exception {
		// no test value
	}

	public void testGetSeat() throws Exception {
// no test value
	}

	public void testSetSeat() throws Exception {
// no test value
	}

	public void testGetHandHoldem() throws Exception {
// no test value
	}

	public void testSetHandHoldem() throws Exception {
// no test value
	}

	public void testFold() throws Exception {
		player.fold();
		assertTrue(Player.PlayerState.FOLD == player.getState());

	}

	/**
	 * - test add negative amount (shouldn't affect)
	 * - add positive amount make sure that was added
	 *
	 * @throws Exception
	 */
	public void testAddCash() throws Exception {
		long cash = player.getCash();
		player.addCash(-50);
		assertEquals(cash, player.getCash());
		player.addCash(+50);
		assertEquals(cash + 50, player.getCash());
	}

	/**
	 * test raise state, bet, and cash
	 *
	 * @throws Exception
	 */
	public void testRaise() throws Exception {
		long cash = player.getCash();
		long raise = cash / 2;
		player.raise(raise);
		assertTrue("player must be in raise state", Player.PlayerState.RAISE == player.getState());
		assertEquals("should be same raise", raise, player.getBet());
		assertEquals("player must lose same amount as the raise", cash, player.getCash() + player.getBet());
	}

	/**
	 * test check state, bet, and cash wasn't affected
	 *
	 * @throws Exception
	 */
	public void testCheck() throws Exception {
		long cash = player.getCash();
		player.check();
		assertTrue("player must be in check state", Player.PlayerState.CHECK == player.getState());
		assertEquals("bet should be 0 when check", 0, player.getBet());
		assertEquals("player must not lose money on check", cash, player.getCash());
	}

	/**
	 * test allin state, bet, and cash
	 *
	 * @throws Exception
	 */
	public void testAllIn() throws Exception {
		long cash = player.getCash();
		player.allIn();
		assertTrue("player must be in allin state", Player.PlayerState.ALL_IN == player.getState());
		assertEquals(cash, player.getBet());
		assertEquals("player must lose same amount as the raise", 0, player.getCash());

	}

	/**
	 * test call state, bet, and cash
	 *
	 * @throws Exception
	 */
	public void testCall() throws Exception {
		long cash = player.getCash();
		long called = cash / 2;
		player.call(called);
		assertTrue("player must be in call state", Player.PlayerState.CALL == player.getState());
		assertEquals(called, player.getBet());
		assertEquals(cash, player.getCash() + player.getBet());
	}

	public void testAutoMove() throws Exception {
		// very small test value
	}

	public void testPlay() throws Exception {
		// not possible to test
	}

	static class PlayerImp extends Player {

		public PlayerImp(String name, long bankBalance, long cash) {
			super(name, bankBalance, cash);
		}

		@Override
		public void play(Turn turn) {

		}
	}
}