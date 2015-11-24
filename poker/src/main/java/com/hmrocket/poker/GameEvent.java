package com.hmrocket.poker;

import com.hmrocket.poker.card.CommunityCards;

import java.util.Set;

/**
 * Created by hmrocket on 19/11/2015.
 * All the GameEvent that happen in single Game
 */
public interface GameEvent {
	/**
	 * Called when, all Player except one fold either holds the highest ranking hand when all the cards are shown
	 */
	void gameEnded();

	/**
	 * Called when a player or more lost all chips or money
	 * There removed from the table and they must either Buy-In again or they are eliminated
	 *
	 * @param player busted players
	 */
	void playerBusted(Set<Player> player);

	/**
	 * Called by Pot whenever there's some got a money from the pot
	 *
	 * @param last    if false winner are just LevelWinner, ther will be other callback of gameWinners, true if these winner(s) win the last pot
	 * @param winners can be level winners (side pot winner) or game winners
	 */
	void gameWinners(boolean last, Set<Player> winners);

	/**
	 * Called by PokerRound before the player start his turn
	 *
	 * @param player           Player current turn
	 * @param turn contains info about this turn like the last bet amount and the min raise amount
	 */
	void onPreTurn(final Player player, final Turn turn);

	/**
	 * Called after the Player make a move (decision or move can be, fold, raise, call, check, all-in)
	 *
	 * @param player Player just finished his turn
	 */
	void onTurnEnded(Player player);

	/**
	 * Called when a new RoundPhase about to start
	 *
	 * @param roundPhase Started RoundPhase
	 */
	void onRound(RoundPhase roundPhase);

	/**
	 * Called after forced bets posted by players
	 *
	 * @param smallBlind Player to the left of the dealer button
	 * @param bigBlind   Player to  the left of the <code>smallBlind</code>
	 */
	void onBlindPosted(Player smallBlind, Player bigBlind);

	/**
	 * Called when more than one player remains (not out) competing for the Pot or part of it
	 *
	 * @param potentialWinners Remaining players whom must expose and compare their hands to win
	 */
	void onShowdown(Set<Player> potentialWinners);

	/**
	 * Called whenever the Pot value change
	 *
	 * @param potValue the sum of money that players wager in the game / sum of money in middle of the table
	 */
	void onPotChanged(long potValue);

	/**
	 * Called when new card dealt face up in the center of the table
	 *
	 * @param communityCards
	 */
	void onCommunityCardsChange(CommunityCards communityCards);
}
