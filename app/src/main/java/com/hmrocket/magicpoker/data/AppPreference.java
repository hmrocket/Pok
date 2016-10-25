package com.hmrocket.magicpoker.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hmrocket.magicpoker.R;

/**
 * Created by hmrocket on 16/01/2016
 */
@SuppressWarnings("WeakerAccess")
public class AppPreference {

	private static final String KEY_LEVEL = "level";
	// public static final String APP_CONFIG_NAME = "app_config";

	private final String KEY_MULTI_COUNT;
	private final String KEY_SOUND_EFFECT;
	private final String KEY_BACKGROUND_SOUND;
	private final String KEY_USERNAME;
	private final String DEFAULT_USERNAME;
	private SharedPreferences preferences;

	public AppPreference(Context context) {
		this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
		// shared keys
		KEY_BACKGROUND_SOUND = context.getString(R.string.key_backgroundSound);
		KEY_SOUND_EFFECT = context.getString(R.string.key_soundEffect);
		KEY_MULTI_COUNT = context.getString(R.string.key_humanCount);
		KEY_USERNAME = context.getString(R.string.key_username);
		DEFAULT_USERNAME = context.getString(R.string.pref_default_username);
	}

	/**
	 * @return the number of humans for multi players
	 */
	public int getHumanCount() {
		// human count default is 2 (also defined in the xml)
		return preferences.getInt(KEY_MULTI_COUNT, 2);
	}

	public void levelUp() {
		setLevel(1 + getLevel());
	}

	public int getLevel() {
		return preferences.getInt(KEY_LEVEL, 1);
	}

	/**
	 * @param level level reached by the user
	 */
	public void setLevel(int level) {
		preferences.edit().putInt(KEY_LEVEL, level).apply();
	}

	/**
	 * @return true if the sound effect is active (i.e betting ..etc)
	 */
	public boolean isSoundEffectEnabled() {
		return preferences.getBoolean(KEY_SOUND_EFFECT, true);
	}

	/**
	 * @return true if the sound effect is active (i.e betting ..etc)
	 */
	public boolean isBackgroundSound() {
		return preferences.getBoolean(KEY_BACKGROUND_SOUND, false);
	}

	public String getUsername() {
		return preferences.getString(KEY_USERNAME, DEFAULT_USERNAME);
	}
}
