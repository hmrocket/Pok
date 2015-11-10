package com.hmrocket.magicpoker.fragment;

import android.app.Fragment;
import android.os.Bundle;

/**
 * @since 10/Nov/2015 - mhamed
 */
public class DataFragment extends Fragment {

	// this method is only called once for this fragment
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// retain this fragment
		setRetainInstance(true);
	}
}
