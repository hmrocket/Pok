package com.hmrocket.magicpoker.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.hmrocket.magicpoker.R;
import com.hmrocket.poker.Player;
import com.hmrocket.poker.card.CommunityCards;
import com.hmrocket.poker.card.Flop;

import java.util.List;

/**
 * Created by hmrocket on 15/11/2015.
 */
public class TableView extends RelativeLayout {

	/**
	 * Max player that TableView can hold
	 */
	public static final int MAX_PLAYERS = 9;
	/**
	 * number of CardViews (shared cards)
	 */
	public static final int COMMUNITY_CARDS = 5;

	protected PotView potView;
	/**
	 * array Contains all the playerViews, layout can hold max 9 playerViews the other will be invisible
	 */
	protected PlayerView[] playerViews;
	/**
	 * array contains 5 CardViews to represent the CommunityCards
	 */
	protected CardView[] cardViews;


	public TableView(Context context) {
		super(context);
		init();
	}

	private void init() {
		View view = inflate(getContext(), R.layout.table_view, this);
		potView = (PotView) view.findViewById(R.id.potView);
		playerViews = new PlayerView[MAX_PLAYERS];
		cardViews = new CardView[COMMUNITY_CARDS];

		playerViews[0] = (PlayerView) findViewById(R.id.playerView0);
		playerViews[1] = (PlayerView) findViewById(R.id.playerView1);
		playerViews[2] = (PlayerView) findViewById(R.id.playerView2);
		playerViews[3] = (PlayerView) findViewById(R.id.playerView3);
		playerViews[4] = (PlayerView) findViewById(R.id.playerView4);
		playerViews[5] = (PlayerView) findViewById(R.id.playerView6);
		playerViews[6] = (PlayerView) findViewById(R.id.playerView6);
		playerViews[7] = (PlayerView) findViewById(R.id.playerView7);
		playerViews[8] = (PlayerView) findViewById(R.id.playerView8);

		CardView cardView;
		int cardViewIDs[] = new int[]{R.id.view_communityCard0, R.id.view_communityCard1, R.id.view_communityCard2,
				R.id.view_communityCard3, R.id.view_communityCard4};
		for (int i = 0; i < cardViews.length; i++) {
			cardView = (CardView) findViewById(cardViewIDs[i]);
			cardView.facedown(false);
			cardViews[i] = cardView;
		}
	}

	public TableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public TableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}


	/**
	 * Get the PlayerView affect to that seat
	 *
	 * @param seatId seat number on the table should be between 0 and less than {@link #MAX_PLAYERS}
	 * @return PlayerView number <code>seatId</code>
	 */
	public PlayerView getPlayerView(int seatId) {
		return playerViews[seatId];
	}

	/**
	 * affect a player to a PlayerView in the TableView
	 * Note: The player must be affected to a seat, getSeat must not return null
	 *
	 * @param player player to add to the TableView
	 */
	public void setPlayerView(@NonNull Player player) {
		playerViews[player.getSeat().getId()].setVisibility(VISIBLE);
		playerViews[player.getSeat().getId()].updateView(player);
	}

	/**
	 * populate TableView with PlayerViews using a list of Player
	 *
	 * @param playerList list of players
	 */
	public void populate(List<Player> playerList) {
		for (Player player : playerList) {
			PlayerView playerView = playerViews[player.getSeat().getId()];
			// TODO make the views invisble at the beggining
			playerView.setVisibility(VISIBLE);
			playerView.updateView(player);
		}
	}

	/**
	 * set the shared card on the TableView
	 *
	 * @param communityCards communityCards object hold the shared card on the table
	 */
	public void setCommunityCardsView(@NonNull CommunityCards communityCards) {
		Flop flop = communityCards.getFlop();
		if (flop != null) {
			cardViews[0].setCard(flop.getCard1());
			cardViews[1].setCard(flop.getCard2());
			cardViews[2].setCard(flop.getCard3());
		}
		cardViews[3].setCard(communityCards.getTurn());
		cardViews[4].setCard(communityCards.getRiver());
	}

	/**
	 * Remove a Player from the TableView
	 *
	 * @param player to remove
	 */
	public void removePlayer(Player player) {
		PlayerView playerView = playerViews[player.getSeat().getId()];
		playerView.setVisibility(INVISIBLE);
		playerView.reset();
	}

	/**
	 * set the PotView text value and chip color
	 *
	 * @param potValue value of the pot
	 */
	public void setPot(long potValue) {
		potView.setAmount(potValue);
	}
}
