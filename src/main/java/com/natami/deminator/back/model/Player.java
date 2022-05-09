package com.natami.deminator.back.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.natami.deminator.back.io.responses.PublicPlayerData;
import com.natami.deminator.back.io.responses.SecretPlayerData;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(as=PublicPlayerData.class)
public class Player implements PublicPlayerData, SecretPlayerData {
	private String name;
	private final Map<Coord, Integer> revealed = new HashMap<>();
	private boolean canPlay = false;
	private int lastTurnPlayed = -1;
	private int score = 0;

	public Player(String playerName) {
		this.name = playerName;
	}

	// // // Overrides PublicPlayerData

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getScore() {
		return this.score;
	}

	// // // Overrides SecretPlayerData

	@JsonView(SecretPlayerData.class)
	@Override
	public Map<Coord, Integer> getRevealed() {
		return revealed;
	}

	@JsonView(SecretPlayerData.class)
	@Override
	public boolean canPlay() {
		return this.canPlay;
	}


	// // // Public functions

	public void setName(String name) {
		this.name = name;
	}


	// // // Protected functions

	protected int getLastTurnPlayed() {
		return lastTurnPlayed;
	}

	protected void reveal(Coord coord, int value) {
		revealed.put(coord, value);
	}

	protected boolean hasRevealed(Coord coord) {
		return revealed.containsKey(coord);
	}

	protected void setLastTurnPlayed(int lastTurnPlayed) {
		this.lastTurnPlayed = lastTurnPlayed;
	}

	protected void setCanPlay(boolean canPlay) {
		this.canPlay = canPlay;
	}

	protected void setScore(int score) {
		this.score = score;
	}
}
