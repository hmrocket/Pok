package com.hmrocket.poker;

import com.hmrocket.poker.card.Deck;
import com.hmrocket.poker.card.HandHoldem;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by hmrocket on 13/10/2015.
 */
public class Game implements PokerRound.RoundEvent {

    protected interface GameEvent { // Table is expecting a callback when game ends and when a player doesn't have enough money to bet
        public void gameEnded();

        public void playerBusted(Set<Player> player);
    }

    private PokerRound pokerRound;
    private Pot pot;
    private Deck deck;
    private GameEvent gameEventListener;


    protected Game() {
        pot = new Pot();
        deck = new Deck();
    }

    public Game(GameEvent gameEvent) {
        pot = new Pot();
        deck = new Deck();
        gameEventListener = gameEvent;
    }

    //TODO POker round should aks for mean bet
    public void startNewHand(long mibBet, List<Player> players, int dealerIndex) {
        deck.reset();
        pot.reset();
        // give players new Hand
        for (Player player : players) {
            player.setState(Player.PlayerState.ACTIVE);
            HandHoldem handHoldem = new HandHoldem(deck.drawCard(), deck.drawCard());
            player.setHand(handHoldem);
        }
        pokerRound = new PokerRound(players, dealerIndex);
    }

    public void removePlayer(Player player) {
        pokerRound.removePlayer(player);
    }

    @Override
    public void onRoundFinish(RoundPhase phase, HashMap<Player, Long> bets) {
        pot.addBet(bets);
        switch (phase) {
            case PRE_FLOP:
                deck.dealFlop();
                break;
            case FLOP:
                deck.drawCard();
                break;
            case TURN:
                deck.drawCard();
                break;
            case RIVER: // Game ended
                Set<Player> busted = pot.distributeToWinners();
                if (gameEventListener != null) {
                    if (busted != null) gameEventListener.playerBusted(busted);
                    gameEventListener.gameEnded();
                }
                break;
        }
    }

    @Override
    public void onRaise() {

    }


}
