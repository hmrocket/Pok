package com.hmrocket.magicpoker.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.hmrocket.magicpoker.R;

/**
 * @since 10/Nov/2015 - mhamed
 */
public class ChipView extends ImageView {

	private static final int CHIPS_VALUE[] = {
			10,
			25,
			100,
			250,
			500,
			1000, //1k
			2000,
			5000,
			10000, //10K
			20000,
			50000,
			100000,//100K
			200000,
			500000,
			1000000,//1M
			100000000 // 100M
	};
	private static final int CHIPS_COLOR[] = {0xFF607D8B, 0xFF795548, 0xFFFFEB3B,
			0xFFCDDC39, 0xFF03A9F4, 0xFF2196F3, 0xFF9C27B0, 0xFF009688, 0xFFF44336, 0xFF00BCD4, Color.BLACK,
			0xFF673AB7, 0xFF4CAF50, 0xFF3F51B5, 0xFFE91E63, 0xFFFFC107
	};

	public ChipView(Context context) {
		super(context);
	}

	public ChipView(Context context, AttributeSet attributeset) {
		super(context, attributeset);
	}

	public ChipView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public ChipView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	/**
	 * set the chip image depending on the pot level
	 * @param potAmount pot value
	 */
	public void setChipLevel(long potAmount) {
		if (potAmount == 0)
			this.setImageDrawable(null);
		else {
			this.setColorFilter(getChipColor(potAmount), PorterDuff.Mode.MULTIPLY);
			// support M API 23
			Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.chip);
			this.setImageDrawable(drawable);
		}
	}

	/**
	 * Find the highest level of poker chip just below the potValue
	 *
	 * @param potAmount pot value
	 * @return color
	 */
	protected int getChipColor(long potAmount) {
		if (potAmount > 0) {
			int i = 0;
			for (int value : CHIPS_VALUE) {
				if (potAmount <= value) {
					return CHIPS_COLOR[i];
				} else i++;
			}
			// if the pot is bigger that any chips available return the last one (highest chip value)
			return CHIPS_COLOR[CHIPS_COLOR.length - 1];
		}
		// if the pot is negative or = 0 return the first smallest chip
		return CHIPS_COLOR[0];
	}

}
