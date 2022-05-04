package com.natami.deminator.back.io.requests;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.io.requests.sub.DeminatorSettings;

public class GameSetup {

	private DeminatorSettings settings;
	private String playerName;

	// // // Request Parameters

	@JsonProperty(value="settings", required = true)
	public void setSettings(DeminatorSettings settings) {
		this.settings = settings;
	}

	@JsonProperty(value="playerName", required = true)
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}


	// // // Getters

	public DeminatorSettings getSettings() {
		return settings;
	}

	public String getPlayerName() {
		return playerName;
	}

	// // // Functions

	public List<String> validate() {
		List<String> errors = this.settings.validate();

		if(playerName == null || playerName.isEmpty()) {
			errors.add("Player name is not set or empty");
		}

		return errors;
	}
}
