package com.natami.deminator.back.io.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.model.Coord;

import java.util.Map;

public interface SecretPlayerData {
	@JsonProperty(value="name")
	String getName();

	@JsonProperty(value="revealed")
	Map<Coord, Integer> getRevealed();

	@JsonProperty(value="canPlay")
	boolean canPlay();
}
