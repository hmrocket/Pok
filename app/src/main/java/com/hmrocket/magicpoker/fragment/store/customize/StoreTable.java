package com.hmrocket.magicpoker.fragment.store.customize;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.hmrocket.magicpoker.R;

/**
 * Created by hmrocket on 31/10/2016
 */

class StoreTable {
	@DrawableRes
	final int tableId;
	@StringRes
	final int titleId;
	final int price;

	StoreTable(@DrawableRes int tableId, @StringRes int titleId, int price) {
		this.tableId = tableId;
		this.titleId = titleId;
		this.price = price;
	}

	/**
	 * @return all table defined in the app
	 */
	static StoreTable[] getAllTables() {
		int[] tableIds = new int[]{R.mipmap.table_flat_blue, R.mipmap.table_skull_black, R.mipmap.table_skull_green, R.mipmap.table_pro};
		StoreTable[] tables = new StoreTable[tableIds.length];

		for (int i = 0; i < tableIds.length; i++) {
			tables[i] = new StoreTable(tableIds[i], R.string.title_activity_store, 10);
		}

		return tables;
	}
}
