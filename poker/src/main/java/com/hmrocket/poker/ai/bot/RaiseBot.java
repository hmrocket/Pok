package com.hmrocket.poker.ai.bot;

import com.hmrocket.poker.Player;

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
	public void play(long amountToContinue) {
		long maxAddBet = (cash + bet) - amountToContinue;
		// bot raise whenever possible, fold otherwise
		if (maxAddBet <= 0) {
			fold();
		} else {
			long betAdd = random.nextInt((int) maxAddBet) + 1;
			raise(betAdd + amountToContinue);
		}
	}
}
