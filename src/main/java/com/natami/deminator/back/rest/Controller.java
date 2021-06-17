package com.natami.deminator.back.rest;

import java.util.HashMap;
import java.util.Map;

import com.natami.deminator.back.entities.EntityRoom;
import com.natami.deminator.back.model.Room;
import com.natami.deminator.back.util.InvalidSettingsException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class Controller {
	private Map<String,Room> rooms = new HashMap<>();

	@GetMapping("/")
	public String index() {
		return "controller test";
	}

	@GetMapping("/error")
	public String error() {
		return "Oups, c'est cassé";
	}

	@GetMapping(path = "/{roomNumber}", produces="application/json")
	public EntityRoom getRoom(@PathVariable String roomNumber) {
		if(rooms.containsKey(roomNumber)) {
			return rooms.get(roomNumber);
		}

		if(Room.isValidRoomNumber(roomNumber)) {
			Room r = new Room();
			rooms.put(roomNumber, r);

			r.setSeed(roomNumber);
			try {
				r.generateGrid();
			} catch (InvalidSettingsException e) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
			}
			return r;
		}

		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

	@GetMapping(path = "/{roomNumber}/show/{x}/{y}", produces="application/json")
	public EntityRoom showCell(@PathVariable String roomNumber, @PathVariable int x, @PathVariable int y) {
		if(!rooms.containsKey(roomNumber)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		rooms.get(roomNumber).getCell(x, y).setAsOpen(true);

		return getRoom(roomNumber);
	}

	@GetMapping(path = "/{roomNumber}/show/all", produces="application/json")
	public EntityRoom showAllCells(@PathVariable String roomNumber) {
		if(!rooms.containsKey(roomNumber)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		rooms.get(roomNumber).openAllCells();

		return getRoom(roomNumber);
	}
}
