/**
 *
 */
package com.hmrocket.poker;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;


/**
 * Make sure that everyone has bet the right amount
 * Handle the rounds
 *
 * @author mhamed
 * @since Oct 8, 2015
 */
public class PokerRound extends Round {


    protected interface RoundEvent {
        public void onRoundFinish(RoundPhase phase, HashMap<Player, Long> bets);

        public void onRaise();

    }

    private HashMap<Player, Long> bets;
    private long calledAmount;
    private RoundPhase phase;
    private RoundEvent roundEvent;

    // Only table or game should create this if we will pass this object to the player
    public PokerRound(List<Player> playersOrderedRightLeft, Player dealer) {
        super(playersOrderedRightLeft, dealer);
        bets = new HashMap<>();
    }

    public PokerRound(List<Player> playersOrderedRightLeft, int dealer) {
        super(playersOrderedRightLeft, dealer);
        bets = new HashMap<>();
    }

    public PokerRound(long minBet, List<Player> playersOrderedRightLeft, int dealerIndex) {
        super(playersOrderedRightLeft, dealerIndex);
        calledAmount = minBet;
        setup(minBet);
    }

    /**
     * Handle the first bets
     * Add small blind and big blind before Round start
     * Note: the dealer isn't the one to start first is the big blind player
     */
    private void setup(long minBet) {
        phase = null;
        bets = new HashMap<>();
        Player smallBlindPlayer = super.nextTurn(); // I prefer using super rather this for no reason
        bets.put(smallBlindPlayer, smallBlindPlayer.raise(minBet / 2));

        // big blind is the one to start the game
        Player bigBlindPlayer = super.nextTurn(); // I prefer using super rather this for no reason
        bets.put(bigBlindPlayer, bigBlindPlayer.raise(minBet));
        startGame(bigBlindPlayer);
    }

    public void startGame(Player startPlayer) {
        phase = RoundPhase.PRE_FLOP;

        while (isCompleted() == false) {
            this.newRound(startPlayer); // new poker round (not super new round )
            roundEvent.onRoundFinish(phase, bets);
            setNextPhase();
        }

    }

    private void setNextPhase() {
        if (phase != RoundPhase.RIVER)
            phase =  RoundPhase.values()[phase.ordinal() + 1];
    }

    @Override
    public void newRound(Player playerToStart) {
        super.newRound(playerToStart);
        do {
            // FIXME either
            playerToStart.play(calledAmount); // player play a move
            if (playerToStart.didRaise()) {
                // Start new raising Round
                super.newRound();
            }
        } while (super.isCompleted() == false);

    }


    /**
     * @return next Player still in the game
     */
    @Override
    public Player nextTurn() {
        Player player = null;
        while ((player = super.nextTurn()) != null &&
                player.isPlaying() == false) {
            // loop until you find a player active
        }
        return player;
    }

    @Override
    void removePlayer(Player player) {
    // The plan was to remove player who folded or quit, now changed (the pot will handle that)
    // Don't remove player
        super.removePlayer(player);
        // If I remove folded player... bet where should it go - should I even remove it ?
        // bets.remove(player);
    }


    public void call(Player player) {
        // FIXME Pot will have a list of Player to him to handle (Player got bet amount use that to have EqualBet)
        if (bets.containsKey(player)) {
            raise(player, calledAmount);
        } else
            bets.put(player, calledAmount);
    }


    public void placeBet(Player player, long amount) {
        if (bets.containsKey(player)) {
            raise(player, amount);
        } else
            bets.put(player, amount);
    }

    public void raise(Player player, long amount) {
        bets.put(player, bets.get(player) + amount);

    }

    public void fold(Player player) {
        // remove player
        removePlayer(player);
        nextTurn();
    }

}

/**
 * <ul>
 * <li>Generic class that define Round on a list of player</li>
 * <li>The Round is completed when all player has played their turns</li>
 * <li>By default the direction is from right to left</li>
 * </ul>
 */
class Round {

    /**
     * Active {@link Player} in this round. Turn direction may varies
     */
    private List<Player> players;
    private int playerTurn; // current turn
    private int turnLeft; //
    private int turnStart;
    private int turnIncrement = 1;

    protected Round(List<Player> playersOrderedRightLeft, Player playerToStart) {
        init(playersOrderedRightLeft, playersOrderedRightLeft.indexOf(playerToStart), true);
    }

    protected Round(List<Player> playersOrderedRightLeft, int playerToStart) {
        init(playersOrderedRightLeft, playerToStart, true);
    }

    private void init(List<Player> playersOrderedRightLeft, int playerToStart, boolean rightToLeft) {
        if (playerToStart >= playersOrderedRightLeft.size() || playerToStart < 0)
            throw new InvalidParameterException("Invalid player starter");
        this.players = playersOrderedRightLeft;
        playerTurn = turnStart = playerToStart;
        turnLeft = playersOrderedRightLeft.size();
        turnIncrement = rightToLeft ? 1 : -1;
    }

    protected Round(List<Player> playersOrdered, boolean rightToLeft) {
        init(playersOrdered, 0, rightToLeft);
    }

    protected Round(List<Player> playersOrdered, int playerTurn, boolean rightToLeft) {
        init(playersOrdered, playerTurn, rightToLeft);
    }

    public Player nextTurn() {
        if (isCompleted()) {
            return null;
        } else {
            turnLeft--;
            increment();
            return players.get(playerTurn);
        }
    }

    private void increment() {
        playerTurn += turnIncrement;
        if (playerTurn >= players.size()) {
            playerTurn = 0;
        } else if (playerTurn < 0) {
            playerTurn = players.size() - 1;
        }
    }

    protected boolean isCompleted() {
        if (turnLeft <= 0) {
            return true;
        } else return false;
    }

    protected void reverseRoundSens() {
        turnIncrement *= -1;

    }

    /** Remove a Player from this round and all the next one.
     *  Unlike {@link Round#addPlayer} remove Player is possible while the Game is still on
     * @param player
     */
    void removePlayer(Player player) {
        throw new UnsupportedOperationException();
        // 3 scenarios
         // player turn didn't come yet
        // player is playing
        // player finished his turn
        // remove player and reset TurnLeft

        //players.remove(player);
        //reset();
    }

    /** Add a Player to next round
     *  Note: Player can't be added while the Game is still on (Round not completed)
     * @param player
     */
    void addPlayer(Player player, int index) {
        if (isCompleted() == false)
            throw new IllegalStateException("No Player can be add while the round isn't completed");

        // and adjust turn
        players.add(index, player);
        reset();
    }

    private void reset() {
        turnLeft = players.size();
        playerTurn = turnStart;
    }

    /**
     * Finish the Round
     */
    protected void terminateRound() {
        reset();
    }

    /**
     * Start new round with the same list of player but different start
     *
     * @param playerToStart first Player to start this Round
     */
    protected void newRound(Player playerToStart) {
        reset();
        // turnStart and playerTurn changed
        playerTurn = turnStart = players.indexOf(playerToStart);

    }

    /**
     * Start new round using the same list of player. First Player to start this Round is the same
     */
    protected void newRound() {
        reset();
    }

    /**
     * return the left Player
     *
     * @param players must be ordered from right to left
     * @param player
     * @return Player on the left of <code>player</code>
     */
    protected static Player getLeftPlayer(List<Player> players, Player player) {
        if (players == null || players.isEmpty())
            return null;

        int nextPlayerIndex = players.indexOf(player) + 1;
        if (nextPlayerIndex == 0) //player doesn't exist in the list (index = -1)
            return null;
        else if (nextPlayerIndex == players.size()) // player is in the end, left player is the first of list
            return players.get(0);
        else return players.get(nextPlayerIndex);
    }

}
