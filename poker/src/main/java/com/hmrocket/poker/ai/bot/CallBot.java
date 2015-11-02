package com.hmrocket.poker.ai.bot;

import com.hmrocket.poker.Player;

/**
 * Created by hmrocket on 26/10/2015.
 */
public class CallBot extends Player {

	public CallBot(String name, long bankBalance, long cash) {
		super(name, bankBalance, cash);
	}

	@Override
	public void play(long amountToContinue) {
		call(amountToContinue);
	}
}
