package com.hmrocket.magicpoker.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hmrocket.magicpoker.R;
import com.hmrocket.magicpoker.Util;
import com.hmrocket.magicpoker.view.CircularSeekBar;
import com.hmrocket.poker.Player;
import com.hmrocket.poker.Turn;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRaiseListener} interface
 * to handle interaction events.
 * Use the {@link RaiseDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RaiseDialog extends DialogFragment implements CircularSeekBar.OnCircularSeekBarChangeListener {

	private static final String ARG_AMOUNT_TO_CONTINUE = "amountToContinue";
	private static final String ARG_STACK = "stack";
	private static final String ARG_MIN_BET = "min_bet";
	private static final DecimalFormat DF = new DecimalFormat("##.#");
	private static final String ARG_MIN_RAISE = "min_raise";
	/**
	 * Last bet
	 */
	private long amountToContinue;
	/**
	 * it's simply twice the amount to continue
	 */
	private long minRaise;
	/**
	 * Smallest bet to add
	 */
	private long minBet;
	/**
	 * Current raise amount (the current amount displayed on the top)
	 */
	private long currentRaise;
	/**
	 * Player stack value
	 */
	private long stack;
	/**
	 * Circular SeekBar to pick the amount to raise
	 */
	private CircularSeekBar circularSeekBar;
	/**
	 * Amount to raise TextView
	 */
	private TextView txRaisePercentage;
	/**
	 * Stack value after raise TextView
	 */
	private TextView txStack;
	/**
	 * Raise amount TextView
	 */
	private TextView txAmount;
	private OnRaiseListener mListener;
	/**
	 * A user Preference if enabled, gives more control over the raise amount and it's gaffe friendly
	 * it wait for the user to confirm the amount before
	 */
	private boolean precisionRaiseMode;
	/**
	 * The button to subtract Min bet to the current raise
	 */
	private Button btnMinus;
	/**
	 * The button to add Min bet to the current raise
	 */
	private Button btnPlus;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param amountToContinue amountToContinue (overall amount to match)
	 * @param stack            Player's cash
	 * @param minBet           Min Bet
	 * @return A new instance of fragment RaiseDialog.
	 */
	public static RaiseDialog newInstance(long minBet, long stack, long amountToContinue) {
		RaiseDialog fragment = new RaiseDialog();
		Bundle args = new Bundle();
		args.putLong(ARG_AMOUNT_TO_CONTINUE, amountToContinue);
		args.putLong(ARG_STACK, stack);
		args.putLong(ARG_MIN_BET, minBet);
		fragment.setArguments(args);
		// XXX if the dialog is large try android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth
		fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		return fragment;
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param player needed to get Player's stack (cash)
	 * @param turn   needed for MinBet, mim amount to match, min raise
	 * @return A new instance of fragment RaiseDialog.
	 */
	public static RaiseDialog newInstance(Player player, Turn turn) {
		RaiseDialog fragment = new RaiseDialog();
		Bundle args = new Bundle();
		// we will show the player the amount to continue and not the amount to add (avoid complication, user don't like math)
		args.putLong(ARG_AMOUNT_TO_CONTINUE, turn.getAmountToContinue());
		// we will show the player the amount he must add to be considered a raise
		args.putLong(ARG_MIN_RAISE, turn.getMinRaise());
		// current stack and not the cash + bet
		args.putLong(ARG_STACK, player.getCash());
		args.putLong(ARG_MIN_BET, turn.getMinBet());
		fragment.setArguments(args);
		// XXX if the dialog is large try android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth
		fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnRaiseListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnRaiseListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			minBet = getArguments().getLong(ARG_MIN_BET);
			stack = getArguments().getLong(ARG_STACK);
			amountToContinue = getArguments().getLong(ARG_AMOUNT_TO_CONTINUE);
			// Raise amount equal to turn.getMinRaise()
			minRaise = getArguments().getLong(ARG_MIN_RAISE, Turn.defaultMinRaise(amountToContinue, minBet));
			// current raise can't be more what the player has as cash
			currentRaise = Math.min(minRaise, stack);
		}
		// decide wither to show ok,cancel,plus,minus buttons
		precisionRaiseMode = PreferenceManager.getDefaultSharedPreferences(getActivity())
				.getBoolean(Util.AppPreference.PRECISION_RAISE_MODE, false);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (outState != null) {
			outState.putLong("currentRaise", currentRaise);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_raise_panel, container, false);
		txAmount = (TextView) view.findViewById(R.id.tx_raiseAmount);
		txStack = (TextView) view.findViewById(R.id.tx_stack);
		txRaisePercentage = (TextView) view.findViewById(R.id.tx_raisePercentage);
		circularSeekBar = (CircularSeekBar) view.findViewById(R.id.circularSeekBar);
		circularSeekBar.setOnSeekBarChangeListener(this);
		// init precision Raise Mode
		if (precisionRaiseMode) {
			// set callback for + and - button
			btnPlus = (Button) view.findViewById(R.id.btn_plus);
			btnPlus.setVisibility(View.VISIBLE);
			btnPlus.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// add the current raise
					setRaise(currentRaise + minBet);
				}
			});
			btnMinus = (Button) view.findViewById(R.id.btn_minus);
			btnMinus.setVisibility(View.VISIBLE);
			btnMinus.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// reduce the raise amount
					setRaise(currentRaise - minBet);
				}
			});
			// set callbacks for ok and cancel
			view.findViewById(R.id.raise_precision_mode).setVisibility(View.VISIBLE);
			view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.onRaiseDialogResult(currentRaise);
					dismiss();
				}
			});

			view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		}

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// recover saved state
		if (savedInstanceState != null) {
			currentRaise = savedInstanceState.getLong("currentRaise", this.currentRaise);
		}
		// set currentRaise
		setRaise(currentRaise);
	}

	/**
	 * Set all View related to the raise,
	 * 1- including enable/disable minus plus button
	 * 2- change the color the Circular SeekBar (fold, call, raise, allin)
	 * 3- set 3 TextView amount, percentage, stack
	 *
	 * @param raise amount to raise
	 */
	public void setRaise(long raise) {
		setRaise(raise, true);
		setRaiseColor(raise);
	}

	/**
	 * Set text of 3 TextViews the raise (top), percentage of the raise comparing to stack(center), stack left (bottom)
	 *
	 * @param amount               amount to continue
	 * @param updateCircleProgress if true update the value of the progress, circle progress remain unchanged otherwise
	 */
	protected void setRaise(long amount, boolean updateCircleProgress) {
		if (amount <= 0) {
			currentRaise = 0;
			if (precisionRaiseMode) {
				// jumping from 0 to 100 is possible so it best to check both button
				btnMinus.setEnabled(false);
				btnPlus.setEnabled(true);
			}
		} else if (amount >= stack) {
			if (precisionRaiseMode) {
				// jumping from 0 to 100 is possible so it best to check both button
				btnPlus.setEnabled(false);
				btnMinus.setEnabled(true);
			}
			currentRaise = stack;
		} else {
			// if(precisionRaiseMode) enable buttons in this state, both buttons can't be disabled in the same time
			if (!precisionRaiseMode) {
				// Do nothing
			} else if (!btnMinus.isEnabled()) {
				btnMinus.setEnabled(true);
			} else if (!btnPlus.isEnabled()) {
				btnPlus.setEnabled(true);
			}
			currentRaise = amount;
		}
		float progress = 100 * currentRaise / (float) stack;

		// set the progress of the circle only if needed; when the user change the progress isn't needed to update it again
		if (updateCircleProgress) circularSeekBar.setProgress((int) progress);
		// set the text in the top
		txAmount.setText(Util.formatNumber(amount));
		// set the text on bottom
		txStack.setText(Util.formatNumber(stack - amount));
		// set the percentage (center of the circle)
		txRaisePercentage.setText(DF.format(progress) + "%");
	}

	/**
	 * color the Circular SeekBar color.fold, color.call, color.raise, color.allin depending
	 * on the raise amountToContinue, if it's lower, equal, above the amountToContinue to continue or full equal to user stack
	 *
	 * @param amount amountToContinue the user would like to raise
	 */
	protected void setRaiseColor(long amount) {
		int circleColor, pressedColor, holoColor;
		if (amount >= stack) {
			// color all in
			circleColor = ContextCompat.getColor(getActivity(), R.color.allin);
			holoColor = ContextCompat.getColor(getActivity(), R.color.allin_25p);
			pressedColor = ContextCompat.getColor(getActivity(), R.color.allin_70p);
		} else if (amount >= this.minRaise) {
			// color raise, must be twice bigger than amount to continue
			circleColor = ContextCompat.getColor(getActivity(), R.color.raise);
			holoColor = ContextCompat.getColor(getActivity(), R.color.raise_25p);
			pressedColor = ContextCompat.getColor(getActivity(), R.color.raise_70p);
		} else if (amount >= this.amountToContinue) {
			// color call
			circleColor = ContextCompat.getColor(getActivity(), R.color.call);
			holoColor = ContextCompat.getColor(getActivity(), R.color.call_25p);
			pressedColor = ContextCompat.getColor(getActivity(), R.color.call_70p);
		} else {
			// color fold
			circleColor = ContextCompat.getColor(getActivity(), R.color.fold);
			holoColor = ContextCompat.getColor(getActivity(), R.color.fold_25p);
			pressedColor = ContextCompat.getColor(getActivity(), R.color.fold_70p);
		}

		// set color depending the condition
		circularSeekBar.setCircleProgressColor(circleColor);
		circularSeekBar.setPointerHaloColor(holoColor);
		circularSeekBar.setPointerHaloColorOnTouch(pressedColor);
		circularSeekBar.setPointerColor(pressedColor);
	}


	@Override
	public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
		long raiseValue = progress * stack / 100;
		setRaiseColor(raiseValue);
		setRaise(raiseValue, false);
	}

	@Override
	public void onStopTrackingTouch(CircularSeekBar seekBar) {
		// might be interesting the change the text to fold or raise ...
		if (!precisionRaiseMode) {
			// precisionRaiseMode is disabled dismiss the dialog right away and post the raise to the activity
			mListener.onRaiseDialogResult(currentRaise);
			dismiss();
		}

	}

	@Override
	public void onStartTrackingTouch(CircularSeekBar seekBar) {
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnRaiseListener {
		void onRaiseDialogResult(long raiseValue);
	}

}
