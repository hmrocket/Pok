package com.hmrocket.magicpoker.fragment.store.customize;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Created by hmrocket on 31/10/2016
 */

class StoreBackground {
	@DrawableRes
	final int backgroundId;
	@StringRes
	final int titleId;
	final float price;

	public StoreBackground(int backgroundId, int titleId, float price) {
		this.backgroundId = backgroundId;
		this.titleId = titleId;
		this.price = price;
	}
}
