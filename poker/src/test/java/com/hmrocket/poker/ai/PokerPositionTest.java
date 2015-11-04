package com.hmrocket.poker.ai;

import junit.framework.TestCase;

/**
 * Created by hmrocket on 02/11/2015.
 */
public class PokerPositionTest extends TestCase {

	public void testGetPosition() throws Exception {
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(2, 0));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(2, 1));
		// 3 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(3, 0));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(3, 1));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(3, 2));
		// 4 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(4, 0));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(4, 1));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(4, 2));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(4, 3));
		// 5 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(5, 0));
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(5, 1));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(5, 3));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(4, 2));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(5, 4));
		// 6 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(6, 0));
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(6, 1));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(6, 3));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(6, 2));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(6, 4));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(6, 5));
		// 7 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(7, 0));
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(7, 1));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(7, 3));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(7, 2));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(7, 4));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(7, 5));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(7, 6));
		// 8 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(8, 0));
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(8, 1));
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(8, 2));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(8, 3));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(8, 4));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(8, 5));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(8, 6));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(8, 7));
		// 9 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(9, 0));
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(9, 1));
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(9, 2));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(9, 3));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(9, 4));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(9, 5));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(9, 6));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(9, 7));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(9, 8));
		// 10 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(10, 0));
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(10, 1));
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(10, 2));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(10, 3));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(10, 4));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(10, 5));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPosition(10, 6));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(10, 9));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(10, 7));
		assertEquals(PokerPosition.LATE, PokerPosition.getPosition(10, 8));
	}
}