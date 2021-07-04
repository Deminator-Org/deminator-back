package com.natami.deminator.back.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface EntityRoom {
	@JsonProperty("width")
	public int getWidth();
	@JsonProperty("height")
	public int getHeight();
	@JsonProperty("mines")
	public int getMinesCount();

	@JsonProperty("turnDuration")
	public int getTurnDuration();

	@JsonProperty("startTime")
	public String getGameStartTime();

	@JsonProperty("who")
	public List<? extends EntityPlayer> getPlayerList();
}
