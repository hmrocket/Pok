package com.hmrocket.magicpoker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.hmrocket.magicpoker.Util;
import com.hmrocket.poker.card.Card;

/**
 * @since 10/Nov/2015 - mhamed
 */
public class CardView extends ImageView {

	private Card card;

	public CardView(Context context) {
		super(context);
	}

	public CardView(Context context, AttributeSet attributeset) {
		super(context, attributeset);
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
		if (this.card != null) {
			// setImageResource ==> reading and decoding on the UI thread
			// setImageDrawable ==> just the setting is on UI thread
			setImageResource(Util.getCardImageId(card));
		} else {
			setImageBitmap(null);
		}
	}

}

