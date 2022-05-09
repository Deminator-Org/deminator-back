package com.natami.deminator.back.io.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natami.deminator.back.model.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RevealedCellInfo {
	private final int clue;
	private final Collection<Player> revealed;

	public RevealedCellInfo(int clue, Collection<Player> revealed) {
		this.clue = clue;
		this.revealed = revealed;
	}

	@JsonProperty("clue")
	public int getClue() {
		return this.clue;
	}

	@JsonProperty("who")
	public Collection<String> getWhoRevealed() {
		// Convert players to String (display name)
		Set<String> revealed = new HashSet<>();
		for(Player p : this.revealed) {
			revealed.add(p.getName());
		}

		return revealed;
	}

	public void addPlayer(Player p) {
		this.revealed.add(p);
	}
}
