/**
 * 
 */
package com.hmrocket.poker;

/**
 * @author mhamed
 * @since Oct 8, 2015
 */
public enum RoundPhase {
	PRE_FLOP,
	FLOP,
	TURN,
	RIVER;

	public static int getCount() {
		return 4; // 4 rounds in poker
	}
}
