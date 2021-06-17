package com.natami.deminator.back.model;

import java.util.HashMap;
import java.util.Map;

public class RoomManager {
	private Map<String,Room> rooms = new HashMap<>();

	public Room getOrCreateRoom(String roomNumber) {
		if(rooms.containsKey(roomNumber)) {
			return rooms.get(roomNumber);
		}

		if(Room.isValidRoomNumber(roomNumber)) {
			Room r = new Room();
			rooms.put(roomNumber, r);
			r.setSeed(roomNumber);
			return r;
		}

		return null;
	}

	public Room getRoom(String roomNumber) {
		return rooms.get(roomNumber);
	}

}
