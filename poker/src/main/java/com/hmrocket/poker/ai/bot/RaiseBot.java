package com.hmrocket.poker.ai.bot;

import com.hmrocket.poker.Player;
import com.hmrocket.poker.Turn;

import java.util.Random;

/**
 * Created by hmrocket on 26/10/2015.
 */
public class RaiseBot extends Player {

	private Random random = new Random();

	public RaiseBot(String name, long bankBalance, long cash) {
		super(name, bankBalance, cash);
	}

	@Override
	public void play(Turn turn) {
		long maxAddBet = (cash + bet) - turn.getAmountToContinue();
		// bot raise whenever possible, fold otherwise
		if (maxAddBet <= 0) {
			fold();
		} else {
			long betAdd = random.nextInt((int) maxAddBet) + 1;
			raise(betAdd + turn.getAmountToContinue());
		}
	}
}
