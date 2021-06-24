package com.natami.deminator.back.postparams;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ReadyPostParams {
	@JsonProperty(value="client", required=true)
	public String getClientID();

	@JsonProperty(value="room", required=true)
	public String getRoomID();

	@JsonProperty(value="isReady", required=true)
	public boolean isReady();
}
