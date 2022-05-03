package com.natami.deminator.back.rest;

import java.util.ArrayList;
import java.util.List;

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
		return "{}";
	}

	@GetMapping(path = "/gameData", produces="application/json")
	public GameData getGameData() {
		if(game == null) {
			throw new IllegalStateException("Game not initialized");
		}
		return game;
	}

	// // // POST // // //

	@PostMapping(path = "/gameSetup", consumes = "application/json", produces="application/json")
	public GameData gameSetup(@RequestBody GameSetup parameters) throws InvalidSettingsException {
		// // // Check game status

		if(game != null) {
			throw new IllegalStateException("Game already initialized");
		}

		// // // Check parameters

		List<String> errors = parameters.validate();
		if(!errors.isEmpty()) {
			System.out.println(new InvalidSettingsException(errors).getMessage());
			throw new InvalidSettingsException(errors);
		}

		// // // All params ok: Apply command

		// Create the new game
		game = new Game(parameters.getSettings());

		// Add player to the game
		if(!game.getPlayers().stream().anyMatch(p -> p.getName().equals(parameters.getPlayerName()))) {
			game.newPlayer(parameters.getPlayerName());
		}

		// // // Return response

		return getGameData();
	}

	@PostMapping(path = "/reveal", consumes = "application/json", produces="application/json")
	public GameData getMines(@RequestBody PlayerAction action) {
		if(!game.open(action.getPlayerName(), action.getCoord())) {
			throw new IllegalArgumentException("Invalid action");
		}

		return game;
	}

	@PostMapping(path = "/rename", consumes = "application/json", produces="application/json")
	public GameData rename(@RequestBody Rename rename) {
		if(!game.renamePlayer(rename.getCurrentName(), rename.getNewName())) {
			throw new IllegalArgumentException("Name already taken");
		}
		return game;
	}
}
