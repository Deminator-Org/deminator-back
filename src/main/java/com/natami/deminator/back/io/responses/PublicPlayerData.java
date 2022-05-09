package com.natami.deminator.back.io.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface PublicPlayerData {
	@JsonProperty(value="name")
	String getName();

	@JsonProperty(value="score")
	int getScore();
}
