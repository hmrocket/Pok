package com.hmrocket.magicpoker.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hmrocket.magicpoker.R;
import com.hmrocket.magicpoker.Util;
import com.hmrocket.magicpoker.view.CircularSeekBar;

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
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param amountToContinue amountToContinue to match
	 * @param stack            Player's cash
	 * @param minBet           Min Bet
	 * @return A new instance of fragment RaiseDialog.
	 */
	public static RaiseDialog newInstance(long minBet, long stack, long amountToContinue) {
		RaiseDialog fragment = new RaiseDialog();
		Bundle args = new Bundle();
		args.putLong(ARG_AMOUNT_TO_CONTINUE, amountToContinue);
		args.putLong(ARG_STACK, stack);
		args.getLong(ARG_MIN_BET, minBet);
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
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnRaiseListener");
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
			amountToContinue = getArguments().getLong(ARG_AMOUNT_TO_CONTINUE);
			// Raise amount must be at least the double
			minRaise = amountToContinue * 2;
			stack = getArguments().getLong(ARG_STACK);
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
		// set init call amountToContinue
		setRaise(2 * amountToContinue);
		return view;
	}

	public void setRaise(long raise) {
		if (raise < 0) raise = 0;
		else if (raise > stack) raise = stack;

		setRaise(raise, raise * 100 / (float) stack, true);
		setRaiseColor(raise);
	}

	/**
	 * Set text of 3 TextViews the raise (top), percentage of the raise comparing to stack(center), stack left (bottom)
	 *
	 * @param amount               amount to continue
	 * @param progress             percentage of the raise
	 * @param updateCircleProgress if true update the value of the progress, circle progress remain unchanged otherwise
	 */
	protected void setRaise(long amount, float progress, boolean updateCircleProgress) {
		// set the progress of the circle only if needed; when the user change the progress isn't needed to update it again
		if (updateCircleProgress) circularSeekBar.setProgress((int) progress);
		// set the text in the top
		txAmount.setText(Util.formatNumber(amount));
		// set the text on bottom
		txStack.setText(Util.formatNumber(stack - amount));
		// set the percentage (center of the cercle)
		txRaisePercentage.setText(DF.format(progress) + "%");
	}

	/**
	 * color the Circular SeekBar color.fold, color.call, color.raise, color.allin depending
	 * on the raise amountToContinue, if it's lower, equal, above the amountToContinue to continue or full equal to user stack
	 *
	 * @param amount amountToContinue the user would like to raise
	 */
	protected void setRaiseColor(long amount) {
		int color70, color, color25;
		if (amount >= stack) {
			// color all in
			color70 = getResources().getColor(R.color.allin_70p);
			color25 = getResources().getColor(R.color.allin_25p);
			color = getResources().getColor(R.color.allin);
		} else if (amount >= this.minRaise) {
			// color raise, must be twice bigger than amount to continue
			color70 = getResources().getColor(R.color.raise_70p);
			color25 = getResources().getColor(R.color.raise_25p);
			color = getResources().getColor(R.color.raise);
		} else if (amount >= this.amountToContinue) {
			// color call
			color70 = getResources().getColor(R.color.call_70p);
			color25 = getResources().getColor(R.color.call_25p);
			color = getResources().getColor(R.color.call);
		} else {
			// color fold
			color70 = getResources().getColor(R.color.fold_70p);
			color25 = getResources().getColor(R.color.fold_25p);
			color = getResources().getColor(R.color.fold);
		}

		// set color depending the condition
		circularSeekBar.setCircleProgressColor(color70);
		circularSeekBar.setPointerHaloColor(color25);
		circularSeekBar.setPointerHaloColorOnTouch(color);
		circularSeekBar.setPointerColor(color70);
	}

	void onRaise(long raiseValue) {
		if (mListener != null) {
			mListener.onFragmentInteraction(raiseValue);
		}
	}

	@Override
	public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
		long raiseValue = progress * stack / 100;
		setRaiseColor(raiseValue);
		setRaise(raiseValue, progress, false);
	}

	@Override
	public void onStopTrackingTouch(CircularSeekBar seekBar) {
		// might be interesting the change the text to fold or raise ...
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
		public void onFragmentInteraction(long raiseValue);
	}

}
