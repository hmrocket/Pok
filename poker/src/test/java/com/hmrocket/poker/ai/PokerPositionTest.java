package com.hmrocket.poker.ai;

import junit.framework.TestCase;

/**
 * Created by hmrocket on 02/11/2015.
 */
public class PokerPositionTest extends TestCase {

	public void testGetPosition() throws Exception {
		// 1 Player
		assertEquals(PokerPosition.EARLY, PokerPosition.getPosition(1, 0));

		// 2 players
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

	public void testGetPositionOnPreFlop() throws Exception {
		// we did introduce Blind position just available and effective on PreFlop only

		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPositionOnPreFlop(2, 0));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(2, 1));
		// 3 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPositionOnPreFlop(3, 0));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(3, 1));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(3, 2));
		// 4 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPositionOnPreFlop(4, 0));
		assertEquals(PokerPosition.LATE, PokerPosition.getPositionOnPreFlop(4, 1));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(4, 2));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(4, 3));
		// 5 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPositionOnPreFlop(5, 0));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPositionOnPreFlop(5, 1));
		assertEquals(PokerPosition.LATE, PokerPosition.getPositionOnPreFlop(5, 2));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(4, 3));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(5, 4));
		// 6 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPositionOnPreFlop(6, 0));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPositionOnPreFlop(6, 1));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPositionOnPreFlop(6, 2));
		assertEquals(PokerPosition.LATE, PokerPosition.getPositionOnPreFlop(6, 3));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(6, 4));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(6, 5));
		// 7 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPositionOnPreFlop(7, 0));
		assertEquals(PokerPosition.EARLY, PokerPosition.getPositionOnPreFlop(7, 1));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPositionOnPreFlop(7, 2));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPositionOnPreFlop(7, 3));
		assertEquals(PokerPosition.LATE, PokerPosition.getPositionOnPreFlop(7, 4));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(7, 5));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(7, 6));
		// 8 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPositionOnPreFlop(8, 0));
		assertEquals(PokerPosition.EARLY, PokerPosition.getPositionOnPreFlop(8, 1));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPositionOnPreFlop(8, 2));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPositionOnPreFlop(8, 3));
		assertEquals(PokerPosition.LATE, PokerPosition.getPositionOnPreFlop(8, 4));
		assertEquals(PokerPosition.LATE, PokerPosition.getPositionOnPreFlop(8, 5));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(8, 6));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(8, 7));
		// 9 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPositionOnPreFlop(9, 0));
		assertEquals(PokerPosition.EARLY, PokerPosition.getPositionOnPreFlop(9, 1));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPositionOnPreFlop(9, 2));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPositionOnPreFlop(9, 3));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPositionOnPreFlop(9, 4));
		assertEquals(PokerPosition.LATE, PokerPosition.getPositionOnPreFlop(9, 5));
		assertEquals(PokerPosition.LATE, PokerPosition.getPositionOnPreFlop(9, 6));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(9, 7));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(9, 8));
		// 10 players
		assertEquals(PokerPosition.EARLY, PokerPosition.getPositionOnPreFlop(10, 0));
		assertEquals(PokerPosition.EARLY, PokerPosition.getPositionOnPreFlop(10, 1));
		assertEquals(PokerPosition.EARLY, PokerPosition.getPositionOnPreFlop(10, 2));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPositionOnPreFlop(10, 3));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPositionOnPreFlop(10, 4));
		assertEquals(PokerPosition.MID_POSITION, PokerPosition.getPositionOnPreFlop(10, 5));
		assertEquals(PokerPosition.LATE, PokerPosition.getPositionOnPreFlop(10, 6));
		assertEquals(PokerPosition.LATE, PokerPosition.getPositionOnPreFlop(10, 7));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(10, 8));
		assertEquals(PokerPosition.BLINDS, PokerPosition.getPositionOnPreFlop(10, 9));

	}
}