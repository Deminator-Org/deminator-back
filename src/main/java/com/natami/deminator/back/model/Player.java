package com.natami.deminator.back.model;

import com.natami.deminator.back.io.responses.PublicPlayerData;
import com.natami.deminator.back.io.responses.SecretPlayerData;

import java.util.HashMap;
import java.util.Map;

public class Player implements SecretPlayerData, PublicPlayerData {
	private String name;
	private final Map<Coord, Integer> revealed = new HashMap<>();
	private int lastTurnPlayed = -1;

	public Player(String playerName) {
		this.name = playerName;
	}

	// // // Overrides PublicPlayerData


	public String getName() {
		return name;
	}

	public int getLastTurnPlayed() {
		return lastTurnPlayed;
	}

	// // // Overrides SecretPlayerData

	public Map<Coord, Integer> getRevealed() {
		return revealed;
	}


	// // // Others functions

	public void setName(String name) {
		this.name = name;
	}

	public void reveal(Coord coord, int value) {
		revealed.put(coord, value);
	}

	public boolean hasRevealed(Coord coord) {
		return revealed.containsKey(coord);
	}

	public void setLastTurnPlayed(int lastTurnPlayed) {
		this.lastTurnPlayed = lastTurnPlayed;
	}
}
