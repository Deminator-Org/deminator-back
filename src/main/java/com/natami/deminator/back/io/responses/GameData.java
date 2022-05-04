package com.natami.deminator.back.io.responses;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.io.requests.sub.DeminatorSettings;
import com.natami.deminator.back.model.Coord;
import com.natami.deminator.back.model.Player;

public interface GameData {
	@JsonProperty(value="settings")
	DeminatorSettings getSettings();

	@JsonProperty(value="players")
	Collection<Player> getPlayers();

	@JsonProperty(value="currentPlayer")
	String getCurrentPlayerName();

	@JsonProperty(value="revealedMines")
	Collection<Coord> getRevealedMines();

	@JsonProperty(value="hasGameEnded")
	boolean hasGameEnded();
}
