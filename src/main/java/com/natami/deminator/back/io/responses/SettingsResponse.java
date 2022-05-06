package com.natami.deminator.back.io.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.model.Coord;

import java.util.Date;
import java.util.Map;

public interface SettingsResponse {
	@JsonProperty(value="startDatetime")
	Date getStartDate();
}
