package com.hmrocket.magicpoker.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
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
		init();
	}

	protected void init() {
		cardId = -1;
		// by default show the card facedown
		facedown(true);
	}

	/**
	 * Define whether the back or the front of CardView is shown
	 *
	 * @param facedown true show card face down, show card face up otherwise
	 */
	public void facedown(boolean facedown) {
		// do not update CardView if the states match
		if (facedown == this.faceDown && cardId != -1)
			return;
		else
			this.faceDown = facedown;

		if (!facedown) {
			if (this.cardId != -1) {
				// setImageResource ==> reading and decoding on the UI thread
				// setImageDrawable ==> just the setting is on UI thread
				setImageResource(cardId);
			} else {
				setImageBitmap(null);
			}
		} else {
			// just show the back cover of the card
			setImageResource(R.mipmap.backcover);
		}
	}

	public CardView(Context context, AttributeSet attributeset) {
		super(context, attributeset);
		init();
	}

	public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public CardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public void setCard(Card card) {
		cardId = Util.getCardImageId(card);
	}

	/**
	 * Reset the CardView
	 */
	public void reset() {
		init();
	}
}

