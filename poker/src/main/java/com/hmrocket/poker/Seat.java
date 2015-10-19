package com.hmrocket.poker;

/**
 * Created by hmrocket on 07/10/2015.
 */
public class Seat {
    private Status status;

	public Seat(Status status) {
		this.status = status;
	}

	public Seat() {
		this.status = Status.AVAILABLE;
	}

	public Status getStatus() {
		return status;
	}

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isAvailable(){
        return status == Status.AVAILABLE;
    }

    public enum Status {
        AVAILABLE,
        UNAVAILABLE
    }
}
