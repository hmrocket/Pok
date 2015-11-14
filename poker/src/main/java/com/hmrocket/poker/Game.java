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

	public void startNewHand(long minBet, List<Player> players, int dealerIndex) {
		deck.reset();
		pot.setup(players);
		communityCards = new CommunityCards();

		// give players new Hand
		for (Player player : players) {
			player.setState(Player.PlayerState.ACTIVE);
			HandHoldem handHoldem = new HandHoldem(deck.drawCard(), deck.drawCard(), communityCards);
			player.setHandHoldem(handHoldem);
			if (PokerTools.DEBUG) System.out.println(player);
		}
		pokerRound = new PokerRound(minBet, players, dealerIndex, this);
	}

	public void removePlayer(Player player) {
		pokerRound.removePlayer(player);
	}

	/**
	 * Poker Round only ends when (1) only one player is left or
	 * (2) all remaining players have matched the highest total bet made during the round.
	 */
	@Override // TODO Optimize isAllPlayersExceptOneFolded isn't needed t
	public void onRoundFinish(RoundPhase phase, List<Player> players) {
		pot.update();

		// distinct two case end game and showdown
		if (isAllPlayersExceptOneFolded(players)) endGame();
		else if (isAllPlayersNotPlayingExceptOne(players)) showdown();
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
			if (player.isOut() == false) {
				numberOfPlayerNotOut++;
				if (numberOfPlayerNotOut > 1) return false;
			}
		}
		return true;
	}

	/**
	 * when
	 */
	private void endGame() {
		Set<Player> busted = pot.distributeToWinners(gameEventListener);
		if (gameEventListener != null) {
			if (!busted.isEmpty()) gameEventListener.playerBusted(busted);
			gameEventListener.gameEnded();
		} else {
			if (PokerTools.DEBUG)
				System.out.println("listener is null");
		}
	}

	// TODO reuse the code see PokerRound
	protected boolean isAllPlayersNotPlayingExceptOne(List<Player> players) {
		int numberOfPlayerPlaying = 0;
		for (Player player : players) {
			if (player.isPlaying()) {
				numberOfPlayerPlaying++;
				if (numberOfPlayerPlaying > 1) return false;
			}
		}
		return true;
	}

	private void showdown() {
		// add the rest of the card
		int missingCard = communityCards.getFlop() == null ? 5 :
				(communityCards.getTurn() == null ? 2 : (communityCards.getRiver() == null ? 1 : 0));
		switch (missingCard) {
			case 5:
				communityCards.setFlop(deck.dealFlop());
			case 2:
				communityCards.setTurn(deck.drawCard());
			case 1:
				communityCards.setRiver(deck.drawCard());
		}
		endGame();
	}

	@Override
	public void onRaise() {

	}

	public interface GameEvent { // Table is expecting a callback when game ends and when a player doesn't have enough money to bet
		public void gameEnded();

		public void playerBusted(Set<Player> player);

		/**
		 * Called by Pot whenever there's some got a money from the pot
		 *
		 * @param last    if false winner are just LevelWinner, ther will be other callback of gameWinners, true if these winner(s) win the last pot
		 * @param winners can be level winners (side pot winner) or game winners
		 */
		public void gameWinners(boolean last, Set<Player> winners);
	}


}
