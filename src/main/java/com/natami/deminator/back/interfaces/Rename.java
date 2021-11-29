package com.natami.deminator.back.interfaces;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Rename {

	@JsonProperty(value="currentName")
	public String getCurrentName();

	@JsonProperty(value="newName")
	public String getNewName();

}
