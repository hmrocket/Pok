package com.hmrocket.poker;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by hmrocket on 18/11/2015.
 */
public class HumanPlayer extends Player {

	private CountDownLatch latch;

	public HumanPlayer(String name, long bankBalance, long cash) {
		super(name, bankBalance, cash);
		latch = new CountDownLatch(1);
	}

	/**
	 * Call this method when player is no longer responsive
	 * Fold and release Any locks that hold the Game waiting for HumanPlayer action.
	 */
	public void notResponsive() {
		fold();
		latch.countDown();
	}

	@Override
	public void fold() {
		super.fold();
		// Human made a decision, release
		latch.countDown();
		// latch won't reset so a new LatchCountDown must be created
		latch = new CountDownLatch(1);
	}

	@Override
	public void raise(long amount) {
		super.raise(amount);
		// Human made a decision, continue
		latch.countDown();
		// latch won't reset so a new LatchCountDown must be created
		latch = new CountDownLatch(1);
	}

	@Override
	public void check() {
		super.check();
		// Human made a decision, continue
		latch.countDown();
		// latch won't reset so a new LatchCountDown must be created
		latch = new CountDownLatch(1);
	}

	@Override
	public void allIn() {
		super.allIn();
		// Human made a decision, continue
		latch.countDown();
		// latch won't reset so a new LatchCountDown must be created
		latch = new CountDownLatch(1);
	}

	@Override
	public void call(long amount) {
		super.call(amount);
		// Human made a decision, continue
		latch.countDown();
		// latch won't reset so a new LatchCountDown must be created
		latch = new CountDownLatch(1);
	}

	@Override
	public void play(Turn turn) {
		// block this call until human play (fold, call or raise..)
		try {
			latch.await(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
