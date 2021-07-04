package com.natami.deminator.back.postparams;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface SettingsPostParams {
	@JsonProperty(value="client", required=true)
	public String getClientID();

	@JsonProperty(value="room", required=true)
	public String getRoomID();

	@JsonProperty(value="width", required=false)
	public Integer getWidth();
	@JsonProperty(value="height", required=false)
	public Integer getHeight();
	@JsonProperty(value="mines", required=false)
	public Integer getMinesCount();

	@JsonProperty(value="turnDuration", required=false)
	public Integer getTurnDuration();
}
