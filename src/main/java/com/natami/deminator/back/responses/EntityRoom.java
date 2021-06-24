package com.natami.deminator.back.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface EntityRoom {
	@JsonProperty("client")
	public String getClientID();

	@JsonProperty("$.grid.width")
	public int getWidth();
	@JsonProperty("$.grid.height")
	public int getHeight();
	@JsonProperty("$.grid.mines")
	public int getMinesCount();

	@JsonProperty("$.game.turnDuration")
	public int getTurnDuration();

	@JsonProperty("$.game.startTime")
	public String getGameStartTime();

	@JsonProperty("who")
	public List<EntityPlayer> getPlayerList();
}
