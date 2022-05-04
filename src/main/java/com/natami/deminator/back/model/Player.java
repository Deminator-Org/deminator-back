package com.natami.deminator.back.model;

import java.util.HashSet;
import java.util.Set;

public class Player {
	private String name;
	private final Set<Coord> revealed = new HashSet<>();
	private int lastTurnPlayed = -1;

	public Player(String playerName) {
		this.name = playerName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void reveal(Coord coord) {
		revealed.add(coord);
	}

	public boolean hasRevealed(Coord coord) {
		return revealed.contains(coord);
	}

	public Set<Coord> getRevealed() {
		return revealed;
	}

	public int getLastTurnPlayed() {
		return lastTurnPlayed;
	}

	public void setLastTurnPlayed(int lastTurnPlayed) {
		this.lastTurnPlayed = lastTurnPlayed;
	}
}
