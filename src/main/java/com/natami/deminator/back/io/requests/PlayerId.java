package com.natami.deminator.back.io.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerId {
	private String id;

	@JsonProperty(value= "id", required = true)
	public String getId() {
		return id;
	}
}
