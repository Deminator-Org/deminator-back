package com.natami.deminator.back.rest;

import java.util.List;

import com.natami.deminator.back.exceptions.InvalidSettingsException;
import com.natami.deminator.back.io.requests.NewPlayer;
import com.natami.deminator.back.io.requests.DeminatorSettings;
import com.natami.deminator.back.io.responses.PlayerGameData;
import com.natami.deminator.back.model.Game;
import com.natami.deminator.back.io.responses.GameData;
import com.natami.deminator.back.io.responses.PlayerAction;

import com.natami.deminator.back.model.Player;
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
	public GameData getPublicGameData() {
		return game;
	}

	@GetMapping(path = "/", produces="application/json")
	public PlayerGameData getPlayerGameData(@RequestBody String playerId) {
		return new PlayerGameData(game.getPlayerById(playerId), game);
	}

	// // // POST // // //

	@PostMapping(path = "/gameSetup", consumes = "application/json", produces="application/json")
	public GameData gameSetup(@RequestBody DeminatorSettings params) throws InvalidSettingsException {
		// // // Check parameters
		List<String> errors = params.validate();
		if(!errors.isEmpty()) {
			System.out.println(new InvalidSettingsException(errors).getMessage());
			throw new InvalidSettingsException(errors);
		}

		// // // All params ok: Apply command
		game.reset(params);

		// // // Return response
		return getPublicGameData();
	}

	@PostMapping(path = "/reveal", consumes = "application/json", produces="application/json")
	public PlayerGameData getMines(@RequestBody PlayerAction action) {
		// // // Check context and parameters

		if(game.hasGameEnded()) {
			throw new IllegalStateException("Game has ended");
		}

		// // // Apply action

		if(!game.open(action.getPlayerId(), action.getCoord())) {
			throw new IllegalArgumentException("Invalid action");
		}

		return getPlayerGameData(action.getPlayerId());
	}

	@PostMapping(path = "/setPlayer", consumes = "application/json", produces="application/json")
	public PlayerGameData setPlayer(@RequestBody NewPlayer params) {
		Player p = game.getPlayerById(params.getId());
		if(p == null) {
			game.newPlayer(params.getId(), params.getName());
		} else {
			p.setName(params.getName());
		}

		return getPlayerGameData(params.getId());
	}
}
