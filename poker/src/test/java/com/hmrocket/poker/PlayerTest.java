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

	static class PlayerImp extends Player {

		public PlayerImp(String name, long bankBalance, long cash) {
			super(name, bankBalance, cash);
		}

		@Override
		public void play(Turn turn) {

		}
	}

	public static final Player.PlayerState[] OFF_STATE = new Player.PlayerState[]{
			Player.PlayerState.Zzz, Player.PlayerState.INACTIVE, Player.PlayerState.FOLD
	};
	public static final Player.PlayerState[] ON_STATE = new Player.PlayerState[]{
			Player.PlayerState.ALL_IN, Player.PlayerState.ACTIVE, Player.PlayerState.CHECK, Player.PlayerState.CALL, Player.PlayerState.RAISE
	};

	private Player player;
	HandHoldem handHoldem;

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
		HandHoldem handHoldem2 = new HandHoldem(new Hand(new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.ACE, Suit.SPADES)));
		player2.setHandHoldem(handHoldem2);
		assertEquals("test equal hands", 0, player.compareTo(player2));

		//test state
		Random r = new Random();

		for (int i = 0; i < 10; i++) {
			Player.PlayerState state1 = ON_STATE[r.nextInt(ON_STATE.length)];
			Player.PlayerState state2 = ON_STATE[r.nextInt(ON_STATE.length)];
			assertEquals("test state: 1:"+ player + " 2:" + player2, 0, player.compareTo(player2));
		}

		for (int i = 0; i < 10; i++) {
			Player.PlayerState state1 = OFF_STATE[r.nextInt(OFF_STATE.length)];
			Player.PlayerState state2 = OFF_STATE[r.nextInt(OFF_STATE.length)];
			assertEquals("test state: 1:"+ player + " 2:" + player2, 0, player.compareTo(player2));
		}
		player2.fold();
		assertEquals("test state", 1, player.compareTo(player2));
		player2.check();
		assertEquals("test state", 0, player.compareTo(player2));
		player2.call(1);
		assertEquals("test state", 0, player.compareTo(player2));
	}

	public void testIsOut() throws Exception {

	}

	public void testGetCash() throws Exception {

	}

	public void testSetCash() throws Exception {

	}

	public void testGetBet() throws Exception {

	}

	public void testSetBet() throws Exception {

	}

	public void testGetName() throws Exception {

	}

	public void testIsPlaying() throws Exception {

	}

	public void testDidRaise() throws Exception {

	}

	public void testGetState() throws Exception {

	}

	public void testSetState() throws Exception {

	}

	public void testGetSeat() throws Exception {

	}

	public void testSetSeat() throws Exception {

	}

	public void testGetHandHoldem() throws Exception {

	}

	public void testSetHandHoldem() throws Exception {

	}

	public void testFold() throws Exception {

	}

	public void testAddCash() throws Exception {

	}

	public void testRaise() throws Exception {

	}

	public void testCheck() throws Exception {

	}

	public void testAllIn() throws Exception {

	}

	public void testCall() throws Exception {

	}

	public void testAutoMove() throws Exception {

	}

	public void testPlay() throws Exception {

	}
}