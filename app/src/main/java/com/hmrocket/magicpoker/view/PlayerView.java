package com.hmrocket.magicpoker.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
	 * Update the PlayerView to reflect Player move (play)
	 *
	 * @param player player affected to this view
	 */
	public void updateView(@NonNull Player player) {
		setCash(player.getCash());
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
		// getColorStateList(id) is deprecated in M
		// FIXME set background of the image using ColorStateList or Tint avoid having color and drawable if possible
		switch (playerState) {
			case FOLD:
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					profileImage.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.fold_selector));
				} else {
					profileImage.setBackgroundColor(getResources().getColor(R.color.fold_selector));
				}
				break;
			case RAISE:
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					profileImage.setBackgroundTintList(getResources().getColorStateList(R.color.raise_selector, null));
				} else {
					profileImage.setBackgroundColor(getResources().getColor(R.color.raise_selector));
				}
				break;
			case ALL_IN:
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					// I don't care about theme that why isn't getContext().getTheme()
					profileImage.setBackgroundTintList(getResources().getColorStateList(R.color.allin_selector, null));
				} else {
					profileImage.setBackgroundColor(getResources().getColor(R.color.allin_selector));
				}
				break;
			case CALL:
			case CHECK:
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					profileImage.setBackgroundTintList(getResources().getColorStateList(R.color.call_selector, getContext().getTheme()));
				} else {
					profileImage.setBackgroundColor(getResources().getColor(R.color.call_selector));
				}
				break;
			case ACTIVE:
				profileImage.setBackgroundColor(Color.TRANSPARENT);
				break;
			default:
				profileImage.setBackgroundColor(Color.LTGRAY);
				break;
		}

	}

	/**
	 * Update the PlayerView to reflect and represent the object Player
	 *
	 * @param player
	 */
	public void setPlayer(@NonNull Player player) {
		setCash(player.getCash());
		setInfo(player);
		setState(player.getState());
	}

	/**
	 * Flip hand cards
	 */
	public void showCards() {
		cardView1.facedown(false);
		cardView2.facedown(false);
	}

	/**
	 * set the CardView of PlayerView
	 * @param hand two cards
	 */
	public void setHand(Hand hand) {
		if (hand != null) {
			cardView1.setCard(hand.getCard1());
			cardView2.setCard(hand.getCard2());
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).setEnabled(enabled);
		}
	}

	/**
	 * reset the PlayerView
	 */
	public void reset() {
		cardView1.reset();
		cardView2.reset();
		txCash.setText("");
		txInfo.setText("");
		profileImage.setImageDrawable(null);
	}
}
