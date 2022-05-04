package com.natami.deminator.back.io.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerGameData {
	private final SecretPlayerData secretPlayerData;
	private final GameData gameData;

	public PlayerGameData(SecretPlayerData secretPlayerData, GameData gameData) {
		this.secretPlayerData = secretPlayerData;
		this.gameData = gameData;
	}

	@JsonProperty(value = "game")
	GameData getGameData() {
		return this.gameData;
	}

	@JsonProperty(value = "player")
	SecretPlayerData getPlayerData() {
		return this.secretPlayerData;
	}
}
