package com.hmrocket.magicpoker.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hmrocket.magicpoker.R;
import com.hmrocket.poker.Player;
import com.hmrocket.poker.RoundPhase;
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
	/**
	 * represent the game info (winning HandType, RoundPhase)
	 */
	protected TextView txInfo;


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
		playerViews[5] = (PlayerView) findViewById(R.id.playerView5);
		playerViews[6] = (PlayerView) findViewById(R.id.playerView6);
		playerViews[7] = (PlayerView) findViewById(R.id.playerView7);
		playerViews[8] = (PlayerView) findViewById(R.id.playerView8);

		CardView cardView;
		int cardViewIDs[] = new int[]{R.id.cardView_communityCard0, R.id.cardView_communityCard1, R.id.cardView_communityCard2,
				R.id.cardView_communityCard3, R.id.cardView_communityCard4};
		for (int i = 0; i < cardViews.length; i++) {
			cardView = (CardView) findViewById(cardViewIDs[i]);
			cardView.facedown(false);
			cardViews[i] = cardView;
		}

		txInfo = (TextView) findViewById(R.id.tx_tableInfo);
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
			playerView.setVisibility(VISIBLE);
			playerView.updateView(player);
		}
	}

	/**
	 * remove all PlayerView from the TableView
	 * call {@link #clear()} if you need to clear PotView and 5 shared CardView
	 */
	public void abandon() {
		for (PlayerView p : playerViews) {
			p.setVisibility(INVISIBLE);
			p.reset();
		}
	}

	/**
	 * Specify the dealer
	 *
	 * @param seatId seat number on the table should be between 0 and less than {@link #MAX_PLAYERS}
	 */
	public void setDealer(int seatId) {
		for (int i = 0; i < seatId; i++)
			playerViews[i].setDealer(false);

		playerViews[seatId].setDealer(true);

		for (int i = seatId + 1; i < playerViews.length; i++)
			playerViews[i].setDealer(false);
	}

	/**
	 * set the shared card on the TableView
	 *
	 * @param communityCards communityCards object hold the shared card on the table
	 * @param roundPhase     phase round of the game
	 */
	public void setCommunityCardsView(RoundPhase roundPhase, @NonNull CommunityCards communityCards) {
		switch (roundPhase) {
			case FLOP:
				Flop flop = communityCards.getFlop();
				cardViews[0].setCard(flop.getCard1());
				cardViews[1].setCard(flop.getCard2());
				cardViews[2].setCard(flop.getCard3());
				break;
			case TURN:
				cardViews[3].setCard(communityCards.getTurn());
				break;
			case RIVER:
				cardViews[4].setCard(communityCards.getRiver());
				break;
		}
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

	/**
	 * set text under the shared card
	 *
	 * @param stringResId String resource id
	 */
	public void setInfo(int stringResId) {
		txInfo.setText(stringResId);
	}

	/**
	 * reset the PotView, reset the 5 shared CardView and reset CardView from PlayerView
	 */
	public void clear() {
		potView.setAmount(0);
		for (CardView cardView : cardViews) {
			cardView.reset();
		}
		for (PlayerView p : playerViews) {
			p.setHand(null);
		}
		txInfo.setText(null);
		showInfo(false);
	}

	/**
	 * show/ hide text view under the shared card
	 *
	 * @param show false will hide the textView info, true will set it to visible
	 */
	public void showInfo(boolean show) {
		if (show)
			txInfo.setVisibility(VISIBLE);
		else txInfo.setVisibility(INVISIBLE);
	}

	/**
	 * Create and move a PotView from the position of the table potView to the Winner Player
	 *
	 * @param winner playerView to translate certain amount to
	 * @param value  the value of the pot transferred
	 */
	public void movePot(Player winner, long value) {
		// create a PotView
		final PotView potView = new PotView(getContext());
		potView.setLayoutParams(new ViewGroup.LayoutParams(this.potView.getWidth(), this.potView.getHeight()));
		potView.setAmount(value);

		int[] potLocation = new int[2];
		this.potView.getLocationOnScreen(potLocation);
		int[] playerLocation = new int[2];
		getPlayerView(winner.getSeat().getId()).getLocationInWindow(playerLocation);


		// animate from potView to the player seat
		// XXX for some reason I can get hold of the right position of PotView
		//potLocation[0] = this.potView.getRight();
		potLocation[1] = this.potView.getBottom();

		TranslateAnimation anim = new TranslateAnimation(potLocation[0], playerLocation[0], potLocation[1], playerLocation[1]);
		TableView.this.addView(potView);

		boolean isCloseToPot = winner.getSeat().getId() == 4 || winner.getSeat().getId() == 5;
		anim.setDuration(isCloseToPot ? 1000 : 2000);
		anim.setInterpolator(getContext(), android.R.interpolator.linear);
		anim.setFillAfter(true);
		anim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				TableView.this.removeView(potView);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		potView.startAnimation(anim);
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
}
