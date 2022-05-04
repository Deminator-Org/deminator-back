package com.natami.deminator.back.model;

import java.util.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.natami.deminator.back.io.requests.DeminatorSettings;
import com.natami.deminator.back.io.responses.GameData;
import com.natami.deminator.back.io.responses.PublicPlayerData;

@JsonSerialize(as=GameData.class)
public class Game implements GameData {
	private DeminatorSettings settings;
	private final Set<Coord> mines = new HashSet<>();
	private final Map<String, Player> players = new HashMap<>();
	private final Map<Coord, Integer> allRevealedCells = new HashMap<>();
	private int lastSynchronizedTurn = -1;

	public Game() {}

	// // // GameData

	@Override
	public Collection<PublicPlayerData> getPlayers() {
		return new HashSet<>(players.values()); // cast set to required subtype
	}

	@Override
	public DeminatorSettings getSettings() {
		return this.settings;
	}

	@Override
	public Map<Coord, Integer> getRevealed() {
		return allRevealedCells;
	}

	@Override
	public boolean hasGameEnded() {
		int revealedMinesCount = 0;
		for (Integer value : allRevealedCells.values()) {
			if (value < 0) {
				revealedMinesCount++;
			}
		}
		return revealedMinesCount == mines.size();
	}

	// // // Other Functions

	public void reset(DeminatorSettings settings) {
		// Set settings
		this.settings = settings;

		// Reset game
		players.clear();
		mines.clear();

		// Generate mines
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

	public Player whoRevealed(Coord coord) {
		for(Player player : players.values()) {
			if(player.hasRevealed(coord)) {
				return player;
			}
		}
		return null;
	}

	public void newPlayer(String playerId, String playerName) {
		players.put(playerId, new Player(playerName));
	}

	/**
	 * Action when player request to reveal a cell
	 * @param playerId Unique Id of the player who does the action
	 * @param coord Cell requested to reveal
	 * @return Error message, null if everything is ok
	 */
	public String open(String playerId, Coord coord) {
		Player player = players.get(playerId);

		if(player == null) {
			// Player is not in the game
			return "Player is not in the game: " + playerId;
		}

		if(whoRevealed(coord) != null) {
			// Cell already revealed
			return "Cell is already revealed: " + coord;
		}

		int currentTurn = getCurrentTurn();
		if(player.getLastTurnPlayed() >= currentTurn) {
			return "Player has already played this turn: " + currentTurn;
		}

		updateStatus();

		int clue = cascadeReveal(player, coord);
		if(clue >= 0) {
			// Can't play till next turn if found a clue
			player.setLastTurnPlayed(currentTurn);
		}

		return null;
	}

	public Player getPlayerById(String playerId) {
		return players.get(playerId);
	}

	public void updateStatus() {
		int currentTurn = getCurrentTurn();
		if(lastSynchronizedTurn < currentTurn) {
			synchronizeTurn();
		}
	}

	// // // Private functions


	/**
	 * @return Current game turn number (calculated from settings and current date), or -1 if game isn't started yet.
	 */
	private int getCurrentTurn() {
		Date now = new Date();
		if(settings.getStartDate().after(now)) {
			// Game has not started yet
			return -1;
		}

		if(hasGameEnded()) {
			// On game end, stop incrementing current turn
			return lastSynchronizedTurn;
		}

		int msSinceGameBegan = (int)(now.getTime() - settings.getStartDate().getTime());
		if(settings.getTurnDuration() <= 0) {
			// 1ms per turn
			return msSinceGameBegan;
		}

		return msSinceGameBegan / (settings.getTurnDuration()*1000);
	}

	/**
	 * Gather all player data from last turn to public game data
	 */
	private void synchronizeTurn() {
		Set<Coord> alreadySynchronized = allRevealedCells.keySet();

		for (Player player : players.values()) {
			Map<Coord, Integer> revealed = player.getRevealed();
			for(Coord c : revealed.keySet()) {
				if(!alreadySynchronized.contains(c)) {
					allRevealedCells.put(c, revealed.get(c));
				}
			}
		}

		this.lastSynchronizedTurn = getCurrentTurn();
	}

	/**
	 * Reveal the given cell to the player. Reveal also surrounding if current cell is blank.
	 * @param player Who does the reveal
	 * @param coord Where to reveal
	 * @return given coord clue
	 */
	private int cascadeReveal(Player player, Coord coord) {
		if(coord.getX() < 0 || coord.getY() < 0 || coord.getX() >= settings.getWidth() || coord.getY() >= settings.getHeight()) {
			// Out of bounds
			return -1;
		}

		int clue = getClue(coord);
		if(player.hasRevealed(coord)) {
			return clue;
		}

		player.reveal(coord, clue);
		if(clue == 0) {
			// If blank cell, cascade-reveal surrounding cells
			Set<Coord> around = coord.around();
			around.removeAll(allRevealedCells.keySet());
			for (Coord c : around) {
				cascadeReveal(player, c);
			}
		}

		return clue;
	}

	/**
	 * @param c coordinate to check
	 * @return -1 if c is a mine; or else the number of neighbor mines (0-9)
	 */
	private int getClue(Coord c) {
		if(mines.contains(c)) return -1;

		Set<Coord> around = c.around();
		around.retainAll(mines);
		return around.size();
	}
}

