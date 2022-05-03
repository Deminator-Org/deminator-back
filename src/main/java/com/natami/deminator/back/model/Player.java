package com.natami.deminator.back.model;

import java.util.HashSet;
import java.util.Set;

public class Player {
	private String name;
	private final Set<Coord> revealed = new HashSet<>();

	public Player(String playerName) {
		this.name = playerName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return revealed.size();
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
}
