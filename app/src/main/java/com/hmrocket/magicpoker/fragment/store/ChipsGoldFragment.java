package com.hmrocket.magicpoker.fragment.store;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hmrocket.magicpoker.R;

public class ChipsGoldFragment extends Fragment implements View.OnClickListener {

	// 2arrays to define the the among gold and chip that one will get after purchasing (I know it a very shitty way to do things)
	private static int[] GOLD_DEALS = new int[]{129, 250, 600};
	private static int[] CHIPS_DEALS = new int[]{2000, (int) 1e5, (int) 1e6};
	private static int[] CHIPS_COST = new int[]{10, 90, 180};


	public static ChipsGoldFragment newInstance() {
		return new ChipsGoldFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_store_chips, container, false);
		int[] ids = new int[]{R.id.container_buy_chip_0, R.id.container_buy_chip_1, R.id.container_buy_chip_2,
				R.id.container_buy_gold_0, R.id.container_buy_gold_1, R.id.container_buy_gold_2};
		for (int id : ids) {
			view.findViewById(id).setOnClickListener(this);
		}
		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.container_buy_gold_0:
				buyGold(0);
				break;
			case R.id.container_buy_gold_1:
				buyGold(1);
				break;
			case R.id.container_buy_gold_2:
				buyGold(2);
				break;
			case R.id.container_buy_chip_0:
				buyChips(0);
				break;
			case R.id.container_buy_chip_1:
				buyChips(1);
				break;
			case R.id.container_buy_chip_2:
				buyChips(2);
				break;
		}

	}

	private void buyGold(int goldOfferId) {

	}

	private void buyChips(int chipsOfferId) {
	}
}
