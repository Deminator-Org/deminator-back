package com.natami.deminator.back.io.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Rename {

	@JsonProperty(value="currentName")
	public String getCurrentName();

	@JsonProperty(value="newName")
	public String getNewName();

}
