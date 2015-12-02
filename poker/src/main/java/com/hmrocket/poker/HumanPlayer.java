package com.hmrocket.poker;

import java.util.concurrent.CountDownLatch;

/**
 * Created by hmrocket on 18/11/2015.
 */
public class HumanPlayer extends Player {

	private CountDownLatch latch;

	public HumanPlayer(String name, long bankBalance, long cash) {
		super(name, bankBalance, cash);
		latch = new CountDownLatch(1);
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
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
