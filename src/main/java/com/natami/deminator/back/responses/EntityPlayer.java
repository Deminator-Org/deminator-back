package com.natami.deminator.back.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface EntityPlayer {
	public String getName();

	@JsonProperty("isReady")
	public boolean isReady();
}
