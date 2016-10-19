package com.hmrocket.magicpoker.fragment.guide;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hmrocket.magicpoker.R;

/**
 * hmrocket on 16/10/2016.
 */

class HandRankAdapter extends RecyclerView.Adapter<HandRankAdapter.RankHolder> {

	/**
	 * Total number of Hand ranks
	 */
	private static final int HAND_RANKS_COUNT = 10;
	/**
	 * Represent the number of card needed to represent a hand rank
	 */
	private static final int HAND_CARD_COUNT = 5;
	@DrawableRes
	private static final int[][] CARD_DRAWABLE_IDS = new int
			// [HAND_RANKS_COUNT][HAND_CARD_COUNT]
			[][]{
			{R.mipmap.sa, R.mipmap.sk, R.mipmap.sq, R.mipmap.sj, R.mipmap.s10}, // RF
			{R.mipmap.da, R.mipmap.sk, R.mipmap.sq, R.mipmap.sj, R.mipmap.s10}, // SF
			{R.mipmap.sa, R.mipmap.da, R.mipmap.ca, R.mipmap.ha, R.mipmap.s2}, // 4K
			{R.mipmap.sa, R.mipmap.da, R.mipmap.ca, R.mipmap.h2, R.mipmap.s2}, // FH
			{R.mipmap.sa, R.mipmap.sq, R.mipmap.s8, R.mipmap.s6, R.mipmap.s3}, // F
			{R.mipmap.ha, R.mipmap.sk, R.mipmap.dq, R.mipmap.sj, R.mipmap.h10}, // S
			{R.mipmap.sa, R.mipmap.da, R.mipmap.ca, R.mipmap.h8, R.mipmap.s2}, // 3K
			{R.mipmap.ha, R.mipmap.sa, R.mipmap.h9, R.mipmap.c4, R.mipmap.s4}, // 2P
			{R.mipmap.ha, R.mipmap.sa, R.mipmap.h9, R.mipmap.c8, R.mipmap.s2}, // 1P
			{R.mipmap.ha, R.mipmap.cj, R.mipmap.h9, R.mipmap.c8, R.mipmap.s2} // HC
	};

	@StringRes
	private static final int[] RANK_TITLES = new int[]{R.string.royal_flush, R.string.straight_flush, R.string.four_of_a_kind,
			R.string.full_house, R.string.flush, R.string.straight, R.string.three_of_a_kind,
			R.string.two_pairs, R.string.one_pair, R.string.high_card};
	@StringRes
	private static final int[] RANK_DESCRIPTION = new int[]{R.string.royal_flush, R.string.straight_flush, R.string.four_of_a_kind,
			R.string.full_house, R.string.flush, R.string.straight, R.string.three_of_a_kind,
			R.string.two_pairs, R.string.one_pair, R.string.high_card};


	@Override
	public RankHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide_ranks, parent, false);
		return new RankHolder(view);
	}

	@Override
	public void onBindViewHolder(RankHolder holder, int position) {
		holder.title.setText(RANK_TITLES[position]);
		holder.description.setText(RANK_DESCRIPTION[position]);
		for (int i = 0; i < HAND_CARD_COUNT; i++) {
			holder.cards[i].setImageResource(CARD_DRAWABLE_IDS[position][i]);
		}
	}

	@Override
	public int getItemCount() {
		// we have 10 hand ranks in poker
		return HAND_RANKS_COUNT;
	}

	static class RankHolder extends RecyclerView.ViewHolder {
		@IdRes
		private static final int[] CARD_VIEW_IDS = new int[]{R.id.iv_card1, R.id.iv_card2, R.id.iv_card3, R.id.iv_card4, R.id.iv_card5};

		final ImageView[] cards = new ImageView[HAND_CARD_COUNT];
		final TextView title;
		final TextView description;

		RankHolder(View rootView) {
			super(rootView);
			title = ((TextView) rootView.findViewById(R.id.tx_rank_title));
			description = ((TextView) rootView.findViewById(R.id.tx_rank_description));
			for (int i = 0; i < HAND_CARD_COUNT; i++) {
				cards[i] = ((ImageView) rootView.findViewById(CARD_VIEW_IDS[i]));
			}
		}
	}
}
