package com.natami.deminator.back.rest;

import javax.servlet.http.HttpServletRequest;

import com.natami.deminator.back.entities.EntityRoom;
import com.natami.deminator.back.model.Room;
import com.natami.deminator.back.model.RoomManager;
import com.natami.deminator.back.util.InvalidSettingsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
public class Controller {
	private RoomManager manager = new RoomManager();

	@GetMapping("/")
	public String index() {
		return "controller test";
	}

	@GetMapping(path = "/{roomNumber}", produces="application/json")
	public EntityRoom getRoom(@PathVariable String roomNumber) {
		Room r = manager.getOrCreateRoom(roomNumber);
		if(r == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't create a room with name " + roomNumber);
		}
		return r;
	}

	@GetMapping(path = "/{roomNumber}/show/{x}/{y}", produces="application/json")
	public EntityRoom showCell(@PathVariable String roomNumber, @PathVariable int x, @PathVariable int y) {
		Room r = manager.getRoom(roomNumber);
		if(r == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No room found with name " + roomNumber);
		}
		r.getGrid().getCell(x, y).setAsOpen(true);

		return getRoom(roomNumber);
	}

	@GetMapping(path = "/{roomNumber}/show/all", produces="application/json")
	public EntityRoom showAllCells(@PathVariable String roomNumber) {
		Room r = manager.getRoom(roomNumber);
		if(r == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No room found with name " + roomNumber);
		}
		r.getGrid().openAllCells();

		return getRoom(roomNumber);
	}

	@RequestMapping(path = "/{roomNumber}/start", produces="application/json")
	public EntityRoom startGame(
			@PathVariable String roomNumber,
			@RequestParam(name = "width", required = false) Integer width,
			@RequestParam(name = "height", required = false) Integer height,
			@RequestParam(name = "mines", required = false) Integer mines) {
		Room r = manager.getOrCreateRoom(roomNumber);
		if(r == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't create a room with name " + roomNumber);
		}
		if(width != null) {
			r.setWidth(width);
		}
		if(height != null) {
			r.setHeigth(height);
		}
		if(mines != null) {
			r.setMinesCount(mines);
		}
		try {
			r.start();
		} catch (InvalidSettingsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}

		return getRoom(roomNumber);
	}

	@GetMapping(path = "/{roomNumber}/set/width/{width}", produces="application/json")
	public EntityRoom setRoomWidth(@PathVariable String roomNumber, @PathVariable int width) {
		Room r = manager.getRoom(roomNumber);
		if(r == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No room found with name " + roomNumber);
		}
		r.setWidth(width);

		return getRoom(roomNumber);
	}

	@GetMapping(path = "/{roomNumber}/set/height/{height}", produces="application/json")
	public EntityRoom setRoomHeight(@PathVariable String roomNumber, @PathVariable int height) {
		Room r = manager.getRoom(roomNumber);
		if(r == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No room found with name " + roomNumber);
		}
		r.setHeigth(height);

		return getRoom(roomNumber);
	}
	@GetMapping(path = "/{roomNumber}/set/mines/{mines}", produces="application/json")
	public EntityRoom setRoomMinesNames(@PathVariable String roomNumber, @PathVariable int mines) {
		Room r = manager.getRoom(roomNumber);
		if(r == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No room found with name " + roomNumber);
		}
		r.setMinesCount(mines);

		return getRoom(roomNumber);
	}
}
