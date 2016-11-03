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
		int[] tableTitleIds = new int[]{R.string.table_blue_title, R.string.table_skull_title, R.string.table_green_title, R.string.table_pro_title};
		// blue, skull, greenLight, pro
		int[] tablePriceIds = new int[]{35, 100, 65, 15};
		StoreTable[] tables = new StoreTable[tableIds.length];

		for (int i = 0; i < tableIds.length; i++) {
			tables[i] = new StoreTable(tableIds[i], tableTitleIds[i], tablePriceIds[i]);
		}

		return tables;
	}
}
