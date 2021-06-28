package com.natami.deminator.back.postparams;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface StartPostParams {
	@JsonProperty(value="room", required=true)
	public String getRoomID();
}
