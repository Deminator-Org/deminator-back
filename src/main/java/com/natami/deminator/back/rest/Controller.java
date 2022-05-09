package com.natami.deminator.back.rest;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.natami.deminator.back.exceptions.InvalidActionException;
import com.natami.deminator.back.exceptions.InvalidSettingsException;
import com.natami.deminator.back.model.Settings;
import com.natami.deminator.back.io.requests.*;
import com.natami.deminator.back.io.responses.*;
import com.natami.deminator.back.model.Game;

import com.natami.deminator.back.model.Player;

import org.springframework.http.MediaType;
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

	// // // POST // // //

	@PostMapping(path="/myStatus", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
	public PlayerGameData getPlayerGameData(@RequestBody PlayerId player) {
		return getPlayerGameData(player.getId());
	}

	@PostMapping(path="/gameSetup", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
	public GameData doGameSetup(@RequestBody @JsonDeserialize(as=SettingsRequest.class) Settings params) throws InvalidSettingsException {
		// // // Check parameters
		Set<String> errors = params.validate();
		if(!errors.isEmpty()) {
			throw new InvalidSettingsException(errors);
		}

		// // // All params ok: Apply command
		game.reset(params);

		// // // Return response
		return getPublicGameData();
	}

	@PostMapping(path="/reveal", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
	public PlayerGameData doReveal(@RequestBody PlayerAction action) throws InvalidActionException {
		// // // Check context and parameters

		if(!game.isGameRunning()) {
			throw new InvalidActionException("Game is not running");
		}

		// // // Apply action

		String error = game.open(action.getPlayerId(), action.getCoord());
		if(error != null) {
			throw new InvalidActionException(error);
		}

		return getPlayerGameData(action.getPlayerId());
	}

	@PostMapping(path="/setPlayer", consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json")
	public PlayerGameData setPlayer(@RequestBody NewPlayer params) throws InvalidSettingsException {
		Set<String> errors = params.validate();

		Player p = game.getPlayerById(params.getId());

		// Check if any other player already have the prompted display name
		if(game.getPlayers().stream().anyMatch(p2 -> p2.getName().equals(params.getName()))
			&& (p == null || !p.getName().equals(params.getName()))) {
			errors.add("Another player already have this display name");
		}

		// Return errors if any
		if(errors.size() > 0) {
			throw new InvalidSettingsException(errors);
		}

		// Set the player
		if(p == null) {
			game.newPlayer(params.getId(), params.getName(), params.getColor());
		} else {
			p.setName(params.getName());
			p.setColor(params.getColor());
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
