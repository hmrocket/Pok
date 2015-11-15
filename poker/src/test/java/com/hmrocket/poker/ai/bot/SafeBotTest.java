package com.hmrocket.poker.ai.bot;

import com.hmrocket.poker.Player;
import com.hmrocket.poker.ai.BotArenaUtil;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hmrocket on 08/11/2015.
 */
public class SafeBotTest extends TestCase {

	private static final List<Player> PLAYERS = new ArrayList(Arrays.asList(
			new RaiseBot("Kais", (long) 72e6, (long) 100), //1
			new SafeBot("Mhamed", (long) 13e6, (long) 150), //2
			new RandBot("Kevin", 450633L, (long) 200),//3
			new CallBot("Yassin", (long) 4e6, 200) //4
	));

	public void testSafeBot() throws Exception {
		// Mhamed must beat all the other players
		// 50 tournament
		// each with 500 hand limit
		// for every tournament buy in is $1000
		// regardless of past performance. It must play 50 tournaments against the other bots.
		// Therefore the bot is putting up $50k in total in the tournaments and needs to see a
		// return of $100k
		long[] result = BotArenaUtil.play(50, 500, PLAYERS, 1000, 20);
		System.out.println(result);
		// Mhamed must double his money through the tournament (100k$)
		assertTrue("Mhamed spent 50k$ and won : " + result[1], result[1] > 50000);
	}
}