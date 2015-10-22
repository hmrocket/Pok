package com.hmrocket.poker.pot;

import com.hmrocket.poker.Player;
import com.hmrocket.poker.card.Card;
import com.hmrocket.poker.card.HandHoldem;
import com.hmrocket.poker.card.Rank;
import com.hmrocket.poker.card.Suit;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by hmrocket on 21/10/2015.
 */
public class PotTest extends TestCase {

    private Pot pot;


    private List<Player> PLAYERS;


    public void setUp() throws Exception {
        super.setUp();
        pot = new Pot();
        PLAYERS = Arrays.asList(new Player[]{
                new Player("Kais", (long) 72e6, (long) 2e6), //1
                new Player("Mhamed", (long) 13e6, (long) 2e6), //2
                new Player("Kevin", 450633L, (long) 1e6),//3
                new Player("Itachi", (long) 10e6, 182000L),//4
                new Player("Yassin", (long) 4e6, 100000L),//5
                new Player("San", (long) 1e6, 100000L),//6
                new Player("Elhem", (long) 480e3, 100000L),//7
                new Player("Sof", (long) 100e3, 100000L),//8
                new Player("M", (long) 100e3, 100000L)//9
        });
    }

    public void testReset() throws Exception {
        // No need to test
    }

    public void testSetup() throws Exception {
        // No need to test
    }

    public void testGetValue() throws Exception {
        // XXX Assert value is 0 at the begging
        assertEquals(pot.getValue(), 0);
        // XXX assert total bet without all in situation
        List<Player> players = new ArrayList<>();
        players.add(PLAYERS.get(0));
        players.add(PLAYERS.get(1));
        pot.setup(players);
        PLAYERS.get(0).setBet(100);
        PLAYERS.get(1).setBet(100);
        pot.update();
        assertEquals("pot: " + pot.getValue(), 200L, pot.getValue());
        PLAYERS.get(0).setBet(500);
        PLAYERS.get(1).setBet(100);
        pot.update();
        assertEquals("pot: " + pot.getValue(), 800L, pot.getValue());

        // XXX assert total bet with all in situations
        players = new ArrayList<>();
        Player allin0, allin1, allin2;
        allin0 = new Player("0", 1000, 100);
        allin1 = new Player("1", 1000, 80);
        allin2 = new Player("2", 1000, 20);

        players.add(allin0);
        players.add(allin1);
        players.add(allin2);
        pot.setup(players);//FIXME why allin1 is added before 0
        allin2.allIn();
        allin1.call(20);
        allin0.call(20);
        pot.update();
        allin1.allIn();
        allin0.call(60);
        pot.update();
        allin0.allIn();
        pot.update();
        assertEquals("pot: " + pot.getValue(), 200L, pot.getValue());
        //Same example but they all got allin at once
        players = new ArrayList<>();
        allin0 = new Player("0", 1000, 100);
        allin1 = new Player("1", 1000, 80);
        allin2 = new Player("2", 1000, 20);
        players.add(allin0);
        players.add(allin1);
        players.add(allin2);
        pot.setup(players);

        allin0.allIn();
        allin1.allIn();
        allin2.allIn();
        pot.update();
        assertEquals("pot: " + pot.getValue(), 200L, pot.getValue());

        // XXX Test a Random situation
        Random r = new Random();
        int PLAYER_COUNT = r.nextInt(PLAYERS.size());
        players = new ArrayList<>();
        for (Player player : PLAYERS)
            players.add(player);
        pot.setup(players);
        long amountCalled = Math.abs(r.nextInt());
        long potValue = 0;
        for (Player player : players) {
            amountCalled = player.play(amountCalled);
            if (player.isOut() == false)
                potValue += amountCalled;

        }
        pot.update();
        assertEquals(potValue, pot.getValue());
    }

    /**
     * was tested in the same time with getValue
     *
     * @throws Exception
     */
    public void testUpdate() throws Exception {

    }

    /**
     * Situation covered by the test
     * - Only one player wins the pot
     * - two players wins all the pot
     * - Player win a main pot other win side pot
     * - check busted player in those situation
     *
     * @throws Exception
     */
    public void testDistributeToWinners() throws Exception {
        List<Player> players = new ArrayList<>();
        players.add(PLAYERS.get(0));
        players.add(PLAYERS.get(1));
        players.add(PLAYERS.get(2));
        pot.setup(players);
        PLAYERS.get(0).setHand(new HandHoldem(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.ACE, Suit.DIAMONDS)));
        PLAYERS.get(1).setHand(new HandHoldem(new Card(Rank.KING, Suit.CLUBS), new Card(Rank.KING, Suit.DIAMONDS)));
        PLAYERS.get(2).setHand(new HandHoldem(new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.QUEEN, Suit.DIAMONDS)));

        long cash[] = new long[3];

        //case 0:  2 all in and 1 fold
        PLAYERS.get(0).allIn();
        PLAYERS.get(1).allIn();
        PLAYERS.get(2).fold();
        pot.update();
        for (int i = 0; i < 3; i++)
            cash[i] = PLAYERS.get(i).getCash();
        pot.distributeToWinners();
        assertEquals("update: cash(0)=" + PLAYERS.get(0).getCash(), cash[0] + cash[1], PLAYERS.get(0).getCash());
        assertEquals("update: cash(1)=" + PLAYERS.get(1).getCash(), 0, PLAYERS.get(1).getCash());
        assertEquals("update: cash(2)=" + PLAYERS.get(2).getCash(), cash[2], PLAYERS.get(2).getCash());

        //case 1: 2 all in 1 fold
        PLAYERS.get(2).call(10);
        PLAYERS.get(0).allIn();
        PLAYERS.get(1).addCash(100);
        PLAYERS.get(1).allIn();
        PLAYERS.get(2).fold();
        pot.update();
        for (int i = 0; i < 3; i++)
            cash[i] = PLAYERS.get(i).getCash();
        pot.distributeToWinners();
        assertEquals("update: cash(0)=" + PLAYERS.get(0).getCash(), 10 + cash[0] + cash[1], PLAYERS.get(0).getCash());
        assertEquals("update: cash(1)=" + PLAYERS.get(1).getCash(), 0, PLAYERS.get(1).getCash());
        assertEquals("update: cash(2)=" + PLAYERS.get(2).getCash(), cash[2] - 10, PLAYERS.get(2).getCash());


    }

}