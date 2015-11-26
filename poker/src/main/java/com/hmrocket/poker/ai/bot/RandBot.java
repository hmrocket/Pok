package com.hmrocket.poker.ai.bot;

import com.hmrocket.poker.Player;
import com.hmrocket.poker.Turn;

import java.util.Random;

/**
 * Created by hmrocket on 26/10/2015.
 */
public class RandBot extends Player {

	public RandBot(String name, long bankBalance, long cash) {
		super(name, bankBalance, cash);
	}

	@Override
	public void play(Turn turn) {
		long amountToContinue = turn.getAmountToContinue();
		Random r = new Random();
		int action = r.nextInt(amountToContinue == 0 ? 4 : 3);
		switch (action) {
			case 0:
				fold();
				break;
			case 1: // raise
				long raiseAmount = r.nextInt((int) (cash + bet));
				if (amountToContinue > raiseAmount) fold();
				else if (amountToContinue == raiseAmount)
					call(amountToContinue);
				else {
					raise(Math.max(raiseAmount, turn.getMinRaise()));
					amountToContinue = raiseAmount;
				}
				break;
			case 2: // call
				long callAmount = r.nextInt((int) cash);
				if (amountToContinue > callAmount) fold();
				else call(amountToContinue);
				break;
			case 3: //Check
				check();
				break;
		}
	}
}
