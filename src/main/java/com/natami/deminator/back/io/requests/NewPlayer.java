package com.natami.deminator.back.io.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class NewPlayer {
	private String id;
	private String name;

	/**
	 * @return Player identifier (Any random string, as unique as possible, identifying a player and that will never change)
	 */
	@JsonProperty(value = "id", required = true)
	public String getId() {
		return this.id;
	}

	/**
	 * @return Current display name of the player
	 */
	@JsonProperty(value = "name", required = true)
	public String getName() {
		return this.name;
	}

	public List<String> validate() {
		List<String> errors = new ArrayList<>();

		if(this.id == null || this.id.isEmpty()) {
			errors.add("Player id is not set or empty");
		}
		if(this.name == null || this.name.isEmpty()) {
			errors.add("Player name is not set or empty");
		}

		return errors;
	}

}
