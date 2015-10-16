package com.hmrocket.poker;

/**
 * Created by hmrocket on 15/10/2015.
 */
public class PokerTools {

    private static final int MIN_BUY_IN_MULTIPLYER = 10;
    private static final int MAX_BUY_IN_MULTIPLYER = 200;

    public static long getMinBuyIn(long minBet) {
        return minBet * MIN_BUY_IN_MULTIPLYER;
    }

    public static long getMaxBuyIn(long minBet) {
        return minBet * MAX_BUY_IN_MULTIPLYER;
    }
}
