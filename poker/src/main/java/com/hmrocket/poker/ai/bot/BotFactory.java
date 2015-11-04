package com.hmrocket.poker.ai.bot;

import com.hmrocket.poker.Player;

import java.util.Random;

/**
 * Created by hmrocket on 03/11/2015.
 */
public final class BotFactory {

	public static Player generateBot(String name, int playerLevel) {
		Random random = new Random();
		long generatedCash = 100 * (random.nextInt(playerLevel) + 1);
		long generatedBankBalance = generatedCash * random.nextInt(playerLevel);

		if ("Samuel L. Jackson".equalsIgnoreCase(name)) {
			// Surprise motherfucker
			// Raise me one more god dam time motherf***er, I dare you! I DOUBLE DARE YOU!
			return new RandBot("Samuel L. Jackson", generatedBankBalance, generatedCash);
		} else {
			return new RandBot("Samuel L. Jackson", generatedBankBalance, generatedCash);
		}
	}
}
