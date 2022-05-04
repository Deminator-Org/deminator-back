package com.natami.deminator.back.model;

import java.util.*;

import com.natami.deminator.back.io.requests.sub.DeminatorSettings;
import com.natami.deminator.back.io.responses.GameData;

public class Game implements GameData {
	private DeminatorSettings settings;
	private final Set<Coord> mines = new HashSet<>();
	private final List<Player> players = new ArrayList<>();
	private final Map<Coord, Set<Player>> allRevealedCells = new HashMap<>();
	private int currentPlayerIndex = 0;
	private int lastSynchronizedTurn = -1;

	public Game() {}

	// // // GameData

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
	public Set<Coord> getRevealedMines() {
		Set<Coord> revealedCells = new HashSet<>();
		for (Player player : players) {
			revealedCells.addAll(player.getRevealed());
		}
		revealedCells.retainAll(mines);
		return revealedCells;
	}

	@Override
	public boolean hasGameEnded() {
		return getRevealedMines().size() == mines.size();
	}

	// // // Other Functions

	public void reset(DeminatorSettings settings) {
		// Set settings
		this.settings = settings;

		// Reset game
		currentPlayerIndex = 0;
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

	public boolean renamePlayer(String currentName, String newName) {
		if(!currentName.equals(newName)) return true;
		if(players.stream().anyMatch(p -> p.getName().equals(newName))) return false;
		for(Player player : players) {
			if(player.getName().equals(currentName)) {
				player.setName(newName);
				return true;
			}
		}
		return false;
	}

	public Player whoRevealed(Coord coord) {
		for(Player player : players) {
			if(player.hasRevealed(coord)) {
				return player;
			}
		}
		return null;
	}

	public void newPlayer(String playerName) {
		players.add(new Player(playerName));
	}

	public boolean open(String playername, Coord coord) {
		Player player = getPlayerByName(playername);

		if(player == null) {
			// Player is not in the game
			return false;
		}

		if(whoRevealed(coord) != null) {
			// Cell already revealed
			return player.hasRevealed(coord);
		}

		int currentTurn = getCurrentTurn();
		if(player.getLastTurnPlayed() >= currentTurn) {
			// Player has already played this turn
			return false;
		}

		if(lastSynchronizedTurn < currentTurn) {
			synchronizeTurn();
		}

		player.setLastTurnPlayed(currentTurn);
		cascadeReveal(player, coord);

		return true;
	}

	// // // Private functions

	private Player getPlayerByName(String playerName) {
		for(Player player : players) {
			if(player.getName().equals(playerName)) {
				return player;
			}
		}
		return null;
	}

	private int getCurrentTurn() {
		if(settings.getStartDate().before(new Date())) {
			return -1;
		}
		if(settings.getTurnDuration() <= 0) {
			// 1ms per turn
			return (int)(new Date().getTime() - settings.getStartDate().getTime());
		}

		return ((int) (new Date().getTime() - settings.getStartDate().getTime()) / settings.getTurnDuration()*1000);
	}

	private void synchronizeTurn() {
		Set<Coord> alreadySynchronized = allRevealedCells.keySet();

		for (Player player : players) {
			for(Coord c : player.getRevealed()) {
				if(!alreadySynchronized.contains(c)) {
					if(!allRevealedCells.containsKey(c)) {
						allRevealedCells.put(c, new HashSet<>());
					}
					allRevealedCells.get(c).add(player);
				}
			}
		}

		this.lastSynchronizedTurn = getCurrentTurn();
	}

	private void cascadeReveal(Player player, Coord coord) {
		player.reveal(coord);

		if(getClue(coord) == 0) {
			Set<Coord> around = coord.around();
			around.removeAll(allRevealedCells.keySet());
			for (Coord c : around) {
				cascadeReveal(player, c);
			}
		}
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

