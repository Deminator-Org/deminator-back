package com.natami.deminator.back.rest;

import com.natami.deminator.back.exceptions.InvalidSettingsException;
import com.natami.deminator.back.interfaces.GameData;
import com.natami.deminator.back.interfaces.GameSetup;
import com.natami.deminator.back.interfaces.PlayerAction;
import com.natami.deminator.back.interfaces.Rename;
import com.natami.deminator.back.interfaces.sub.DeminatorSettings;
import com.natami.deminator.back.model.Game;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class Controller {

	private Game game = null;

	// // // GET // // //

	@GetMapping(path = "/", produces="application/json")
	public String index() {
		System.out.println("index");
		return "{}";
	}

	@GetMapping(path = "/gameData", produces="application/json")
	public GameData getGameData() {
		System.out.println("getGameData");
		if(game == null) {
			throw new IllegalStateException("Game not initialized");
		}
		return game;
	}

	// // // POST // // //

	@PostMapping(path = "/gameSetup", consumes = "application/json", produces="application/json")
	public GameData gameSetup(@RequestBody GameSetup parameters) throws InvalidSettingsException {
		System.out.println("gameSetup");
		if(game == null) {
			DeminatorSettings settings = parameters.getSettings();

			// Check parameters, send InvalidSettingsException in case of error
			if(settings.getWidth() <= 0) {
				throw new InvalidSettingsException("Width must be positive");
			} else if(settings.getHeight() <= 0) {
				throw new InvalidSettingsException("Height must be positive");
			} else if(settings.getMinesCount() <= 0) {
				throw new InvalidSettingsException("Mines count must be positive");
			} else if(settings.getMinesCount() >= settings.getWidth() * settings.getHeight()) {
				throw new InvalidSettingsException("Mines count must be less than board size (" + settings.getWidth() * settings.getHeight() + ")");
			}

			game = new Game(settings.getWidth(), settings.getHeight(), settings.getMinesCount());
		}

		if(!game.getPlayers().stream().anyMatch(p -> p.getName().equals(parameters.getPlayerName()))) {
			game.newPlayer(parameters.getPlayerName());
		}

		return getGameData();
	}

	@PostMapping(path = "/reveal", consumes = "application/json", produces="application/json")
	public GameData getMines(@RequestBody PlayerAction action) {
		System.out.println("getMines");
		if(!game.hasGeneratedMines()) {
			game.generateMines(action.getCoord());
		}

		if(!game.open(action.getPlayerName(), action.getCoord())) {
			throw new IllegalArgumentException("Invalid action");
		}

		return game;
	}

	@PostMapping(path = "/rename", consumes = "application/json", produces="application/json")
	public GameData rename(@RequestBody Rename rename) {
		System.out.println("rename");
		if(!game.renamePlayer(rename.getCurrentName(), rename.getNewName())) {
			throw new IllegalArgumentException("Name already taken");
		}
		return game;
	}
}
