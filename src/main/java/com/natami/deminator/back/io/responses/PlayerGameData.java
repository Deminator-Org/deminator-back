package com.natami.deminator.back.io.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
	@JsonSerialize(as=SecretPlayerData.class)
	SecretPlayerData getPlayerData() {
		return this.secretPlayerData;
	}
}
