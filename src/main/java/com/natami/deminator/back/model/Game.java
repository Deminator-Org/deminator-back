package com.natami.deminator.back.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.natami.deminator.back.interfaces.GameData;
import com.natami.deminator.back.interfaces.sub.DeminatorSettings;

public class Game implements GameData {
	private int width;
	private int height;
	private int minesCount;
	private final Set<Coord> mines = new HashSet<>();
	private final List<Player> players = new ArrayList<>();
	private int currentPlayerIndex = 0;

	public Game(DeminatorSettings settings) {
		this.width = settings.getWidth();
		this.height = settings.getHeight();
		this.minesCount = settings.getMinesCount();

		this.generateMines();
	}


	@Override
	public Collection<Coord> getGrid() {
		return mines;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setMinesCount(int minesCount) {
		this.minesCount = minesCount;
	}

	@Override
	public Collection<Player> getPlayers() {
		return players;
	}

	// Add minecount different coords in the mines set except for the given coord
	public void generateMines() {
		mines.clear();
		while(mines.size() < minesCount) {
			Coord mine = new Coord((int) (Math.random() * width), (int) (Math.random() * height));
			if(!mines.contains(mine)) {
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

	@Override
	public String getCurrentPlayerName() {
		return players.get(currentPlayerIndex).getName();
	}
}

