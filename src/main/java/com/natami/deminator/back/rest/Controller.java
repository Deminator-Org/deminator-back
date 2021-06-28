package com.natami.deminator.back.rest;

import javax.servlet.http.HttpServletRequest;

import com.natami.deminator.back.model.Player;
import com.natami.deminator.back.model.Room;
import com.natami.deminator.back.model.RoomManager;
import com.natami.deminator.back.postparams.ActionPostParams;
import com.natami.deminator.back.postparams.ClientPostParams;
import com.natami.deminator.back.postparams.ReadyPostParams;
import com.natami.deminator.back.postparams.SettingsPostParams;
import com.natami.deminator.back.postparams.StartPostParams;
import com.natami.deminator.back.responses.EntityGame;
import com.natami.deminator.back.responses.EntityRoom;

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
	private RoomManager rooms = new RoomManager();

	@Autowired
	private HttpServletRequest request;

	@GetMapping(path = "/", produces="application/json")
	public String index() {
		return "{client:\"" + getUniqueSessionId() + "\"}";
	}

	@GetMapping(path = "/{roomNumber}", produces="application/json")
	public EntityRoom getRoomStatus(@PathVariable String roomNumber) {
		return getRoom(roomNumber, true);
	}

	@PostMapping(path = "/settings", consumes="application/json", produces="application/json")
	public EntityRoom setRoomSettings(@RequestBody SettingsPostParams parameters) {
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
		if(parameters.getTurnDuration() != null) {
			r.setTurnDuration(parameters.getTurnDuration());
		}
		return r;
	}

	@PostMapping(path = "/ready", consumes="application/json", produces="application/json")
	public EntityRoom setPlayerReady(@RequestBody ReadyPostParams parameters) {
		Room r = getRoom(parameters.getRoomID(), false);
		r.setPlayerReady(getUniqueSessionId(), parameters.isReady());
		return r;
	}

	@PostMapping(path = "/client", consumes="application/json")
	public EntityRoom updateUserInfo(@RequestBody ClientPostParams parameters) {
		Room r = getRoom(parameters.getRoomID(), false);
		Player p = r.getPlayer(getUniqueSessionId());
		p.setName(parameters.getNewName());
		return r;
	}

	@PostMapping(path = "/start", consumes="application/json")
	public EntityRoom startGame(@RequestBody StartPostParams parameters) {
		Room r = getRoom(parameters.getRoomID(), false);
		if(r.areAllPlayersReady()) {
			r.start();
		}
		return r;
	}

	@GetMapping(path = "/{roomNumber}/game", produces="application/json")
	public EntityGame getGameStatus(@PathVariable String roomNumber) {
		// TODO
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
	}

	@PostMapping(path = "/action", consumes="application/json", produces="application/json")
	public void doAction(@RequestBody ActionPostParams parameters) {
		// TODO
		return;
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
		Room r = authorizeCreation ? rooms.getOrCreateRoom(roomNumber) : rooms.getRoom(roomNumber);
		if(r == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, (authorizeCreation ? "Cannot create a room " : "Could not find room ") + roomNumber);
		}
		r.registerPlayer(getUniqueSessionId());
		return r;
	}
}
