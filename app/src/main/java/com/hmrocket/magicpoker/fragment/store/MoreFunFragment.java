package com.hmrocket.magicpoker.fragment.store;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hmrocket.magicpoker.R;

/**
 * A fragment with a Google +1 button.
 */
public class MoreFunFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	// The request code must be 0 or greater.
	private static final int PLUS_ONE_REQUEST_CODE = 0;
	// The URL to +1.  Must be a valid URL.
	private final String PLUS_ONE_URL = "http://developer.android.com";
	private String mParam1;
	private String mParam2;

	// TODO add a plus +1 button to your google+ page
	//private PlusOneButton mPlusOneButton;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment MoreFunFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static MoreFunFragment newInstance(String param1, String param2) {
		MoreFunFragment fragment = new MoreFunFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_more_fun, container, false);

		//Find the +1 button
		//mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		// Refresh the state of the +1 button each time the activity receives focus.
		//mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
	}


}
