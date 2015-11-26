package com.hmrocket.magicpoker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hmrocket.magicpoker.R;
import com.hmrocket.magicpoker.Util;
import com.hmrocket.magicpoker.fragment.DataFragment;
import com.hmrocket.magicpoker.fragment.RaiseDialog;
import com.hmrocket.magicpoker.view.TableView;
import com.hmrocket.poker.GameEvent;
import com.hmrocket.poker.HumanPlayer;
import com.hmrocket.poker.Player;
import com.hmrocket.poker.RoundPhase;
import com.hmrocket.poker.Turn;
import com.hmrocket.poker.ai.bot.SafeBot;
import com.hmrocket.poker.card.CommunityCards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameActivity extends Activity implements View.OnClickListener, RaiseDialog.OnRaiseListener, GameEvent {

	private final List<Player> PLAYERS = new ArrayList(Arrays.asList(
			new HumanPlayer("Mhamed", (long) 13e6, (long) 150), //1
			new SafeBot("Kais", (long) 72e6, (long) 100), //0
			new SafeBot("Kevin", 450633L, (long) 200),//3
			new SafeBot("Itachi", (long) 10e6, 200),//4
			new SafeBot("Yassin", (long) 4e6, 200),//5
			new SafeBot("San", (long) 1e6, 50),//6
			new SafeBot("Elhem", (long) 480e3, 100),//7
			new SafeBot("Sof", (long) 100e3, 200),//8
			new SafeBot("M", (long) 100e3, 200)//9
	));
	
	private Button btnController[];
	private DataFragment dataFragment;
	private RaiseDialog raiseDialog;
	private TableView tableView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);


		// Hide Navigation and status bar
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
				| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


		// set Layout controller
		btnController = new Button[5];
		btnController[0] = (Button) findViewById(R.id.btn_allin);
		btnController[1] = (Button) findViewById(R.id.btn_raise);
		btnController[2] = (Button) findViewById(R.id.btn_call);
		btnController[3] = (Button) findViewById(R.id.btn_fold);
		btnController[4] = (Button) findViewById(R.id.btn_start_skip_info);
		for (Button btn : btnController) {
			btn.setOnClickListener(this);
		}
		tableView = (TableView) findViewById(R.id.tableView);

		// attach fragment data to the activity
		dataFragment = (DataFragment) getFragmentManager().findFragmentByTag("game");
		if (dataFragment == null) {
			dataFragment = DataFragment.newInstance(9, 2);
			getFragmentManager().beginTransaction().add(dataFragment, "game").commit();
			// DEBUG
			//tableView.populate();
		}


	}

	@Override
	protected void onResume() {
		super.onResume();
	}


	@Override
	public void onClick(View v) {
		// TODO handle onClick
		switch (v.getId()) {
			case R.id.btn_allin:
				dataFragment.getPlayer().allIn();
				break;
			case R.id.btn_raise:
				// the Dialog fragment should represent just he add value no more, change onRaiseDialogResult to money added to the table
				//  show RaiseDialog
				raiseDialog = RaiseDialog.newInstance(dataFragment.getPlayer(), dataFragment.getTurn());
				raiseDialog.show(getFragmentManager(), null);
				break;
			case R.id.btn_call:
				// call or check
				dataFragment.getPlayer().call(dataFragment.getTurn().getAmountToContinue());
				break;
			case R.id.btn_fold:
				dataFragment.getPlayer().fold();
				break;
			case R.id.btn_start_skip_info:
				// XXX DEBUG
				for (int i = 0; i < PLAYERS.size(); i++)
					dataFragment.getTable().addPlayer(PLAYERS.get(i), i);
				// XXX DEBUG
				dataFragment.startGame();
				break;
		}
	}

	@Override
	public void onRaiseDialogResult(long raiseValue) {
		// disable panel controller
		for (Button btn : btnController)
			btn.setEnabled(false);

		// dispatch HumanPlayer action
		// the method is called on raise but the user can be called or checked or fold..
		dataFragment.getPlayer().autoMove(raiseValue, dataFragment.getTurn());
	}

	@Override
	public void gameEnded() {
		// ask the player if he would like to continue playing
		btnController[4].setEnabled(true);
		btnController[4].setText(R.string.start);
	}

	@Override
	public void playerBusted(Set<Player> player) {
		// TODO remove Player from TableView too, if it's human request that he buy in or purchase chips
	}

	@Override
	public void gameWinners(boolean last, Set<Player> winners) {
		// TODO animate last winning players
		// TODO update PlayerView
	}

	@Override
	public void onPreTurn(Player player, Turn turn) {
		if (player instanceof HumanPlayer) {
			// activate only possible commands
			btnController[0].setEnabled(true); //AllIn
			btnController[3].setEnabled(true); //Fold
			if (player.getCash() + player.getBet() > turn.getMinBet()) {
				// Raise/Call cmd enabled only if the player has above the minBet (if it's equal then only allin/Fold activated)
				btnController[1].setEnabled(true); // Raise
				btnController[2].setEnabled(true); // Call
			}
			btnController[4].setText(Util.formatNumber(turn.getAmountToContinue() - player.getBet()));
			//
		}
		// TODO animate PlayerView
	}

	@Override
	public void onTurnEnded(Player player) {
		// TODO update and animate PlayerView to reflect action
		if (player instanceof HumanPlayer) {
			// disable panel control
			btnController[0].setEnabled(false); //AllIn
			btnController[1].setEnabled(false); // raise
			btnController[2].setEnabled(false); // call
			btnController[3].setEnabled(false); //Fold
			if (player.isOut()) {
				// TODO implement skip by disabling animation and task delays
				btnController[4].setEnabled(true);
				btnController[4].setText(R.string.skip);
			} else {
				// remove info (amount to add to continue
				btnController[4].setText(null);
			}
		}
	}

	@Override
	public void onRound(RoundPhase roundPhase) {
		// TODO animate card dealing on PRE_FLOP and Toast roundPhase name
		if (roundPhase == RoundPhase.PRE_FLOP) {
			tableView.populate(PLAYERS);
		}
	}

	@Override
	public void onBlindPosted(Player smallBlind, Player bigBlind) {
		// TODO post raise same work as TurnEnded
	}

	@Override
	public void onShowdown(Set<Player> potentialWinners) {
		// TODO flip potentialWinners cards
	}

	@Override
	public void onPotChanged(long potValue) {
		// TODO Update PotView
	}

	@Override
	public void onCommunityCardsChange(CommunityCards communityCards) {
		// TODO flip shared cards
	}
}
