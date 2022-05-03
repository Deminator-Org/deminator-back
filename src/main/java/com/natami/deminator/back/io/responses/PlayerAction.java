package com.natami.deminator.back.io.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.model.Coord;

public class PlayerAction {
	private String playerName;
	private Coord coord;

	@JsonProperty(value="playerName")
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@JsonProperty(value="coord")
	public void setCoord(Coord coord) {
		this.coord = coord;
	}

	public String getPlayerName() {
		return playerName;
	}

	public Coord getCoord() {
		return coord;
	}
}
