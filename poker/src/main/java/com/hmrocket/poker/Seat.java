package com.hmrocket.poker;

/**
 * Created by hmrocket on 07/10/2015.
 */
public class Seat {
    private Status status;
	private int id;

	public Seat(int id) {
		this.status = Status.AVAILABLE;
		this.id = id;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public enum Status {
		AVAILABLE,
        UNAVAILABLE
    }
}
