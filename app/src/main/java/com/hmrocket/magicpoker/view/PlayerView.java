package com.hmrocket.magicpoker.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hmrocket.magicpoker.R;
import com.hmrocket.magicpoker.Util;
import com.hmrocket.poker.Player;
import com.hmrocket.poker.card.Hand;


/**
 * @since 10/Nov/2015 - mhamed
 */
public class PlayerView extends RelativeLayout {

	// XXX Assure the View save it's state
	private CardView cardView1;
	private CardView cardView2;
	private ImageView profileImage;
	/**
	 * TextView represent the player cash
	 */
	private TextView txCash;
	/**
	 * Text on the top of the PlayerImage it contains either player's bet or player's name
	 */
	private TextView txInfo;

	public PlayerView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.player_view, this, true);
		profileImage = (ImageView) findViewById(R.id.iv_profile);
		cardView1 = (CardView) findViewById(R.id.cardView1);
		cardView2 = (CardView) findViewById(R.id.cardView2);
		txCash = (TextView) findViewById(R.id.tx_cash);
		txInfo = (TextView) findViewById(R.id.tx_info);

	}

	public PlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public PlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	/**
	 * set the image of player
	 * @param profileImage player image
	 */
	public void setProfileImage(final Bitmap profileImage) {
		this.profileImage.setImageBitmap(profileImage);
	}

	/**
	 * Update the PlayerView to reflect and represent the object Player
	 *
	 * @param player
	 * @param faceUp specify if the card will e face up or down (covered or not)
	 */
	public void updateView(@NonNull Player player, boolean faceUp) {
		setCash(player.getCash());
		if (faceUp)
			setHand(player.getHandHoldem() == null ? null : player.getHandHoldem().getHand());
		setInfo(player);
		setState(player.getState());
	}

	/**
	 * set cash
	 *
	 * @param cash buy-in chips
	 */
	public void setCash(long cash) {
		if (cash < 0) {
			txCash.setText("");
		} else {
			txCash.setText(Util.formatNumber(cash));
		}
	}

	/**
	 * set the CardView of PlayerView
	 * @param hand two cards
	 */
	public void setHand(Hand hand) {
		cardView1.setCard(hand.getCard1());
		cardView2.setCard(hand.getCard2());
	}

	/**
	 * display info about player, If player's bet is 0 the view will show the name, the bet amount otherwise
	 *
	 * @param player player you would like to this veiw to represent
	 */
	public void setInfo(@NonNull Player player) {
		if (player.getBet() > 0)
			txInfo.setText(Long.toString(player.getBet()));
		else txInfo.setText(player.getName());
	}

	/**
	 * set the PlayerView state
	 * @param playerState Player's {@link com.hmrocket.poker.Player.PlayerState state}
	 */
	public void setState(Player.PlayerState playerState) {
		switch (playerState) {
			case FOLD:
				profileImage.setBackground(getResources().getDrawable(R.drawable.fold_selector));
				break;
			case RAISE:
				profileImage.setBackground(getResources().getDrawable(R.drawable.raise_selector));
				break;
			case ALL_IN:
				profileImage.setBackground(getResources().getDrawable(R.drawable.allin_selector));
				break;
			case CALL:
			case CHECK:
				profileImage.setBackground(getResources().getDrawable(R.drawable.call_selector));
				break;
			case ACTIVE:
				profileImage.setBackgroundColor(Color.TRANSPARENT);
				break;
			default:
				profileImage.setBackgroundColor(Color.LTGRAY);
				break;
		}

	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).setEnabled(enabled);
		}
	}
}
