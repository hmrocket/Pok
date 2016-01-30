package com.hmrocket.magicpoker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hmrocket.magicpoker.R;

/**
 * Full Screen Splash activity, you can load some data here
 */
public class SplashActivity extends Activity {

    /**
	 * The number of milliseconds to wait before launching MainActivity
	 */
	private static final int AUTO_MAIN_DELAY_MILLIS = 500;
	private final Runnable launchMain = new Runnable() {
		@Override
		public void run() {
			// Show MainActivity
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
		}
	};

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);


		Handler mHideHandler = new Handler();
		mHideHandler.postDelayed(launchMain, AUTO_MAIN_DELAY_MILLIS);
	}


}
