package com.natami.deminator.back.responses;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.util.Coord;

public interface EntityGame {
	@JsonProperty(value="$.grid.width")
	public int getWidth();
	@JsonProperty(value="$.grid.height")
	public int getHeight();
	@JsonProperty(value="$.grid.mines")
	public int getMinesCount();

	public List<Coord> getMines();
	public List<Coord> getOpenCells();
	public Map<String, Integer> getScores();

	@JsonProperty("nextTurnTime")
	public String getNextTurnStartTime();
	public int getTurnNumber();
}
