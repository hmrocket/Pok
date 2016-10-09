package com.hmrocket.magicpoker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hmrocket.magicpoker.R;
import com.hmrocket.magicpoker.data.AppPreference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private AppPreference preference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btn_continueGame).setOnClickListener(this);
		findViewById(R.id.btn_startGame).setOnClickListener(this);
		findViewById(R.id.btn_multiplayer).setOnClickListener(this);
		findViewById(R.id.btn_store).setOnClickListener(this);
		findViewById(R.id.btn_pokerGuide).setOnClickListener(this);
		findViewById(R.id.btn_about).setOnClickListener(this);

		preference = new AppPreference(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_continueGame:
				break;
			case R.id.btn_startGame:
				startActivity(GameActivity.newIntent(this));
				break;
			case R.id.btn_multiplayer:
				startActivity(GameActivity.newIntent(this, preference.getHumanCount()));
				break;
			case R.id.btn_store:
				startActivity(new Intent(this, StoreActivity.class));
				break;
			case R.id.btn_pokerGuide:
				break;
			case R.id.btn_about:
				break;
			default:
				break;
		}
	}
}
