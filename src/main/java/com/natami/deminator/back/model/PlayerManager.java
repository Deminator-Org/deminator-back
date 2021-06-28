package com.natami.deminator.back.model;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
	private Map<Long, Player> players = new HashMap<>();

	public Player getPlayer(long uniqueSessionId) {
		if(players.containsKey(uniqueSessionId)) {
			return players.get(uniqueSessionId);
		}
		// Create new player
		Player p = new Player(uniqueSessionId);
		p.setReady(false);
		players.put(uniqueSessionId, p);
		return p;
	}

}
