package com.hmrocket.magicpoker.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

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
	private FrameLayout cardContainer;
	private ImageView profileImage;
	private View profileContainer;
	private TextView dealerFlag;
	/**
	 * TextView represent the player cash
	 */
	private TextView txCash;
	/**
	 * Text on the top of the PlayerImage it contains player's name
	 */
	private TextView txInfo;
	/**
	 * PotView on the top of the PlayerImage it contains player's bet
	 */
	private PotView potView;
	/**
	 * ViewSwitcher  to switch between potView and textView
	 */
	private ViewSwitcher vsInfo;

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
		potView = (PotView) findViewById(R.id.potView);
		vsInfo = (ViewSwitcher) findViewById(R.id.vs_info);
		vsInfo.getInAnimation().setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
		vsInfo.getOutAnimation().setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
		dealerFlag = (TextView) findViewById(R.id.tx_dealerFlag);
		cardContainer = (FrameLayout) findViewById(R.id.fl_cardview);
		profileContainer = findViewById(R.id.frame_iv_profile);
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
	 *
	 * @param profileImage player image
	 */
	public void setProfileImage(final Bitmap profileImage) {
		this.profileImage.setImageBitmap(profileImage);
	}

	/**
	 * Show/Hide the dealer tag indicator
	 *
	 * @param isDealer represent if this PlayerView is a dealer or not
	 */
	public void setDealer(boolean isDealer) {
		dealerFlag.setVisibility(isDealer ? VISIBLE : GONE);
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
		int viewToDisplay;
		if (player.getBet() > 0) {
			potView.setAmount(player.getBet());
			viewToDisplay = 1;
		} else {
			txInfo.setText(player.getName());
			viewToDisplay = 0;
		}
		updateViewSwitcherInfo(viewToDisplay);
	}

	/**
	 * set the PlayerView state
	 *
	 * @param playerState Player's {@link com.hmrocket.poker.Player.PlayerState state}
	 */
	public void setState(Player.PlayerState playerState) {
		// TODO support player_view_circle
		boolean circle = false;
		if (playerState == null)
			profileImage.setBackground(null);
			// getColorStateList(id) is deprecated in M
		else switch (playerState) {
			case FOLD:
				if (circle) {
					// profileImage.setBackgroundColor(getResources().getColor(R.color.fold_selector));
					// ColorStateList colorStateList = ContextCompat.getColorStateList(getContext(), R.color.fold_selector);
				} else {
					setEnabled(false);
					// we don't like support state, that's why we won't use ColorStateList as color
					profileImage.setColorFilter(
							//colorStateList.getColorForState(profileImage.getBackground().getState(), colorStateList.getDefaultColor())
							ContextCompat.getColor(getContext(), R.color.fold)
							, PorterDuff.Mode.MULTIPLY);
				}
				break;
			case RAISE:
				if (circle) {
					//profileImage.setBackgroundTintList(getResources().getColorStateList(R.color.raise_selector, null));
				} else {
					//profileImage.setBackgroundColor(getResources().getColor(R.color.raise_selector));
					// we don't like support state, that's why we won't use ColorStateList as color
					profileImage.setColorFilter(
							//colorStateList.getColorForState(profileImage.getBackground().getState(), colorStateList.getDefaultColor())
							ContextCompat.getColor(getContext(), R.color.raise)
							, PorterDuff.Mode.MULTIPLY);
				}
				break;
			case ALL_IN:
				if (circle) {
					// I don't care about theme that why isn't getContext().getTheme()
					//profileImage.setBackgroundTintList(getResources().getColorStateList(R.color.allin_selector, null));
				} else {
					profileImage.setColorFilter(
							//colorStateList.getColorForState(profileImage.getBackground().getState(), colorStateList.getDefaultColor())
							ContextCompat.getColor(getContext(), R.color.allin)
							, PorterDuff.Mode.MULTIPLY);
				}
				break;
			case CALL:
			case CHECK:
				if (circle) {
					//profileImage.setBackgroundTintList(getResources().getColorStateList(R.color.call_selector, getContext().getTheme()));
				} else {
					//profileImage.setBackgroundColor(getResources().getColor(R.color.call_selector));
					profileImage.setColorFilter(
							//colorStateList.getColorForState(profileImage.getBackground().getState(), colorStateList.getDefaultColor())
							ContextCompat.getColor(getContext(), R.color.call)
							, PorterDuff.Mode.MULTIPLY);
				}
				break;
			case ACTIVE:
				// remove filter
				setEnabled(true);
				profileImage.setColorFilter(null);
				//profileImage.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.frame_player));
				break;
			default:
				profileImage.setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
				setEnabled(false);
				break;
		}

	}

	/**
	 * switch between Info text and info bet (PotView)
	 * @param position the view position to display
	 */
	private void updateViewSwitcherInfo(int position) {
		if (position > vsInfo.getDisplayedChild()) {
			vsInfo.showNext();
		} else if (position < vsInfo.getDisplayedChild())
			vsInfo.showPrevious();
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).setEnabled(enabled);
		}
		if (enabled)
			profileImage.setAlpha(1f);
		else profileImage.setAlpha(.3f);

		cardView1.setEnabled(enabled);
		cardView2.setEnabled(enabled);
	}

	/**
	 * display info about player
	 *
	 * @param stringResId String resource id to show as info text
	 */
	public void setInfo(int stringResId) {
		txInfo.setText(stringResId);
		updateViewSwitcherInfo(0);
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
	 * @param faceDown tru if the card are face down, false if card is showing
	 */
	public void showCards(boolean faceDown) {
		cardView1.facedown(faceDown);
		cardView2.facedown(faceDown);
	}

	/**
	 * set the CardView of PlayerView face down
	 * @param hand two cards
	 */
	public void setHand(Hand hand) {
		if (hand != null) {
			cardContainer.setVisibility(VISIBLE);
			cardView1.facedown(true);
			cardView1.setCard(hand.getCard1());
			cardView2.facedown(true);
			cardView2.setCard(hand.getCard2());
		} else {
			// hide card container to adjust layout size and center
			cardContainer.setVisibility(GONE);
			cardView1.reset();
			cardView2.reset();
		}
	}

	/**
	 * reset the PlayerView
	 */
	public void reset() {
		// hide card container to adjust layout size and center
		cardContainer.setVisibility(GONE);
		cardView1.reset();
		cardView2.reset();
		txCash.setText("");
		txInfo.setText("");
		profileImage.setImageDrawable(null);
	}

	/**
	 * Highlight PlayerView onTurn
	 *
	 * @param highlight true will highlight the PlayerView, default state otherwise
	 */
	public void setHighlight(boolean highlight) {
		if (highlight)
			profileContainer.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.frame_player));
		else
			profileContainer.setBackground(null);
	}

	/**
	 * Enable/disable PlayerView's Cards
	 *
	 * @param enableCard1 false will gray the PlayerView Card1, default state otherwise
	 * @param enableCard2 false will gray the PlayerView Card2, default state otherwise
	 */
	public void enableCards(boolean enableCard1, boolean enableCard2) {
		cardView1.setEnabled(enableCard1);
		cardView2.setEnabled(enableCard2);
	}

}
