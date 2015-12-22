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
		pokerRound.startGame();
	}

	public void removePlayer(Player player) {
		pokerRound.removePlayer(player);
	}

	/**
	 * Poker Round only ends when (1) only one player is left or
	 * (2) all remaining players have matched the highest total bet made during the round.
	 */
	@Override
	public void onRoundFinish(RoundPhase phase, List<Player> players) {
		long potValue = pot.getValue();
		pot.update();
		// fire a callback only if the Pot value changed
		if (potValue != pot.getValue()) gameEventListener.onPotChanged(pot.getValue());

		// distinct two case end game and showdown
		if (isAllPlayersExceptOneFolded(players)) {
			endGame(false);
		} else if (phase == RoundPhase.RIVER || isLess2PlayersPlaying(players)) {
			showdown();
		}
	}

	@Override
	public void onPreTurn(Player player, Turn turn) {
		gameEventListener.onPreTurn(player, turn);
	}

	@Override
	public void onTurnEnded(Player player) {
		gameEventListener.onTurnEnded(player);
	}

	@Override
	public void onRound(RoundPhase roundPhase) {
		if (roundPhase != RoundPhase.PRE_FLOP) {
			// reset the playing player state to active for the new round
			for (Player p : pokerRound.getPlayers()) {
				if (p.isPlaying())
					p.setState(Player.PlayerState.ACTIVE);
			}
		}
		// update shared card
		switch (roundPhase) {
			case FLOP:
				communityCards.setFlop(deck.dealFlop());
				gameEventListener.onCommunityCardsChange(RoundPhase.FLOP, communityCards);
				break;
			case TURN:
				communityCards.setTurn(deck.drawCard());
				gameEventListener.onCommunityCardsChange(RoundPhase.TURN, communityCards);
				break;
			case RIVER:
				communityCards.setRiver(deck.drawCard());
				gameEventListener.onCommunityCardsChange(RoundPhase.RIVER, communityCards);
				break;
		}
		gameEventListener.onRound(roundPhase);
	}

	@Override
	public void onBlindPosted(Player smallBlind, Player bigBlind) {
		gameEventListener.onBlindPosted(smallBlind, bigBlind);
	}

	/**
	 * This is method handle situation like Player inactive and zzz unlike turn#isAllPlayersExceptOneFolded
	 *
	 * @param players player list
	 * @return true if all the player is folded except one, false otherwise
	 */
	private boolean isAllPlayersExceptOneFolded(List<Player> players) {
		int numberOfPlayerNotOut = 0;
		for (Player player :
				players) {
			if (!player.isOut()) {
				numberOfPlayerNotOut++;
				if (numberOfPlayerNotOut > 1) return false;
			}
		}
		return true;
	}

	/**
	 * distribute pot and callback playerBusted() and gameEnded()
	 * @param isShowdown true if the game is ended by a showdown, false otherwise
	 */
	private void endGame(boolean isShowdown) {
		Set<Player> busted = pot.distributeToWinners(isShowdown, gameEventListener);
		if (gameEventListener != null) {
			if (!busted.isEmpty()) gameEventListener.playerBusted(busted);
			gameEventListener.gameEnded();
		} else {
			if (PokerTools.DEBUG)
				System.out.println("listener is null");
		}
	}

	/**
	 * Check if less than two player are playing, This showdown condition (second end of game condition).
	 * This is method handle situation like Player inactive and zzz unlike turn#isLess2PlayersPlaying
	 * @param players player list
	 * @return true if less than two players are playing, false otherwise
	 */
	protected boolean isLess2PlayersPlaying(List<Player> players) {
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
		gameEventListener.onShowdown(pot.getPotentialWinners());
		// add the rest of the card
		switch (communityCards.getMissingCardCount()) {
			case 5:
				communityCards.setFlop(deck.dealFlop());
				gameEventListener.onCommunityCardsChange(RoundPhase.FLOP, communityCards);
			case 2:
				communityCards.setTurn(deck.drawCard());
				gameEventListener.onCommunityCardsChange(RoundPhase.TURN, communityCards);
			case 1:
				communityCards.setRiver(deck.drawCard());
				gameEventListener.onCommunityCardsChange(RoundPhase.RIVER, communityCards);
		}
		endGame(true);
	}

}
