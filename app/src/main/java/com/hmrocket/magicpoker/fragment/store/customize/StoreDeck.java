package com.hmrocket.magicpoker.fragment.store.customize;

import android.support.annotation.StringRes;

import com.hmrocket.magicpoker.R;

/**
 * Created by hmrocket on 31/10/2016
 */

class StoreDeck {

	final int[] deckPeek;
	@StringRes
	final int titleId;
	final float price;

	public StoreDeck(int[] deckPeek, int titleId, float price) {
		this.deckPeek = deckPeek;
		this.titleId = titleId;
		this.price = price;
	}

	static StoreDeck[] getAllDecks() {
		int deckIds[][] = new int[][]{
				{R.mipmap.ca, R.mipmap.ck, R.mipmap.cq, R.mipmap.cj, R.mipmap.c10},
				{R.mipmap.da, R.mipmap.dk, R.mipmap.dq, R.mipmap.dj, R.mipmap.d10}
		};
		StoreDeck[] storeDecks = new StoreDeck[deckIds.length];
		for (int i = 0; i < storeDecks.length; i++) {
			storeDecks[i] = new StoreDeck(deckIds[i], R.string.title_activity_store, 10);
		}
		return storeDecks;
	}
}
