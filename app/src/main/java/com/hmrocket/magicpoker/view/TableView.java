package com.hmrocket.magicpoker.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.hmrocket.magicpoker.R;

/**
 * Created by hmrocket on 15/11/2015.
 */
public class TableView extends RelativeLayout {

	/**
	 * Max player that TableView can hold
	 */
	private static final int MAX_PLAYERS = 9;
	/**
	 * number of CardViews (shared cards)
	 */
	private static final int COMMUNITY_CARDS = 5;

	protected PotView potView;
	/**
	 * array Contains all the playerViews, layout can hold max 9 playerViews the other will be invisible
	 */
	protected PlayerView[] playerViews;
	/**
	 * array contains 5 CardViews to represent the CommunityCards
	 */
	protected CardView[] communityCards;


	public TableView(Context context) {
		super(context);
		init();
	}

	private void init() {
		View view = inflate(getContext(), R.layout.table_view, this);
		potView = (PotView) view.findViewById(R.id.potView);
		playerViews = new PlayerView[MAX_PLAYERS];
		communityCards = new CardView[COMMUNITY_CARDS];

		playerViews[0] = (PlayerView) findViewById(R.id.playerView0);
		playerViews[1] = (PlayerView) findViewById(R.id.playerView1);
		playerViews[2] = (PlayerView) findViewById(R.id.playerView2);
		playerViews[3] = (PlayerView) findViewById(R.id.playerView3);
		playerViews[4] = (PlayerView) findViewById(R.id.playerView4);
		playerViews[5] = (PlayerView) findViewById(R.id.playerView6);
		playerViews[6] = (PlayerView) findViewById(R.id.playerView6);
		playerViews[7] = (PlayerView) findViewById(R.id.playerView7);
		playerViews[8] = (PlayerView) findViewById(R.id.playerView8);

		communityCards[0] = (CardView) findViewById(R.id.view_communityCard0);
		communityCards[1] = (CardView) findViewById(R.id.view_communityCard1);
		communityCards[2] = (CardView) findViewById(R.id.view_communityCard2);
		communityCards[3] = (CardView) findViewById(R.id.view_communityCard3);
		communityCards[4] = (CardView) findViewById(R.id.view_communityCard4);

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

}
