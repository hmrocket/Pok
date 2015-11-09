package com.hmrocket.poker.ai.bot;

import com.hmrocket.poker.Player;
import com.hmrocket.poker.Turn;

/**
 * Created by hmrocket on 01/11/2015.
 */
public class FoldBot extends Player {


	public FoldBot(String name, long bankBalance, long cash) {
		super(name, bankBalance, cash);
	}

	@Override
	public void play(Turn turn) {
		fold();
	}
}
