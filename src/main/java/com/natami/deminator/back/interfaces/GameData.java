package com.natami.deminator.back.interfaces;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.model.Coord;
import com.natami.deminator.back.model.Player;

public interface GameData {
	@JsonProperty(value="width")
	public int getWidth();

	@JsonProperty(value="height")
	public int getHeight();

	@JsonProperty(value="mines")
	public int getMinesCount();

	@JsonProperty(value="mines")
	public Collection<Coord> getMines();

	@JsonProperty(value="players")
	public Collection<Player> getPlayers();

	@JsonProperty(value="currentPlayer")
	public String getCurrentPlayerName();
}
