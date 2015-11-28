package com.hmrocket.magicpoker.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.hmrocket.magicpoker.R;
import com.hmrocket.magicpoker.Util;
import com.hmrocket.poker.card.Card;

/**
 * @since 10/Nov/2015 - mhamed
 */
public class CardView extends ImageView {

	private int cardId;
	private boolean faceDown;

	public CardView(Context context) {
		super(context);
		init(true);
	}

	/**
	 * set the default state of the CardView which is no image card and facedown is set to true
	 *
	 * @param facedown false CardView by default will be show, otherwise backcover will be shown by default
	 */
	protected void init(boolean facedown) {
		cardId = -1;
		// by default No card is shown and show the card facedown
		faceDown = facedown;
	}

	public CardView(Context context, AttributeSet attributeset) {
		super(context, attributeset);
		init(true);
	}

	public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(true);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public CardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(true);
	}

	/**
	 * Define whether the back or the front of CardView is shown
	 *
	 * @param facedown true show card face down, show card face up otherwise
	 */
	public void facedown(boolean facedown) {
		// do not update CardView if the states match or there's no card id
		if (facedown == this.faceDown) {
			return;
		}
		this.faceDown = facedown;
		if (cardId == -1)
			return; // no card set yet

		renderCard();
	}

	/**
	 * if facedown is off set the card, otherwise the backcover
	 */
	private void renderCard() {
		if (!faceDown) {
			// setImageResource ==> reading and decoding on the UI thread
			// setImageDrawable ==> just the setting is on UI thread
			setImageResource(cardId);
		} else {
			// just show the back cover of the card
			setImageResource(R.mipmap.backcover);
		}
	}

	/**
	 * Set a card, setCard(null) will remove card it will show neither back cover neither the card
	 *
	 * @param card card to set
	 */
	public void setCard(Card card) {
		if (card == null) // if the card is null show null drawable
			reset();
		else {
			// depending on faceDown variable either show backcover or the card itself
			int id = Util.getCardImageId(card);
			if (id != cardId) {
				cardId = id;
				renderCard();
			}
		}
	}

	/**
	 * Reset the CardView by removing the image but keep faceDown attribute
	 */
	public void reset() {
		cardId = -1;
		setImageBitmap(null);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();

		Bundle state = new Bundle();
		state.putParcelable("PARENT", superState);
		state.putInt("card_id", cardId);
		state.putBoolean("facedown", faceDown);
		return state;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Bundle savedState = (Bundle) state;

		Parcelable superState = savedState.getParcelable("PARENT");
		super.onRestoreInstanceState(superState);
		cardId = savedState.getInt("card_id", -1);
		faceDown = savedState.getBoolean("facedown", true);
		if (cardId != -1)
			renderCard();
	}
}

