package com.hmrocket.magicpoker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.NonNull;

import com.hmrocket.poker.Player;
import com.hmrocket.poker.RoundPhase;

/**
 * @since 09/Dec/2015 - mhamed
 */
public class SoundManager {

	private static final int SOUND_ID_CARD_FLIP = 0;
	// Action sound
	private static final int SOUND_ID_FOLD = 1;
	private static final int SOUND_ID_RAISE = 2;
	private static final int SOUND_ID_CALL = 3;
	private static final int SOUND_ID_CHECK = 4;
	private static final int SOUND_ID_ALL_IN = 5;
	// CommunityCard sound
	private static final int SOUND_ID_DRAW_FLOP = 6;
	private static final int SOUND_ID_DRAW_TURN = 7;
	private static final int SOUND_ID_DRAW_RIVER = 8;

	private static final int SOUND_ID_WIN = 9;

	private SoundPool soundPool;
	private float rate;


	private SoundManager() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			soundPool = buildSoundPool();
		} else {
			soundPool = buildBeforeAPI21();
		}
		// rate normal playback
		rate = 1;
	}

	/**
	 * Create a SoundManager
	 *
	 * @return SoundManager
	 */
	public static SoundManager create() {
		return new SoundManager();
	}

	/**
	 * Create a SoundManager and load game streams sound file
	 *
	 * @param context context of the app
	 * @return SoundManager
	 */
	public static SoundManager createAndLoad(Context context) {
		SoundManager soundManager = new SoundManager();
		soundManager.loadGameMusic(context);
		// TODO load play rate based on the game speed rate
		return soundManager;
	}

	/**
	 * method to load game music into the ram for fast play
	 *
	 * @param context the context of the app need to load the audio resource
	 */
	public void loadGameMusic(Context context) {
		// TODO add sound resource
		int[] soundsRes = new int[]{};
		for (int res : soundsRes) {
			soundPool.load(context, res, 1);
		}
		throw new UnsupportedOperationException("No yet implemented");
	}

	/**
	 * construct the SoundManager  depending on the api version of the device
	 *
	 * @return SoundPool of max 9 stream, for game usage specificlly action and event type of sonification
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private SoundPool buildSoundPool() {
		AudioAttributes audioAttributes = new AudioAttributes.Builder()
				.setUsage(AudioAttributes.USAGE_GAME)
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
				.build();

		return new SoundPool.Builder()
				.setMaxStreams(9)
				.setAudioAttributes(audioAttributes)
				.build();
	}

	@SuppressWarnings("deprecation")
	public SoundPool buildBeforeAPI21() {
		return new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	}


	public void adjustSound(Activity activity) {
		// AudioManager audio settings for adjusting the volume
		AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
		float actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actVolume / maxVolume;

		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	/**
	 * play a sound effect based on action
	 *
	 * @param playerState the player state after turn end
	 */
	public void playActionSound(@NonNull Player.PlayerState playerState) {
		switch (playerState) {
			case FOLD:
				//int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate
				soundPool.play(SOUND_ID_FOLD, 1f, 1f, 1, 0, rate);
				break;
			case RAISE:
				soundPool.play(SOUND_ID_RAISE, 1f, 1f, 1, 0, rate);
				break;
			case CALL:
				soundPool.play(SOUND_ID_CALL, 1f, 1f, 1, 0, rate);
				break;
			case CHECK:
				soundPool.play(SOUND_ID_CHECK, 1f, 1f, 1, 0, rate);
				break;
			case ALL_IN:
				soundPool.play(SOUND_ID_ALL_IN, 1f, 1f, 1, 0, rate);
				break;
			default:
				break;
		}
	}

	/**
	 * play a winning sound
	 */
	public void playWinSound() {
		soundPool.play(SOUND_ID_WIN, 1f, 1f, 1, 0, rate);
	}

	/**
	 * play a sound specific to a round
	 *
	 * @param roundPhase play sound only if it's among this values FLOP, TURN, RIVER
	 */
	public void playCardDraw(RoundPhase roundPhase) {
		switch (roundPhase) {
			case FLOP:
				soundPool.play(SOUND_ID_DRAW_FLOP, 1f, 1f, 1, 0, rate);
				break;
			case TURN:
				soundPool.play(SOUND_ID_DRAW_TURN, 1f, 1f, 1, 0, rate);
				break;
			case RIVER:
				soundPool.play(SOUND_ID_DRAW_RIVER, 1f, 1f, 1, 0, rate);
				break;
			default:
				// no sound on SHOWDOWN and PRE_FLOP
				break;

		}
	}

	/**
	 * play flip card sound
	 */
	public void playCardFlip() {
		soundPool.play(SOUND_ID_CARD_FLIP, 1f, 1f, 1, 0, rate);
	}

	/**
	 * get the rate of sound playing
	 *
	 * @return playback rate (1.0 = normal playback, range 0.5 to 2.0)
	 */
	public float getRate() {
		return rate;
	}

	/**
	 * Set the rate of sound playing
	 *
	 * @param rate playback rate (1.0 = normal playback, range 0.5 to 2.0)
	 */
	public void setRate(float rate) {
		this.rate = rate;
	}
}
