package com.hmrocket.magicpoker.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.hmrocket.poker.GameEvent;
import com.hmrocket.poker.HumanPlayer;
import com.hmrocket.poker.Player;
import com.hmrocket.poker.RoundPhase;
import com.hmrocket.poker.Table;
import com.hmrocket.poker.Turn;
import com.hmrocket.poker.card.CommunityCards;

import java.util.Set;

/**
 * @since 10/Nov/2015 - mhamed
 */
public class DataFragment extends Fragment {

	// Arguments
	private static final String TABLE_CAPACITY = "table_capacity";
	private static final String MIN_BET = "min_bet";

	private Table table;
	private GameEvent gameEventListener;
	private GameService gameService;
	private Player playerTurn;
	private Turn turn;
	private Set<Player> bustedPlayer;


	public static DataFragment newInstance(int tableCapacity, long minBet) {
		Bundle args = new Bundle();
		args.putInt(TABLE_CAPACITY, tableCapacity);
		args.putLong(MIN_BET, minBet);
		DataFragment f = new DataFragment();
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		// GameEvent interface must be implemented by the activity
		try {
			gameEventListener = (GameEvent) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString()
					+ " must implement GameEvent");
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity context) {
		super.onAttach(context);
		// GameEvent interface must be implemented by the activity
		try {
			gameEventListener = (GameEvent) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString()
					+ " must implement GameEvent");
		}
	}

	// this method is only called once for this fragment
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// retain this fragment
		setRetainInstance(true);

		if (getArguments() == null) {
			table = new Table(9, 20);
		} else {
			table = new Table(getArguments().getInt(TABLE_CAPACITY), getArguments().getLong(MIN_BET));
		}
		// Populate table
		populateTable();
	}

	private void populateTable() {
		// TODO load HumanPlayer info (no image)

		// TODO add Bot around
	}

	@Override
	public void onDestroy() {
		if (gameService != null) gameService.terminate();
		super.onDestroy();
	}

	public Table getTable() {
		return table;
	}

	public void startGame() {
		gameService = new GameService();
		gameService.execute();
	}

	public Player getPlayer() {
		return playerTurn;
	}

	public Turn getTurn() {
		return turn;
	}

	public Set<Player> getBustedPlayer() {
		return bustedPlayer;
	}

	/**
	 * Connection between the game logic and the
	 * Service will be best but this will do just fine.
	 */
	class GameService extends AsyncTask<Void, Object, Void> implements GameEvent {

		private static final int GAME_ENDED = 1;
		private static final int PLAYER_BUSTED = 2;
		private static final int GAME_WINNERS = 3;
		private static final int ON_PRE_TURN = 4;
		private static final int ON_TURN_ENDED = 5;
		private static final int ON_ROUND = 6;
		private static final int ON_BLIND_POSTED = 7;
		private static final int ON_SHOWDOWN = 8;
		private static final int ON_POT_CHANGED = 9;
		private static final int ON_COMMUNITY_CARDS_CHANGED = 10;

		@Override
		protected Void doInBackground(Void... parms) {
			table.startGame();

			// TODO save data after everyGame
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// reset and setup needed variable for every hand
			bustedPlayer = null;
			table.setGameEventListener(this);
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			super.onProgressUpdate(values);
			switch ((int) values[0]) {
				case GAME_ENDED:
					gameEventListener.gameEnded();
					break;
				case PLAYER_BUSTED:
					gameEventListener.playerBusted((Set<Player>) values[1]);
					break;
				case GAME_WINNERS:
					gameEventListener.gameWinners((boolean)values[1], (Set<Player>) values[2]);
					break;
				case ON_PRE_TURN:
					gameEventListener.onPreTurn((Player) values[1], (Turn) values[2]);
					break;
				case ON_TURN_ENDED:
					gameEventListener.onTurnEnded((Player) values[1]);
					break;
				case ON_ROUND:
					gameEventListener.onRound((RoundPhase) values[1]);
					break;
				case ON_BLIND_POSTED:
					gameEventListener.onBlindPosted((Player) values[1], (Player) values[2]);
					break;
				case ON_SHOWDOWN:
					gameEventListener.onShowdown((Set<Player>) values[1]);
					break;
				case ON_POT_CHANGED:
					gameEventListener.onPotChanged((long) values[1]);
					break;
				case ON_COMMUNITY_CARDS_CHANGED:
					gameEventListener.onCommunityCardsChange((CommunityCards) values[1]);
					break;
				default:
					break;
			}
		}

		@Override
		public void gameEnded() {
			publishProgress(GAME_ENDED);
		}

		@Override
		public void playerBusted(Set<Player> players) {
			bustedPlayer = players;
			publishProgress(PLAYER_BUSTED, players);
		}

		@Override
		public void gameWinners(boolean last, Set<Player> winners) {
			publishProgress(GAME_WINNERS, last, winners);
		}

		@Override
		public void onPreTurn(Player player, Turn turn) {
			DataFragment.this.turn = turn;
			DataFragment.this.playerTurn = player;
			publishProgress(ON_PRE_TURN, player, turn);
		}

		@Override
		public void onTurnEnded(Player player) {
			DataFragment.this.turn = null;
			DataFragment.this.playerTurn = null;
			publishProgress(ON_TURN_ENDED, player);
		}

		@Override
		public void onRound(RoundPhase roundPhase) {
			publishProgress(ON_ROUND, roundPhase);
		}

		@Override
		public void onBlindPosted(Player smallBlind, Player bigBlind) {
			publishProgress(ON_BLIND_POSTED, smallBlind, bigBlind);
		}

		@Override
		public void onShowdown(Set<Player> potentialWinners) {
			publishProgress(ON_SHOWDOWN, potentialWinners);

		}

		@Override
		public void onPotChanged(long potValue) {
			publishProgress(ON_POT_CHANGED, potValue);
		}

		@Override
		public void onCommunityCardsChange(CommunityCards communityCards) {
			publishProgress(ON_COMMUNITY_CARDS_CHANGED, communityCards);
		}

		/**
		 * Terminate an AsyncTask that is running
		 */
		public void terminate() {
			cancel(true);
			for (Player p : table.getPlayers()) {
				if (p instanceof HumanPlayer) {
					((HumanPlayer) p).notResponsive();
				}
			}
		}
	}
}
