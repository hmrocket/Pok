package com.hmrocket.magicpoker.fragment;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Represent a static content
 */
public class StaticFragment extends Fragment {
	private static final String ARG_LAYOUT_ID = "layout_id";
	private static final String ARG_HAS_TOOLBAR = "toolbar";
	private static final String ARG_TOOLBAR_ID = "toolbar_id";
	private static final String ARG_TOOLBAR_TITLE = "toolbar_title";

	public static StaticFragment newInstance(@LayoutRes int layoutId) {
		StaticFragment fragment = new StaticFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_LAYOUT_ID, layoutId);
		args.putBoolean(ARG_HAS_TOOLBAR, false);
		fragment.setArguments(args);
		return fragment;
	}

	public static StaticFragment newInstance(@LayoutRes int layoutId, @IdRes int toolbarId, String title) {
		StaticFragment fragment = new StaticFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_LAYOUT_ID, layoutId);
		args.putBoolean(ARG_HAS_TOOLBAR, true);
		args.putInt(ARG_TOOLBAR_ID, toolbarId);
		args.putString(ARG_TOOLBAR_TITLE, title);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(getArguments().getInt(ARG_LAYOUT_ID), container, false);

		if (getArguments().getBoolean(ARG_HAS_TOOLBAR))
			setupToolbar(view);

		return view;
	}

	private void setupToolbar(View rootView) {
		Toolbar toolbar = (Toolbar) rootView.findViewById(getArguments().getInt(ARG_TOOLBAR_ID));
		toolbar.setTitle(getArguments().getString(ARG_TOOLBAR_TITLE));
	}

}
