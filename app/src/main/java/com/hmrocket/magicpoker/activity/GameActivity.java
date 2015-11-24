package com.hmrocket.magicpoker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hmrocket.magicpoker.R;
import com.hmrocket.magicpoker.fragment.DataFragment;
import com.hmrocket.magicpoker.fragment.RaiseDialog;
import com.hmrocket.poker.GameEvent;
import com.hmrocket.poker.HumanPlayer;
import com.hmrocket.poker.Player;
import com.hmrocket.poker.RoundPhase;
import com.hmrocket.poker.card.CommunityCards;

import java.util.Set;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameActivity extends Activity implements View.OnClickListener, RaiseDialog.OnRaiseListener, GameEvent {

	private Button btnController[];
	private DataFragment dataFragment;
	private RaiseDialog raiseDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// attach fragment data to the activity
		dataFragment = (DataFragment) getFragmentManager().findFragmentByTag("game");
		if (dataFragment == null) {
			dataFragment = DataFragment.newInstance(9, 20);
		}

		setContentView(R.layout.activity_game);


		// Hide Navigation and status bar
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
				| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


		// set Layout controller
		btnController = new Button[4];
		btnController[0] = (Button) findViewById(R.id.btn_allin);
		btnController[1] = (Button) findViewById(R.id.btn_raise);
		btnController[2] = (Button) findViewById(R.id.btn_call);
		btnController[3] = (Button) findViewById(R.id.btn_fold);
		for (Button btn : btnController) {
			btn.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO handle onClick
		switch (v.getId()) {
			case R.id.btn_allin:
				break;
			case R.id.btn_raise:
				//  show RaiseDialg
				raiseDialog.show(getFragmentManager(), null);
				break;
			case R.id.btn_call:
				break;
			case R.id.btn_fold:
				break;
		}
	}

	@Override
	public void onRaiseConfirmed(long raiseValue) {
		// disable panel controller
		for (Button btn : btnController)
			btn.setEnabled(false);

		// TODO dispatch HumanPlayer action
	}

	@Override
	public void gameEnded() {

	}

	@Override
	public void playerBusted(Set<Player> player) {

	}

	@Override
	public void gameWinners(boolean last, Set<Player> winners) {

	}

	@Override
	public void onPreTurn(Player player, long amountToContinue) {
		if (player instanceof HumanPlayer) {
			// activate controller panel
			// TODO activate only possible commands
			for (Button btn : btnController)
				btn.setEnabled(true);
		}
	}

	@Override
	public void onTurnEnded(Player player) {

	}

	@Override
	public void onRound(RoundPhase roundPhase) {

	}

	@Override
	public void onBlindPosted(Player smallBlind, Player bigBlind) {

	}

	@Override
	public void onShowdown(Set<Player> potentialWinners) {

	}

	@Override
	public void onPotChanged(long potValue) {

	}

	@Override
	public void onCommunityCardsChange(CommunityCards communityCards) {

	}
}
