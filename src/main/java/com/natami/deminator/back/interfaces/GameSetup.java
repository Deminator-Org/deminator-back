package com.natami.deminator.back.interfaces;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.interfaces.sub.DeminatorSettings;

public class GameSetup {

	private DeminatorSettings settings;
	private String playerName;

	public GameSetup() {
	}

	@JsonProperty(value="settings")
	public DeminatorSettings getSettings() {
		return settings;
	}

	@JsonProperty(value="playerName")
	public String getPlayerName() {
		return playerName;
	}

	public void setSettings(DeminatorSettings settings) {
		this.settings = settings;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public List<String> validate() {
		List<String> errors = this.settings.validate();

		if(playerName == null || playerName.isEmpty()) {
			errors.add("Player name is not set or empty");
		}
		return errors;
	}
}
