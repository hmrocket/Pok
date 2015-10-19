package com.hmrocket.poker;

import com.hmrocket.poker.card.CommunityCards;
import com.hmrocket.poker.card.Deck;
import com.hmrocket.poker.card.HandHoldem;
import com.hmrocket.poker.pot.Pot;

import java.util.List;
import java.util.Set;

/**
 * Created by hmrocket on 13/10/2015.
 */
public class Game implements PokerRound.RoundEvent {

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
        pot.setup(players);
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

    /**
     * Poker Round only ends when (1) only one player is left or
     * (2) all remaining players have matched the highest total bet made during the round.
     */
    @Override // TODO Game should check win
    public void onRoundFinish(RoundPhase phase, List<Player> players) {
        pot.update();

        if (isAllPlayersExceptOneFolded(players)) endGame();
        else
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
                    showdown();
                    break;
            }
    }

    private boolean isAllPlayersExceptOneFolded(List<Player> players) {
        int numberOfPlayerNotOut = 0;
        for (Player player :
                players) {
            if (player.isOut() == false) numberOfPlayerNotOut++;
            if (numberOfPlayerNotOut > 1) return false;
        }
        return true;
    }

    /**
     * when
     */
    private void endGame() {
        Set<Player> busted = pot.distributeToWinners();
        if (gameEventListener != null) {
            gameEventListener.playerBusted(busted);
            gameEventListener.gameEnded();
        }
    }

    private void showdown() {
        endGame();
    }

    @Override
    public void onRaise() {

    }

    protected interface GameEvent { // Table is expecting a callback when game ends and when a player doesn't have enough money to bet
        public void gameEnded();

        public void playerBusted(Set<Player> player);
    }


}
