package com.natami.deminator.back.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.natami.deminator.back.util.Coord;

public class RoomManager {
	private Map<String,Room> rooms = new HashMap<>();

	public Room getOrCreateRoom(String roomNumber) {
		if(rooms.containsKey(roomNumber)) {
			return rooms.get(roomNumber);
		}

		if(Room.isValidRoomNumber(roomNumber)) {
			Room r = new Room(roomNumber);
			rooms.put(roomNumber, r);
			return r;
		}

		return null;
	}

	public Room getRoom(String roomNumber) {
		return rooms.get(roomNumber);
	}

	public void action(Room r, String clientID, List<Coord> actionsList) {
		Player p = r.getPlayer(Long.parseLong(clientID));
		p.setActions(actionsList);
	}

}
