package com.hmrocket.poker.ai.bot;

import com.hmrocket.poker.Turn;
import com.hmrocket.poker.ai.HandOdds;

/**
 * Created by hmrocket on 09/11/2015.
 */
public class SafeBotAi extends SafeBot {

	public SafeBotAi(String name, long bankBalance, long cash) {
		super(name, bankBalance, cash);
	}

	@Override
	void preflopStrategy(Turn turn) {
		// play pre flop the same way you play any Phase
		HandOdds handStrength = handOddsCalculator.getHandOdds(turn.getPokerRoundTurnsCount(), handHoldem);
		if (turn.isRaisedBefore() || turn.isRaisedAfter())
			makeMove(turn, handStrength.getHandStrength(), turn.getAmountToContinue() - bet);
		else makeMove(turn, handStrength);
	}
}
