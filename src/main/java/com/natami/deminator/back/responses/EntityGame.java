package com.natami.deminator.back.responses;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface EntityGame {
	public int getTurnNumber();
	public EntityGrid getGrid();
	public Map<String, Integer> getScores();

	@JsonProperty("nextTurnTime")
	public String getNextTurnStartTime();
}
