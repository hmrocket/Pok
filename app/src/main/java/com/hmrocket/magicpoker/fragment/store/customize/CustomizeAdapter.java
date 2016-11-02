package com.hmrocket.magicpoker.fragment.store.customize;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hmrocket.magicpoker.R;

/**
 * Created by hmrocket on 01/11/2016
 */ //// -------- Adapter   -----------
final class CustomizeAdapter extends RecyclerView.Adapter {

	private static final int TYPE_TABLE = 1;
	private static final int TYPE_DECK = 2;
	private static final int TYPE_BACKGROUND = 3;

	private final StoreBackground[] storeBackgrounds;
	private final StoreDeck[] storeDecks;
	private final StoreTable[] storeTables;
	private CustomizeListener listener;

	CustomizeAdapter(StoreDeck[] storeDecks, StoreTable[] storeTables, StoreBackground[] storeBackgrounds, CustomizeListener listener) {
		this.storeDecks = storeDecks != null ? storeDecks : new StoreDeck[0];
		this.storeTables = storeTables != null ? storeTables : new StoreTable[0];
		this.storeBackgrounds = storeBackgrounds != null ? storeBackgrounds : new StoreBackground[0];
		this.listener = listener;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create holder and set the onCLickListener on buy button
		switch (viewType) {
			case TYPE_DECK:
				View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_deck, parent, false);
				final DeckViewHolder deckViewHolder = new DeckViewHolder(view);
				deckViewHolder.buy.setOnClickListener(getOnClickHolder(deckViewHolder));
				return deckViewHolder;
			case TYPE_BACKGROUND:
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_background, parent, false);
				BackgroundViewHolder backgroundViewHolder = new BackgroundViewHolder(view);
				backgroundViewHolder.buy.setOnClickListener(getOnClickHolder(backgroundViewHolder));
				return backgroundViewHolder;
			case TYPE_TABLE:
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_table, parent, false);
				TableViewHolder tableViewHolder = new TableViewHolder(view);
				tableViewHolder.buy.setOnClickListener(getOnClickHolder(tableViewHolder));
				return tableViewHolder;
			default:
				return null;
		}
	}

	private View.OnClickListener getOnClickHolder(@NonNull final RecyclerView.ViewHolder holder) {
		return listener == null ? null : new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (holder.getItemViewType()) {
					case TYPE_DECK:
						listener.onBuyDeck(getStoreDeck(holder.getAdapterPosition()));
						break;
					case TYPE_TABLE:
						listener.onBuyTable(getStoreTable(holder.getAdapterPosition()));
						break;
					case TYPE_BACKGROUND:
						listener.onBuyBackground(getStoreBackground(holder.getAdapterPosition()));
						break;
				}
			}
		};
	}

	private StoreDeck getStoreDeck(int position) {
		return storeDecks[position];
	}

	private StoreTable getStoreTable(int position) {
		// remove the offset of the position
		return storeTables[position - storeDecks.length];
	}

	private StoreBackground getStoreBackground(int position) {
		// remove the offset of the position
		return storeBackgrounds[position - storeDecks.length - storeTables.length];
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

	}

	@Override
	public int getItemViewType(int position) {
		// return the right type item depending on the position
		if (isDeckPostion(position))
			return TYPE_DECK;
		else if (isTablePostion(position))
			return TYPE_TABLE;
		else if (isBackgroundPostion(position))
			return TYPE_BACKGROUND;
		else return super.getItemViewType(position);
	}

	private boolean isDeckPostion(int position) {
		return position < storeDecks.length;
	}

	private boolean isTablePostion(int position) {
		return position - storeDecks.length < storeTables.length;
	}

	private boolean isBackgroundPostion(int position) {
		return position < getItemCount();
	}

	@Override
	public int getItemCount() {
		return storeBackgrounds.length + storeTables.length + storeDecks.length;
	}


	interface CustomizeListener {
		void onBuyTable(StoreTable storeTable);

		void onBuyDeck(StoreDeck storeDeck);

		void onBuyBackground(StoreBackground storeBackground);
	}

	//	----------------- Holders (Deck, Table, Background)
	private final static class DeckViewHolder extends RecyclerView.ViewHolder {
		ImageView[] deckPreview;
		TextView title;
		TextView price;
		Button buy;

		DeckViewHolder(View itemView) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.tx_title);
			price = (TextView) itemView.findViewById(R.id.tx_price);
			buy = (Button) itemView.findViewById(R.id.btn_buy);

			int[] ids = new int[]{R.id.iv_card1, R.id.iv_card2, R.id.iv_card3, R.id.iv_card4, R.id.iv_card5};
			deckPreview = new ImageView[ids.length];
			for (int i = 0; i < ids.length; i++)
				deckPreview[i] = (ImageView) itemView.findViewById(ids[i]);
		}
	}

	private final static class TableViewHolder extends RecyclerView.ViewHolder {
		ImageView table;
		TextView title;
		TextView price;
		Button buy;

		TableViewHolder(View itemView) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.tx_title);
			price = (TextView) itemView.findViewById(R.id.tx_price);
			table = (ImageView) itemView.findViewById(R.id.iv_table);
			buy = (Button) itemView.findViewById(R.id.btn_buy);
		}


	}

	private final static class BackgroundViewHolder extends RecyclerView.ViewHolder {
		ImageView background;
		TextView title;
		TextView price;
		Button buy;

		BackgroundViewHolder(View itemView) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.tx_title);
			price = (TextView) itemView.findViewById(R.id.tx_price);
			background = (ImageView) itemView.findViewById(R.id.iv_background);
			buy = (Button) itemView.findViewById(R.id.btn_buy);
		}
	}
}
