package com.natami.deminator.back.interfaces;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.model.Coord;

public interface PlayerAction {

	@JsonProperty(value="player")
	public String getPlayerName();

	@JsonProperty(value="coord")
	public Coord getCoord();
}
