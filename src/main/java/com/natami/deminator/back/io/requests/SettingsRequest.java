package com.natami.deminator.back.io.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface SettingsRequest {
	@JsonProperty(value="width", required = true)
	int getWidth();

	@JsonProperty(value="height", required = true)
	int getHeight();

	@JsonProperty(value="mines", required = true)
	int getMinesCount();

	@JsonProperty(value="turnDuration", required = true)
	int getTurnDuration();

	@JsonProperty(value="startTimeout", defaultValue = "3")
	int getStartTimeout();

	@JsonProperty(value="seed")
	long getSeed();
}
