package com.natami.deminator.back.interfaces;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.model.Coord;
import com.natami.deminator.back.model.Player;

public interface GameData {
	@JsonProperty(value="grid")
	public Collection<Coord> getGrid();

	@JsonProperty(value="players")
	public Collection<Player> getPlayers();

	@JsonProperty(value="currentPlayer")
	public String getCurrentPlayerName();
}
