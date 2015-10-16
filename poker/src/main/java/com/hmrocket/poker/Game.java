package com.hmrocket.poker;

import com.hmrocket.poker.card.Deck;
import com.hmrocket.poker.card.HandHoldem;

import java.util.List;

/**
 * Created by hmrocket on 13/10/2015.
 */
public class Game {

    protected interface GameEvent {
        public void gameEnded();
        public void playerBusted(Player player);
    }
    private PokerRound pokerRound;
    private Pot pot;
    private Deck deck;



    public Game() {
        pot = new Pot();
        deck = new Deck();
    }

    public Game(long mibBet, GameEvent gameEvent) {
        // TODO auto generated
    }

    public void startNewHand(List<Player> players, int dealerIndex) {
        deck.reset();
        pot.reset();
        // give players new Hand
        for(Player player : players) {
            player.setState(Player.PlayerState.ACTIVE);
            HandHoldem handHoldem = new HandHoldem(deck.drawCard(), deck.drawCard());
            player.setHand(handHoldem);
        }
        pokerRound = new PokerRound(players, dealerIndex);
    }

    public void removePlayer(Player player) {
        pokerRound.removePlayer(player);
    }
}
