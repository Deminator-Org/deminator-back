package com.natami.deminator.back.io.responses;

import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.natami.deminator.back.model.Coord;

public interface GameData {
	@JsonProperty(value="settings")
	@JsonSerialize(as=SettingsResponse.class)
	SettingsResponse getSettings();

	@JsonProperty(value="players")
	Collection<PublicPlayerData> getPlayers();

	@JsonProperty(value="revealed")
	Map<Coord, Integer> getRevealed();

	@JsonProperty(value="hasGameEnded")
	boolean hasGameEnded();
}
