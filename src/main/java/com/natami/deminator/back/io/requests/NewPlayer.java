package com.natami.deminator.back.io.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class NewPlayer {
	private String id;
	private String name;
	private int color;

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

	@JsonProperty(value = "color", required = true)
	public int getColor() {
		return this.color;
	}

	public Set<String> validate() {
		Set<String> errors = new HashSet<>();

		if(this.id == null || this.id.isEmpty()) {
			errors.add("Player id is not set or empty");
		}
		if(this.name == null || this.name.isEmpty()) {
			errors.add("Player name is not set or empty");
		}
		if(this.color < 0 || this.color > 359) {
			errors.add("Color is not between 0 and 359 (included)");
		}

		return errors;
	}
}
