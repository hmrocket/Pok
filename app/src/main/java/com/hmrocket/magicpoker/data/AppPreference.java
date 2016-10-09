package com.hmrocket.magicpoker.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hmrocket on 16/01/2016.
 */
public class AppPreference {

	private static final String KEY_MULTI_COUNT = "human_count";
	private SharedPreferences preferences;

	public AppPreference(Context context) {
		this.preferences = context.getSharedPreferences("app_config", Context.MODE_PRIVATE);
	}

	public int getHumanCount() {
		return preferences.getInt(KEY_MULTI_COUNT, 2);
	}

	/**
	 * @param multiHumanPref set the number of humans for multi players
	 */
	public void setHumanCount(int multiHumanPref) {
		preferences.edit().putInt(KEY_MULTI_COUNT, multiHumanPref).apply();
	}
}
