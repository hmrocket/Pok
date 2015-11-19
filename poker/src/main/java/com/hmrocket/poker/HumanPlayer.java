package com.hmrocket.poker;

import java.util.concurrent.CountDownLatch;

/**
 * Created by hmrocket on 18/11/2015.
 */
public class HumanPlayer extends Player {

	private OnHumanTurn onHumanTurn;
	private CountDownLatch latch;

	public HumanPlayer(String name, long bankBalance, long cash, OnHumanTurn callback) {
		super(name, bankBalance, cash);
		if (onHumanTurn == null)
			throw new IllegalArgumentException("OnHumanTurn callback can't be null");

		onHumanTurn = callback;
		latch = new CountDownLatch(1);
	}

	@Override
	public void fold() {
		super.fold();
		// Human made a decision, release
		latch.countDown();
	}

	@Override
	public void raise(long amount) {
		super.raise(amount);
		// Human made a decision, continue
		latch.countDown();
	}

	@Override
	public void check() {
		super.check();
		// Human made a decision, continue
		latch.countDown();
	}

	@Override
	public void allIn() {
		super.allIn();
		// Human made a decision, continue
		latch.countDown();
	}

	@Override
	public void call(long amount) {
		super.call(amount);
		// Human made a decision, continue
		latch.countDown();
	}

	@Override
	public void play(Turn turn) {
		onHumanTurn.requestAction(turn.getAmountToContinue());
		// block this call until human play (fold, call or raise..)
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public interface OnHumanTurn {
		public void requestAction(long amountToContinue);
	}
}
