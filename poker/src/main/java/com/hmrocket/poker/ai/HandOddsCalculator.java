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

	public HandOddsCalculator(int precisionLevel) {
		this.precisionLevel = precisionLevel;
		this.deck = new Deck();
		this.handOdds = new HandOdds(precisionLevel);
	}

	public HandOdds getHandOdds(int playersCount, HandHoldem handHoldem) {
		// if the hand is null just return HandOdds init to 0 (means 0 winning chances)
		if (handHoldem == null)
			return new HandOdds(1);
		else
			return getHandOdds(playersCount, handHoldem.getHand(), handHoldem.getCommunityCards());
	}

	/**
	 * @param playersCount
	 * @param hand           NotNull
	 * @param communityCards
	 * @return
	 */
	public HandOdds getHandOdds(int playersCount, Hand hand, CommunityCards communityCards) {
		return calculateHandOdds(playersCount, new HandHoldem(hand, communityCards));
	}

	private HandOdds calculateHandOdds(int playersCount, HandHoldem handHoldem) {
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
		List<HandHoldem> handHoldemList = new ArrayList<>(playersCount + 1);
		//Credit: http://cowboyprogramming.com/2007/01/04/programming-poker-ai/
		//simulate the progress of the game a very large number of time, and count the number of those times you win.
		for (int i = 0; i < precisionLevel; i++) {
			handHoldemList.clear();
			// Deal your opponent's hole cards, and the remaining community cards
			for (int j = 0; j < playersCount; j++) {
				HandHoldem handHoldemOppent = new HandHoldem(deck.drawCard(), deck.drawCard(), handHoldem.getCommunityCards());
				handHoldemList.add(handHoldemOppent);
			}
			// deal Flop if flop wasn't dealt
			if (handHoldem.getCommunityCards().getFlop() == null) {
				handHoldem.getCommunityCards().setFlop(deck.dealFlop());
			}
			// dealt turn if wasn't dealt
			if (handHoldem.getCommunityCards().getTurn() == null)
				handHoldem.getCommunityCards().setTurn(deck.drawCard());
			// deal river if wasn't dealt
			if (handHoldem.getCommunityCards().getRiver() == null)
				handHoldem.getCommunityCards().setRiver(deck.drawCard());

			// Evaluate all hands, and see who has the best hands
			handHoldemList = PokerTools.getBestHands(handHoldemList);
			if (handHoldemList.contains(handHoldem)) {
				handOdds.wins(handHoldem.getHandScore().getHandType());
			}
			deck.resetIgnoreBurns();
		}
		return handOdds;
	}

	public HandOdds getHandOdds(int playersCount, Hand hand) {
		return calculateHandOdds(playersCount, new HandHoldem(hand, null));
	}
}
