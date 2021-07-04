package com.natami.deminator.back.model;

import java.util.List;

import com.natami.deminator.back.responses.EntityPlayer;
import com.natami.deminator.back.util.Coord;

public class Player implements EntityPlayer {
	private String name;
	private boolean isReady = false;
	private boolean havePlayedThisTurn = false;
	private List<Coord> actionsList;

	public Player(long uniqueSessionId) {
		this.name = "Player " + uniqueSessionId; // Make this prettier
	}

	public void setReady(boolean b) {
		this.isReady = b;
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

	public boolean alreadyPlayed() {
		return havePlayedThisTurn;
	}

	public void setActions(List<Coord> actionsList) {
		this.actionsList = actionsList;
	}

	public List<Coord> getActions() {
		return this.actionsList;
	}
}
