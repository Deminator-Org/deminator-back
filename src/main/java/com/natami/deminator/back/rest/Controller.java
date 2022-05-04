package com.natami.deminator.back.rest;

import java.util.List;

import com.natami.deminator.back.exceptions.InvalidSettingsException;
import com.natami.deminator.back.io.requests.GameSetup;
import com.natami.deminator.back.io.requests.Rename;
import com.natami.deminator.back.model.Game;
import com.natami.deminator.back.io.responses.GameData;
import com.natami.deminator.back.io.responses.PlayerAction;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class Controller {

	private final Game game = new Game();

	// // // GET // // //

	@GetMapping(path = "/", produces="application/json")
	public GameData getGameData() {
		return game;
	}

	// // // POST // // //

	@PostMapping(path = "/gameSetup", consumes = "application/json", produces="application/json")
	public GameData gameSetup(@RequestBody GameSetup parameters) throws InvalidSettingsException {
		// // // Check parameters

		List<String> errors = parameters.validate();
		if(!errors.isEmpty()) {
			System.out.println(new InvalidSettingsException(errors).getMessage());
			throw new InvalidSettingsException(errors);
		}

		// // // All params ok: Apply command

		// Create the new game
		game.reset(parameters.getSettings());

		// Add player to the game
		if(game.getPlayers().stream().noneMatch(p -> p.getName().equals(parameters.getPlayerName()))) {
			game.newPlayer(parameters.getPlayerName());
		}

		// // // Return response

		return getGameData();
	}

	@PostMapping(path = "/reveal", consumes = "application/json", produces="application/json")
	public GameData getMines(@RequestBody PlayerAction action) {
		// // // Check context and parameters

		if(game.hasGameEnded()) {
			throw new IllegalStateException("Game has ended");
		}
		if(!game.getCurrentPlayerName().equals(action.getPlayerName())) {
			throw new IllegalStateException("Not your turn");
		}

		// // // Apply action

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
