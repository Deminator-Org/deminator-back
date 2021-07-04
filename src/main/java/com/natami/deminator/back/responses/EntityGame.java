package com.natami.deminator.back.responses;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.util.Coord;

public interface EntityGame {
	@JsonProperty(value="width")
	public int getWidth();
	@JsonProperty(value="height")
	public int getHeight();

	@JsonProperty(value="mines")
	public Set<Coord> getMines();

	public Set<Coord> getOpenCells();
	public Map<String, Integer> getScores();

	@JsonProperty("nextTurnTime")
	public String getNextTurnStartTime();
	public int getTurnNumber();
}
