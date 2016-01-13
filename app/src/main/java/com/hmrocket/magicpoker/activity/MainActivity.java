package com.hmrocket.magicpoker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hmrocket.magicpoker.R;

public class MainActivity extends Activity implements View.OnClickListener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btn_continueGame).setOnClickListener(this);
		findViewById(R.id.btn_startGame).setOnClickListener(this);
		findViewById(R.id.btn_store).setOnClickListener(this);
		findViewById(R.id.btn_pokerGuide).setOnClickListener(this);
		findViewById(R.id.btn_about).setOnClickListener(this);

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_continueGame:
				break;
			case R.id.btn_startGame:
				startActivity(new Intent(this, GameActivity.class));
				break;
			case R.id.btn_store:
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
