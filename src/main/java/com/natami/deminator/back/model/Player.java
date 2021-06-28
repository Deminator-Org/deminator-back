package com.natami.deminator.back.model;

import com.natami.deminator.back.responses.EntityPlayer;

public class Player implements EntityPlayer {
	private String name;
	private boolean isReady = false;

	public Player(long uniqueSessionId) {
		this.name = "Player " + uniqueSessionId; // Make this prettier
	}

	public void setReady(boolean b) {
		this.isReady = true;
	}

	public void setName(String newName) {
		this.name = newName;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isReady() {
		return this.isReady;
	}
}
