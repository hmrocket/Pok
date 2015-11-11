package com.hmrocket.magicpoker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hmrocket.magicpoker.R;
import com.hmrocket.magicpoker.Util;

//TODO multi-size support for the bck, textSize, size en general
//optional currency support
/**
 * @since 10/Nov/2015 - mhamed
 */
public class PotView extends FrameLayout {

	private long amount;
	private TextView potAmountLabel;
	private ChipView potChips;

	public PotView(Context context, AttributeSet attributeset) {
		super(context, attributeset);
		LayoutInflater.from(context).inflate(R.layout.pot_view, this, true);
		potAmountLabel = (TextView) findViewById(R.id.tx_PotAmount);
		potChips = (ChipView) findViewById(R.id.chipsView_Pot);
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long l) {
		amount = l;
		potAmountLabel.setText(Util.formatNumberShort(l));
		potChips.setChipLevel(l);
	}

}
