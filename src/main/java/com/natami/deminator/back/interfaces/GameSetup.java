package com.natami.deminator.back.interfaces;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface GameSetup {
	@JsonProperty(value="width")
	public Integer getWidth();

	@JsonProperty(value="height")
	public Integer getHeight();

	@JsonProperty(value="mines")
	public Integer getMinesCount();

	@JsonProperty(value="playername")
	public String getPlayerName();
}
