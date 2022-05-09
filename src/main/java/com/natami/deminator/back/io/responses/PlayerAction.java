package com.natami.deminator.back.io.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.model.Coord;

public class PlayerAction {
	private String playerId;
	private Coord coord;

	@JsonProperty(value="id")
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	@JsonProperty(value="coord")
	public void setCoord(Coord coord) {
		this.coord = coord;
	}

	public String getPlayerId() {
		return playerId;
	}

	public Coord getCoord() {
		return coord;
	}
}
