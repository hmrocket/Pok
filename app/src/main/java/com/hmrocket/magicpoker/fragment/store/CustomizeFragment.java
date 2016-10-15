package com.hmrocket.magicpoker.fragment.store;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hmrocket.magicpoker.R;


/**
 * store
 */
public class CustomizeFragment extends Fragment {

	public static CustomizeFragment newInstance() {
		return new CustomizeFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_customize, container, false);
	}

}
