package com.natami.deminator.back.rest;

import com.natami.deminator.back.exceptions.InvalidSettingsException;
import com.natami.deminator.back.interfaces.GameData;
import com.natami.deminator.back.interfaces.GameSetup;
import com.natami.deminator.back.interfaces.PlayerAction;
import com.natami.deminator.back.interfaces.Rename;
import com.natami.deminator.back.model.Coord;
import com.natami.deminator.back.model.Game;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class Controller {

	private Game game = null;

	@GetMapping(path = "/", produces="application/json")
	public String index() {
		return "{}";
	}

	@GetMapping(path = "/gameSetup", consumes = "application/json", produces="application/json")
	public GameData gameSetup(@RequestBody GameSetup parameters) throws InvalidSettingsException {
		if(game == null) {
			// Check parameters, send InvalidSettingsException in case of error
			if(parameters.getWidth() <= 0) {
				throw new InvalidSettingsException("Width must be positive");
			} else if(parameters.getHeight() <= 0) {
				throw new InvalidSettingsException("Height must be positive");
			} else if(parameters.getMinesCount() <= 0) {
				throw new InvalidSettingsException("Mines count must be positive");
			} else if(parameters.getMinesCount() >= parameters.getWidth() * parameters.getHeight()) {
				throw new InvalidSettingsException("Mines count must be less than board size (" + parameters.getWidth() * parameters.getHeight() + ")");
			}

			game = new Game(parameters.getWidth(), parameters.getHeight(), parameters.getMinesCount());
		}

		if(!game.getPlayers().stream().anyMatch(p -> p.getName().equals(parameters.getPlayerName()))) {
			game.newPlayer(parameters.getPlayerName());
		}

		return getGameData();
	}

	@GetMapping(path = "/gameSetup", produces="application/json")
	public GameData getGameData() {
		if(game == null) {
			throw new IllegalStateException("Game not initialized");
		}
		return game;
	}

	@GetMapping(path = "/reveal", consumes = "application/json", produces="application/json")
	public GameData getMines(@RequestBody PlayerAction action) {
		if(!game.hasGeneratedMines()) {
			game.generateMines(action.getCoord());
		}

		if(!game.open(action.getPlayerName(), action.getCoord())) {
			throw new IllegalArgumentException("Invalid action");
		}

		return game;
	}

	@GetMapping(path = "/rename", consumes = "application/json", produces="application/json")
	public GameData rename(@RequestBody Rename rename) {
		if(!game.renamePlayer(rename.getCurrentName(), rename.getNewName())) {
			throw new IllegalArgumentException("Name already taken");
		}
		return game;
	}
}
