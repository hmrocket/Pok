package com.hmrocket.magicpoker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hmrocket.magicpoker.R;
import com.hmrocket.magicpoker.fragment.StaticFragment;
import com.hmrocket.magicpoker.fragment.guide.HandRanksFragment;

public class GuideActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	private NavigationView navigationView;

	public static Intent newInstance(Context context) {
		return new Intent(context, GuideActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guide, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_feedback) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings({"StatementWithEmptyBody", "ConstantConditions"})
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		// check the item
		navigationView.setCheckedItem(item.getItemId());
		// Handle navigation view item clicks here.
		Fragment fragment = null;
		switch (item.getItemId()) {
			case R.id.nav_rules:
				fragment = StaticFragment.newInstance(R.layout.fragment_guide_rules);
				getSupportActionBar().setTitle(R.string.rules_of_the_game);
				break;
			case R.id.nav_handRanks:
				fragment = HandRanksFragment.newInstance();
				getSupportActionBar().setTitle(R.string.hand_ranks);
				break;
			case R.id.nav_positionBasics:
				fragment = StaticFragment.newInstance(R.layout.fragment_guide_position_basics);
				getSupportActionBar().setTitle(R.string.position_basics);
				break;
			case R.id.nav_preFlop_strategy:
				fragment = StaticFragment.newInstance(R.layout.fragment_guide_preflop);
				getSupportActionBar().setTitle(R.string.pre_flop_strategy);
				break;
			case R.id.nav_reading_your_opponent:
				fragment = StaticFragment.newInstance(R.layout.fragment_guide_reading_your_opponent);
				getSupportActionBar().setTitle(R.string.reading_your_opponent);
				break;
			case R.id.nav_bankroll:
				// TODO write an article about this
				fragment = StaticFragment.newInstance(R.layout.fragment_guide_backroll_mangement);
				getSupportActionBar().setTitle(R.string.bankroll_management);
				break;
			case R.id.nav_tight_aggressive:
				fragment = StaticFragment.newInstance(R.layout.fragment_guide_tight_agressive);
				getSupportActionBar().setTitle(R.string.tight_aggressive);
				break;
			default:
				// mini games and poker tools
		}

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_guide, fragment)
				.commit();

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

}
