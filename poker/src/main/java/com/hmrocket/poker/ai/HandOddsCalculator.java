package com.hmrocket.poker.ai;

import com.hmrocket.poker.PokerTools;
import com.hmrocket.poker.card.CommunityCards;
import com.hmrocket.poker.card.Deck;
import com.hmrocket.poker.card.Hand;
import com.hmrocket.poker.card.HandHoldem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hmrocket on 26/10/2015.
 */
public final class HandOddsCalculator {

	private final int precisionLevel;
	private final Deck deck;
	private final HandOdds handOdds;

	/**
	 * @param precisionLevel iterations count
	 */
	public HandOddsCalculator(int precisionLevel) {
		this.precisionLevel = precisionLevel;
		this.deck = new Deck();
		this.handOdds = new HandOdds(precisionLevel);
	}

	/**
	 * @param playersCount total players (including you)
	 * @param handHoldem your hand
	 * @return the odds of your hand wins against <code>playersCount - 1</code> others Hands
	 */
	public HandOdds getHandOdds(int playersCount, HandHoldem handHoldem) {
		// if the hand is null just return HandOdds init to 0 (means 0 winning chances)
		if (handHoldem == null || handHoldem.getHand() == null)
			return new HandOdds(1);
		else
			return calculateHandOdds(playersCount, handHoldem);
	}

	/**
	 *
	 * @param playersCount number of players Playing (you included)
	 * @param handHoldemPlayer your hand
	 * @return the odds your hand will win against the <code>playersCount - 1</code> other players Hand
	 */
	private HandOdds calculateHandOdds(int playersCount, final HandHoldem handHoldemPlayer) {
		// all players point to the same CommunityCard
		// avoid messing with the real CommunityCard create new one
		CommunityCards cc = new CommunityCards(handHoldemPlayer.getFlop(), handHoldemPlayer.getTurn(), handHoldemPlayer.getRiver());
		HandHoldem handHoldem = new HandHoldem(handHoldemPlayer.getHand(), cc);

		handOdds.reset();
		// new deck (full card available
		deck.reset();
		// burn known cards
		deck.burn(handHoldem.getHand().getCard1());
		deck.burn(handHoldem.getHand().getCard2());
		if (handHoldem.getCommunityCards() == null) {
			handHoldem.setCommunityCards(new CommunityCards());
		} else {
			// burn FLop
			if (handHoldem.getCommunityCards().getFlop() != null) {
				deck.burn(handHoldem.getCommunityCards().getFlop().getCard1());
				deck.burn(handHoldem.getCommunityCards().getFlop().getCard2());
				deck.burn(handHoldem.getCommunityCards().getFlop().getCard3());
			}
			// burn turn
			if (handHoldem.getCommunityCards().getTurn() != null)
				deck.burn(handHoldem.getCommunityCards().getTurn());
			// burn river
			if (handHoldem.getCommunityCards().getRiver() != null)
				deck.burn(handHoldem.getCommunityCards().getRiver());
		}
		List<HandHoldem> handHoldemList = new ArrayList<>(playersCount);
		//Credit: http://cowboyprogramming.com/2007/01/04/programming-poker-ai/
		//simulate the progress of the game a very large number of time, and count the number of those times you win.
		for (int i = 0; i < precisionLevel; i++) {
			handHoldemList.clear();
			// you hands first
			handHoldemList.add(handHoldem);
			// Deal your opponent's hole cards, and the remaining community cards
			for (int j = 1; j < playersCount; j++) {
				HandHoldem handHoldemOppent = new HandHoldem(deck.drawCard(), deck.drawCard(), handHoldem.getCommunityCards());
				handHoldemList.add(handHoldemOppent);
			}

			// deal Flop if flop wasn't dealt
			// dealt turn if wasn't dealt
			// deal river if wasn't dealt
			// add the rest of the card,
			CommunityCards communityCards = handHoldemPlayer.getCommunityCards();
			int missingCard = communityCards == null || communityCards.getFlop() == null ? 5 :
					(communityCards.getTurn() == null ? 2 : (communityCards.getRiver() == null ? 1 : 0));
			switch (missingCard) {
				case 5:
					cc.setFlop(deck.dealFlop());
				case 2:
					cc.setTurn(deck.drawCard());
				case 1:
					cc.setRiver(deck.drawCard());
			}

			// Evaluate all hands, and see who has the best hands
			handHoldemList = PokerTools.getBestHands(handHoldemList);
			if (handHoldemList.contains(handHoldem)) {
				handOdds.wins(handHoldem.getHandScore().getHandType());
			}
			deck.resetIgnoreBurns();
		}

		// you need to keep using CommunityCard cc or it will be Cabbage Collector in consequence
		// SoftReference point to null (raison why you have null pointer)
		if (cc == null) {
			if (PokerTools.DEBUG) {
				System.out.println();
			}
		}
		return handOdds;
	}

	/**
	 *
	 * @param playersCount total players (including you)
	 * @param hand your hand
	 * @return the odds of your hand wins against <code>playersCount - 1</code> others Hands
	 */
	public HandOdds getHandOdds(int playersCount, Hand hand) {
		return calculateHandOdds(playersCount, new HandHoldem(hand, null));
	}
}
