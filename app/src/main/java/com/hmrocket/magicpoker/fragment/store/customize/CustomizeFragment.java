package com.hmrocket.magicpoker.fragment.store.customize;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hmrocket.magicpoker.R;


/**
 * store
 */
public class CustomizeFragment extends Fragment implements CustomizeAdapter.CustomizeListener {

	public static CustomizeFragment newInstance() {
		return new CustomizeFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		RecyclerView recyclerView = ((RecyclerView) inflater.inflate(R.layout.fragment_customize, container, false));

		setupRecyclerView(recyclerView);

		return recyclerView;
	}

	private void setupRecyclerView(RecyclerView recyclerView) {
		recyclerView.setAdapter(new CustomizeAdapter(StoreDeck.getAllDecks(), StoreTable.getAllTables(), null, this));
	}

	@Override
	public void onBuyTable(StoreTable storeTable) {
		Toast.makeText(getContext(), "Process buying of " + storeTable.tableId + ", price" + storeTable.price, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onBuyDeck(StoreDeck storeDeck) {
		Toast.makeText(getContext(), "Process buying of " + storeDeck.deckPeek.length + ", price" + storeDeck.price, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onBuyBackground(StoreBackground storeBackground) {
		Toast.makeText(getContext(), "Process buying of " + storeBackground.titleId + ", price" + storeBackground.price, Toast.LENGTH_SHORT).show();
	}
}
