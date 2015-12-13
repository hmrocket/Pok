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

import java.util.HashMap;

/**
 * @since 09/Dec/2015 - mhamed
 */
public class SoundManager {

	private static final int MAX_STEAM = 2;
	private static final int[] RAISE_SOUND_RES = new int[]{R.raw.chipsstack1, R.raw.chipsstack2, R.raw.chipsstack3, R.raw.chipsstack4, R.raw.chipsstack5, R.raw.chipsstack6};

	/**
	 * Represent the index of the current RAISE_SOUND_RES played/will be player
	 * Note: we pick different raise sound every time
	 */
	private int raiseSoundIndex;

	private SoundPool soundPool;
	/**
	 * Map resource id to it's the Sound id after it load
	 */
	private HashMap<Integer, Integer> mapSoundId;
	private float rate;


	private SoundManager() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			soundPool = buildSoundPool();
		} else {
			soundPool = buildBeforeAPI21();
		}
		// rate normal playback
		rate = 1;
		raiseSoundIndex = -1;
		mapSoundId = new HashMap<>();
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
				.setMaxStreams(MAX_STEAM)
				.setAudioAttributes(audioAttributes)
				.build();
	}

	@SuppressWarnings("deprecation")
	public SoundPool buildBeforeAPI21() {
		return new SoundPool(MAX_STEAM, AudioManager.STREAM_MUSIC, 0);
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
		int[] soundsRes = new int[]{R.raw.cardfan1, // shuffle (game start)
				R.raw.win_wohoo, R.raw.card_flip, // Human player win (game end), showdown
				R.raw.cardslide1, R.raw.cardslide2, R.raw.cardslide3, R.raw.cardslide4, R.raw.cardslide5, // community card
				R.raw.chipshandle5_allin, R.raw.check, R.raw.chipshandle6_call, R.raw.fold_weaponswipe01, // 4 move
				R.raw.chipsstack1, R.raw.chipsstack2, R.raw.chipsstack3, R.raw.chipsstack4, R.raw.chipsstack5, R.raw.chipsstack6, // raise move
				R.raw.levelup, // progress
		};
		for (int res : soundsRes) {
			mapSoundId.put(res, soundPool.load(context, res, 1));
		}
	}

	/**
	 * method to unload game music from the ram
	 *
	 * @param context the context of the app need to unload the audio resource
	 */
	public void unloadGameMusic(Context context) {
		for (int id : mapSoundId.values()) {
			soundPool.unload(id);
		}
		mapSoundId.clear();
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
				soundPool.play(mapSoundId.get(R.raw.fold_weaponswipe01), 1f, 1f, 1, 0, rate);
				break;
			case RAISE:
				soundPool.play(mapSoundId.get(getRaiseSoundRes()), 1f, 1f, 1, 0, rate);
				break;
			case CALL:
				soundPool.play(mapSoundId.get(R.raw.chipshandle6_call), 1f, 1f, 1, 0, rate);
				break;
			case CHECK:
				soundPool.play(mapSoundId.get(R.raw.check), 1f, 1f, 1, 0, rate);
				break;
			case ALL_IN:
				soundPool.play(mapSoundId.get(R.raw.chipshandle5_allin), 1f, 1f, 1, 0, rate);
				break;
			default:
				break;
		}
	}

	private int getRaiseSoundRes() {
		raiseSoundIndex++;
		if (raiseSoundIndex == RAISE_SOUND_RES.length)
			raiseSoundIndex = 0;
		return RAISE_SOUND_RES[raiseSoundIndex];
	}

	/**
	 * play a winning sound
	 */
	public void playWinSound() {
		soundPool.play(mapSoundId.get(R.raw.win_wohoo), 1f, 1f, 1, 0, rate);
	}

	/**
	 * play a sound specific to a round
	 *
	 * @param roundPhase play sound only if it's among this values FLOP, TURN, RIVER
	 */
	public void playCardDraw(RoundPhase roundPhase) {
		switch (roundPhase) {
			case FLOP:
				soundPool.play(mapSoundId.get(R.raw.cardslide1), 1f, 1f, 1, 0, rate);
				soundPool.play(mapSoundId.get(R.raw.cardslide2), 1f, 1f, 1, 0, rate);
				soundPool.play(mapSoundId.get(R.raw.cardslide3), 1f, 1f, 1, 0, rate);
				break;
			case TURN:
				soundPool.play(mapSoundId.get(R.raw.cardslide4), 1f, 1f, 1, 0, rate);
				break;
			case RIVER:
				soundPool.play(mapSoundId.get(R.raw.cardslide5), 1f, 1f, 1, 0, rate);
				break;
			default:
				// no sound on SHOWDOWN and PRE_FLOP
				break;

		}
	}

	/**
	 * play Level up sound
	 */
	public void playLevelUp() {
		soundPool.play(mapSoundId.get(R.raw.levelup), 1f, 1f, 1, 0, rate);
	}

	/**
	 * play shuffle cards sound
	 */
	public void playShuffleCards() {
		soundPool.play(mapSoundId.get(R.raw.cardfan1), 1f, 1f, 1, 0, rate);
	}

	/**
	 * play flip cards sound
	 */
	public void playCardFlip() {
		soundPool.play(mapSoundId.get(R.raw.card_flip), 1f, 1f, 1, 0, rate);
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
