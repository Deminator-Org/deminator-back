package com.natami.deminator.back.rest;

import javax.servlet.http.HttpServletRequest;

import com.natami.deminator.back.model.Room;
import com.natami.deminator.back.model.RoomManager;
import com.natami.deminator.back.responses.*;
import com.natami.deminator.back.postparams.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
public class Controller {
	private RoomManager manager = new RoomManager();

	@Autowired
	private HttpServletRequest request;

	@GetMapping("/")
	public String index() {
		return "You are on session ID: " + getUniqueSessionId();
	}

	@GetMapping(path = "/{roomNumber}", produces="application/json")
	public EntityRoom getRoomStatus(@PathVariable String roomNumber) {
		return getRoom(roomNumber, true);
	}

	@PostMapping(path = "/settings", consumes="application/json", produces="application/json")
	public EntityRoom setPlayerReady(@RequestBody SettingsPostParams parameters) {
		Room r = getRoom(parameters.getRoomID(), false);

		// TODO: Gestion d'erreurs
		if(parameters.getWidth() != null) {
			r.setWidth(parameters.getWidth());
		}
		if(parameters.getHeight() != null) {
			r.setHeigth(parameters.getHeight());
		}
		if(parameters.getMinesCount() != null) {
			r.setMinesCount(parameters.getMinesCount());
		}
		return r;
	}

	@PostMapping(path = "/ready", consumes="application/json", produces="application/json")
	public EntityRoom updateSettings(@RequestBody ReadyPostParams parameters) {
		Room r = getRoom(parameters.getRoomID(), false);
		// TODO
		return r;
	}

	@PostMapping(path = "/client", consumes="application/json", produces="application/json")
	public EntityRoom updateUserInfo(@RequestBody ClientPostParams parameters) {
		Room r = getRoom(parameters.getRoomID(), false);
		// TODO
		return r;
	}


	@GetMapping(path = "/{roomNumber}/game", produces="application/json")
	public EntityGame getGameStatus(@PathVariable String roomNumber) {
		// TODO
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
	}

	@PostMapping(path = "/action", consumes="application/json", produces="application/json")
	public EntityGridUpdate doAction(@RequestBody ActionPostParams parameters) {
		// TODO
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
	}

	private long getUniqueSessionId() {
		long id = request.getRemotePort();
		String[] list = request.getRemoteHost().split("[.:]+");
		for(String i : list) {
			id *= 129;
			id += Integer.parseInt(i);
		}
		return id;
	}

	private Room getRoom(String roomNumber, boolean authorizeCreation) {
		Room r = authorizeCreation ? manager.getOrCreateRoom(roomNumber) : manager.getRoom(roomNumber);
		if(r == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, (authorizeCreation ? "Cannot create a room " : "Could not find room ") + roomNumber);
		}
		return r;
	}
}
