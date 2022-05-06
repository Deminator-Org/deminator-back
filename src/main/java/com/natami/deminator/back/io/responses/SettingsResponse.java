package com.natami.deminator.back.io.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public interface SettingsResponse {
	@JsonProperty(value="width")
	int getWidth();

	@JsonProperty(value="height")
	int getHeight();

	@JsonProperty(value="mines")
	int getMinesCount();

	@JsonProperty(value="startDatetime")
	Date getStartDate();

	@JsonProperty(value="turnDuration")
	int getTurnDuration();
}
