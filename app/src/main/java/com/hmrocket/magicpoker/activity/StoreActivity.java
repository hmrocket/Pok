package com.hmrocket.magicpoker.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hmrocket.magicpoker.R;
import com.hmrocket.magicpoker.fragment.ChipsGoldFragment;
import com.hmrocket.magicpoker.fragment.MoreFunFragment;

public class StoreActivity extends AppCompatActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO every time the user visit Store give him free money
		setContentView(R.layout.activity_store);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the  sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.container);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(mViewPager);

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).
			switch (position) {
				case 0:
					return new ChipsGoldFragment();
				case 1:
					break;
				case 2:
					return MoreFunFragment.newInstance("meh", "meh");
			}
			return new ChipsGoldFragment();
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case 0:
					// sell chips and virtual gold
					return getString(R.string.chips_and_gold);
				case 1:
					// Section for deck, table, background customization. (Note: bot and profile image is in the setting and they are free)
					return getString(R.string.customization);
				case 2:
					// sell more apps, watch video, win money
					return getString(R.string.more_fun);
			}
			return null;
		}
	}
}