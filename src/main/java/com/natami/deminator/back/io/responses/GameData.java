package com.natami.deminator.back.io.responses;

import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.model.Settings;
import com.natami.deminator.back.model.Coord;

public interface GameData {
	@JsonProperty(value="settings")
	Settings getSettings();

	@JsonProperty(value="players")
	Collection<PublicPlayerData> getPlayers();

	@JsonProperty(value="revealed")
	Map<Coord, Integer> getRevealed();

	@JsonProperty(value="hasGameEnded")
	boolean hasGameEnded();
}
