package com.hmrocket.magicpoker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hmrocket.magicpoker.AnalyticsTrackers;
import com.hmrocket.magicpoker.R;
import com.hmrocket.magicpoker.Util;
import com.hmrocket.magicpoker.fragment.DataFragment;
import com.hmrocket.magicpoker.fragment.RaiseDialog;
import com.hmrocket.magicpoker.view.PlayerView;
import com.hmrocket.magicpoker.view.TableView;
import com.hmrocket.poker.GameEvent;
import com.hmrocket.poker.HumanPlayer;
import com.hmrocket.poker.Player;
import com.hmrocket.poker.RoundPhase;
import com.hmrocket.poker.Turn;
import com.hmrocket.poker.card.CommunityCards;
import com.hmrocket.poker.card.HandScoreCalculator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameActivity extends Activity implements View.OnClickListener, RaiseDialog.OnRaiseListener, GameEvent {

	private static final String KEY_HUMAN_COUNT = "humanCount";

	private Button btnController[];
	private DataFragment dataFragment;
	private RaiseDialog raiseDialog;
	private TableView tableView;
	/**
	 * if there's more of one human player this will be changed to true
	 */
	private boolean humanCardFaceDown = false;


	public static Intent newIntent(Context context) {
		return new Intent(context, GameActivity.class);
	}

	public static Intent newIntent(Context context, int humanCount) {
		Intent i = new Intent(context, GameActivity.class);
		i.putExtra(KEY_HUMAN_COUNT, humanCount);
		return i;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);
		int playerNumber = 1;
		if (savedInstanceState != null) {
			playerNumber = savedInstanceState.getInt(KEY_HUMAN_COUNT, playerNumber);
		}



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
			dataFragment = DataFragment.newInstance(6, 2);
			// more than one player ( supprot for only one player more)
			if (playerNumber > 1) {
				int position = new Random().nextInt(DataFragment.PLAYERS.size() - 1) + 1;
				DataFragment.PLAYERS.add(position, new HumanPlayer("Mhamed2", (long) 1e6, 200));
				this.humanCardFaceDown = true;
			}
			getFragmentManager().beginTransaction().add(dataFragment, "game").commit();
			// DEBUG
			//tableView.populate();
		}
		// else TODO restore the game


	}

	@Override
	protected void onResume() {
		super.onResume();
	}


	@Override
	public void onClick(View v) {
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
				// only one HumanPlayer
				if ((Integer) v.getTag() == Integer.valueOf(1)) {
					btnController[4].setEnabled(false);
					v.setTag(null);
					dataFragment.skip();
					AnalyticsTrackers.skipEvent(dataFragment.getRoundPhase());
					break;
				}
				// XXX DEBUG
				if (dataFragment.getTable().getPlayers() == null || dataFragment.getTable().getPlayers().isEmpty()) {
					dataFragment.getTable().populate(DataFragment.PLAYERS);
					tableView.populate(DataFragment.PLAYERS);
				}
				// remove busted player if any
				for (Player p : dataFragment.getBustedPlayer() != null ? dataFragment.getBustedPlayer() : Collections.<Player>emptyList())
					tableView.removePlayer(p);
				// XXX DEBUG
				tableView.clear();
				dataFragment.startGame();
				AnalyticsTrackers.startEvent();
				btnController[4].setEnabled(false);
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
		btnController[4].setTag(null); // remove skip tag
		btnController[4].setText(R.string.start);
	}

	@Override
	public void playerBusted(Set<Player> player) {
		// TODO if it's human request that he buy in or purchase chips to removed him from busted set
		for (Player p : player)
			tableView.getPlayerView(p.getSeat().getId()).setInfo(R.string.busted);
	}

	@Override
	public void gameWinners(boolean last, boolean isShowdown, Set<Player> winners, long winAmount) {
		// TODO animate last winning players
		for (Player p : winners) {
			PlayerView playerView = tableView.getPlayerView(p.getSeat().getId());
			// transfer the amount to winner playerView
			tableView.movePot(p, winAmount);
			// update players View
			playerView.updateView(p);
			// show winning hand info only if the round phase is showdown (if showdown not called the info text will be invisible)
			tableView.setInfo(Util.getStringId(p.getHandHoldem().getHandScore().getHandType()));
			if (last) {
				playerView.setInfo(R.string.winner);
				if (isShowdown) {
					// display winner hand type and animate best 5 cards
					if (winners.size() > 1 && HandScoreCalculator.isCommunityCards5BestCard(p.getHandHoldem().getHandScore(), p.getHandHoldem().getCommunityCards())) {
						// give priority on a tie, to highlight shared cards if they can represent the players HandScore
						boolean[] enableCC = new boolean[5];
						Arrays.fill(enableCC, true);
						tableView.enableCommunityCard(enableCC);
						playerView.enableCards(false, false);
					} else tableView.highlightWinningCard(playerView, p.getHandHoldem());
				}
			}
		}

	}

	@Override
	public void onPreTurn(Player player, Turn turn) {
		if (player instanceof HumanPlayer) {
			// activate only possible commands
			btnController[0].setEnabled(true); //AllIn
			btnController[3].setEnabled(true); //Fold
			long maxBet = player.getCash() + player.getBet();
			// Call cmd enabled only if the player has above amountToContinue (if it's equal or less then only allin/Fold activated)
			if (maxBet > turn.getAmountToContinue()) {
				btnController[2].setEnabled(true); // Call
				btnController[2].setText(turn.getAmountToContinue() == player.getBet() ? R.string.check : R.string.call);
				// Raise cmd enabled only if the player has above the minBet (if it's equal or less then only allin/Call/Fold activated)
				if (maxBet > turn.getMinRaise()) {
					btnController[1].setEnabled(true); // Raise
				}
			}
			// The amount to add can't be more than cash
			long amountToAdd = Math.min(player.getCash(), turn.getAmountToContinue() - player.getBet());
			if (amountToAdd != 0) {
				btnController[4].setText(Util.formatNumber(amountToAdd));
			}
		}
		tableView.getPlayerView(player.getSeat().getId()).setHighlight(true);
		// animate PlayerView

	}

	@Override
	public void onTurnEnded(Player player) {
		tableView.getPlayerView(player.getSeat().getId()).setHighlight(false);
		// update and animate PlayerView to reflect action
		if (player instanceof HumanPlayer) {
			// disable panel control
			btnController[0].setEnabled(false); //AllIn
			btnController[1].setEnabled(false); // raise
			btnController[2].setEnabled(false); // call
			btnController[3].setEnabled(false); //Fold
			if (player.isOut()) {
				btnController[4].setEnabled(true);
				btnController[4].setText(R.string.skip);
				btnController[4].setTag(1);
			} else {
				// remove info (amount to add to continue
				btnController[4].setText(null);
			}
		}
		tableView.getPlayerView(player.getSeat().getId()).updateView(player);
	}

	@Override
	public void onRound(RoundPhase roundPhase) {
		// TODO animate card dealing on PRE_FLOP and Toast roundPhase name
		for (Player player : dataFragment.getTable().getPlayers()) {
			PlayerView playerView = tableView.getPlayerView(player.getSeat().getId());
			if (roundPhase == RoundPhase.PRE_FLOP) {
				tableView.setDealer(dataFragment.getTable().getDealer());

				if (player instanceof HumanPlayer) {
					playerView.setHand(player.getHandHoldem().getHand());
					// if there's more of one player show card face down
					playerView.showCards(humanCardFaceDown);
				}
				// PRE_FLOP is called before onBlindPosted (before setup of the round and after setup of the game)
				playerView.setEnabled(true);
			}
			playerView.updateView(player);
		}
		Toast.makeText(this, roundPhase.name(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onBlindPosted(Player smallBlind, Player bigBlind) {
		// post raise same work as TurnEnded
		tableView.getPlayerView(smallBlind.getSeat().getId()).updateView(smallBlind);
		tableView.getPlayerView(bigBlind.getSeat().getId()).updateView(bigBlind);
	}

	@Override
	public void onShowdown(Set<Player> potentialWinners) {
		// flip potentialWinners cards
		for (Player player : potentialWinners) {
			PlayerView playerView = tableView.getPlayerView(player.getSeat().getId());
			playerView.setHand(player.getHandHoldem().getHand());
			playerView.showCards(false);
		}
	}

	@Override
	public void onPotChanged(long potValue) {
		// Update PotView
		tableView.setPot(potValue);
	}

	@Override
	public void onCommunityCardsChange(RoundPhase roundPhase, CommunityCards communityCards) {
		// flip shared cards
		tableView.setCommunityCardsView(roundPhase, communityCards);
	}
}
