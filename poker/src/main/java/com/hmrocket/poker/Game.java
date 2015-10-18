package com.hmrocket.poker;

import com.hmrocket.poker.card.CommunityCards;
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
    private CommunityCards communityCards;
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
    public void startNewHand(long minBet, List<Player> players, int dealerIndex) {
        deck.reset();
        pot.reset();
        communityCards = new CommunityCards();
        // give players new Hand
        for (Player player : players) {
            player.setState(Player.PlayerState.ACTIVE);
            HandHoldem handHoldem = new HandHoldem(deck.drawCard(), deck.drawCard(), communityCards);
            player.setHand(handHoldem);
        }
        pokerRound = new PokerRound(minBet, players, dealerIndex);
        pokerRound.startGame();
    }

    public void removePlayer(Player player) {
        pokerRound.removePlayer(player);
    }

    @Override
    public void onRoundFinish(RoundPhase phase, HashMap<Player, Long> bets) {
        pot.addBet(bets);
        switch (phase) {
            case PRE_FLOP:
                communityCards.setFlop(deck.dealFlop());
                break;
            case FLOP:
                communityCards.setTurn(deck.drawCard());
                break;
            case TURN:
                communityCards.setRiver(deck.drawCard());
                break;
            case RIVER: // Game ended // the game not always ends here !
                Set<Player> busted = pot.distributeToWinners();
                if (gameEventListener != null) {
                    gameEventListener.playerBusted(busted);
                    gameEventListener.gameEnded();
                }
                break;
        }
    }

    @Override
    public void onRaise() {

    }


}
