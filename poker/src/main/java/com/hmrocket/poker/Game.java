package com.hmrocket.poker;

import com.hmrocket.poker.card.Deck;
import com.hmrocket.poker.card.HandHoldem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hmrocket on 13/10/2015.
 */
public class Game implements PokerRound.RoundEvent {

	protected interface GameEvent { // TODO Table is expecting a callback when game ends and when a player doesn't have enough money to bet
		public void gameEnded();

		public void playerBusted(Player player);
	}

	private PokerRound pokerRound;
	private Pot pot;
	private Deck deck;
	private GameEvent gameEventListener;


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
			case RIVER: // Game ended
				pot.distributeToWinners();
				if (gameEventListener != null) gameEventListener.gameEnded();
				break;
		}
	}

	@Override
	public void onRaise() {

	}


}
