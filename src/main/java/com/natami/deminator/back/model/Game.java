package com.natami.deminator.back.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.natami.deminator.back.io.requests.sub.DeminatorSettings;
import com.natami.deminator.back.io.responses.GameData;

public class Game implements GameData {
	private DeminatorSettings settings;
	private final Set<Coord> mines = new HashSet<>();
	private final List<Player> players = new ArrayList<>();
	private int currentPlayerIndex = 0;

	public Game(DeminatorSettings settings) {
		this.settings = settings;

		this.generateMines();
	}

	@Override
	public Collection<Player> getPlayers() {
		return players;
	}

	@Override
	public String getCurrentPlayerName() {
		return players.get(currentPlayerIndex).getName();
	}

	@Override
	public DeminatorSettings getSettings() {
		return this.settings;
	}


	@Override
	public Collection<Coord> getRevealedMines() {
		Set<Coord> revealedCells = new HashSet<>();
		for (Player player : players) {
			revealedCells.addAll(player.getRevealed());
		}
		revealedCells.retainAll(mines);
		return revealedCells;
	}

	// Add minecount different coords in the mines set except for the given coord
	public void generateMines() {
		mines.clear();

		Random random = new Random(settings.getSeed());

		int minesCount = settings.getMinesCount();
		int width = settings.getWidth();
		int height = settings.getHeight();
		while(mines.size() < minesCount) {
			for(int i=mines.size(); i<minesCount; i++) {
				Coord mine = new Coord(random.nextInt(width), random.nextInt(height));
				mines.add(mine);
			}
		}
	}

	public boolean hasGeneratedMines() {
		return !mines.isEmpty();
	}

	public boolean renamePlayer(String currentName, String newName) {
		if(currentName != newName) return true;
		if(players.stream().anyMatch(p -> p.getName().equals(newName))) return false;
		for(Player player : players) {
			if(player.getName().equals(currentName)) {
				player.setName(newName);
				return true;
			}
		}
		return false;
	}

	public boolean areAllMinesRevealed() {
		for(Coord mine : mines) {
			if(whoRevealed(mine) == null) {
				return false;
			}
		}
		return hasGeneratedMines();
	}

	public Player whoRevealed(Coord coord) {
		for(Player player : players) {
			if(player.hasRevealed(coord)) {
				return player;
			}
		}
		return null;
	}
	public boolean open(String playername, Coord coord) {
		Player revealer = whoRevealed(coord);
		if(revealer != null) return revealer.getName().equals(playername);

		for(Player player : players) {
			if(player.getName().equals(playername)) {
				player.reveal(coord);
				currentPlayerIndex = (currentPlayerIndex+1) % players.size();
				return true;
			}
		}
		return false;
	}

	public void newPlayer(String playerName) {
		players.add(new Player(playerName));
	}


}

