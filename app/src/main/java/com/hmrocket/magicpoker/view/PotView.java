package com.hmrocket.magicpoker.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hmrocket.magicpoker.R;
import com.hmrocket.magicpoker.Util;

//TODO multi-size support for the bck, textSize, size en general
//optional currency support

/**
 * @since 10/Nov/2015 - mhamed
 */
public class PotView extends LinearLayout {

	private long amount;
	private TextView potAmountLabel;
	private ChipView potChips;

	public PotView(Context context, AttributeSet attributeset) {
		super(context, attributeset);
		LayoutInflater.from(context).inflate(R.layout.pot_view, this, true);
		potAmountLabel = (TextView) findViewById(R.id.tx_PotAmount);
		potChips = (ChipView) findViewById(R.id.chipsView_Pot);
	}

	public PotView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public PotView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public PotView(Context context) {
		super(context);
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long l) {
		amount = l;
		potAmountLabel.setText(Util.formatNumberShort(l));
		potChips.setChipLevel(l);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		Bundle state = new Bundle();
		state.putParcelable("PARENT", superState);
		state.putLong("amount", amount);
		return state;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Bundle bundle = (Bundle) state;
		super.onRestoreInstanceState(bundle.getParcelable("PARENT"));
		amount = bundle.getLong("amount", 0);
		if (amount != 0)
			setAmount(amount);
	}
}
