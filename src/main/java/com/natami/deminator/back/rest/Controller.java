package com.natami.deminator.back.rest;

import java.util.List;

import com.natami.deminator.back.exceptions.InvalidActionException;
import com.natami.deminator.back.exceptions.InvalidSettingsException;
import com.natami.deminator.back.model.Settings;
import com.natami.deminator.back.io.requests.*;
import com.natami.deminator.back.io.responses.*;
import com.natami.deminator.back.model.Game;

import com.natami.deminator.back.model.Player;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class Controller {

	private final Game game = new Game();

	// // // GET // // //

	@GetMapping(path = "/", produces="application/json")
	public GameData getPublicGameData() {
		game.updateStatus();
		return game;
	}

	@GetMapping(path = "/status", produces="application/json")
	public PlayerGameData getPlayerGameData(@RequestBody PlayerId player) {
		return getPlayerGameData(player.getId());
	}

	// // // POST // // //

	@PostMapping(path = "/gameSetup", consumes = "application/json", produces="application/json")
	public GameData doGameSetup(@RequestBody Settings params) throws InvalidSettingsException {
		// // // Check parameters
		List<String> errors = params.validate();
		if(!errors.isEmpty()) {
			throw new InvalidSettingsException(errors);
		}

		// // // All params ok: Apply command
		game.reset(params);

		// // // Return response
		return getPublicGameData();
	}

	@PostMapping(path = "/reveal", consumes = "application/json", produces="application/json")
	public PlayerGameData doReveal(@RequestBody PlayerAction action) throws InvalidActionException {
		// // // Check context and parameters

		if(game.hasGameEnded()) {
			throw new InvalidActionException("Game has ended");
		}

		// // // Apply action

		String error = game.open(action.getPlayerId(), action.getCoord());
		if(error != null) {
			throw new InvalidActionException(error);
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

	// // // EXCEPTION // // //
	@ExceptionHandler(InvalidSettingsException.class)
	@ResponseBody
	public String outputInvalidSettings(InvalidSettingsException e) {
		return e.getMessage();
	}

	@ExceptionHandler(InvalidActionException.class)
	@ResponseBody
	public String outputInvalidAction(InvalidActionException e) {
		return e.getMessage();
	}

	// // // Functions // // //
	private PlayerGameData getPlayerGameData(String id) {
		game.updateStatus();
		return new PlayerGameData(game.getPlayerById(id), game);
	}
}
