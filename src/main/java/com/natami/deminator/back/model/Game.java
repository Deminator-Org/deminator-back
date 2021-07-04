package com.natami.deminator.back.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.natami.deminator.back.responses.EntityGame;
import com.natami.deminator.back.util.Coord;
import com.natami.deminator.back.util.InvalidSettingsException;

public class Game implements EntityGame {
	private final Room room;
	private final Set<Coord> discoveredCells = new HashSet<>();
	private final Set<Coord> mines = new HashSet<>();
	private final Map<Player, Double> scores = new HashMap<>();
	private int turnNumber = 0;
	private String nextTurnStartTime;

	public Game(Room r) throws InvalidSettingsException {
		this.room = r;
		this.nextTurnStartTime = r.getGameStartTime();
		this.generateGrid();
	}

	private void checkSettings(int minesCount) throws InvalidSettingsException {
		if(room.getWidth() < 1) {
			throw new InvalidSettingsException("width:" + getWidth());
		}
		if(room.getHeight() < 1) {
			throw new InvalidSettingsException("height:" + getHeight());
		}
		if(minesCount >= getWidth()*getHeight()) {
			throw new InvalidSettingsException("minesCount:" + minesCount + ", width:" + getWidth()+ ", height:" + getHeight());
		}
	}
	public void generateGrid() throws InvalidSettingsException {
		checkSettings(room.getMinesCount());
		Random r = new Random(room.getSeed().hashCode());

		// add mines
		while(mines.size() < room.getMinesCount()) {
			Coord randomCoord = new Coord(r.nextInt(getWidth()), r.nextInt(getHeight()));
			mines.add(randomCoord);
		}
	}


	public void update() {
		if(nextTurnStartTime == null) {
			// Game has ended
			return;
		}
		String now = getISOCurrentTime(0);
		if(now.compareTo(nextTurnStartTime) > 0) {
			// get players actions
			for(Player p : room.getPlayerList()) {
				if(p.getActions() != null) {
					discoveredCells.addAll(p.getActions());
					p.setActions(null);
				}
			}

			if(discoveredCells.containsAll(mines)) {
				// End of the game
				nextTurnStartTime = null;
			} else {
				// Set next turn
				turnNumber++;
				nextTurnStartTime = getISOCurrentTime(room.getTurnDuration());
			}
		}
	}

	@Override
	public int getWidth() {
		return room.getWidth();
	}

	@Override
	public int getHeight() {
		return room.getHeight();
	}

	@Override
	public Set<Coord> getMines() {
		return mines;
	}

	@Override
	public Set<Coord> getOpenCells() {
		return discoveredCells;
	}

	@Override
	public Map<String, Integer> getScores() {
		Map<String, Integer> scores = new HashMap<>();
		for(Player p : this.scores.keySet()) {
			int score = (int)((double) this.scores.get(p));
			if(score > 0) {
				scores.put(p.getName(), score);
			}
		}
		return scores;
	}

	@Override
	public String getNextTurnStartTime() {
		return this.nextTurnStartTime;
	}

	@Override
	public int getTurnNumber() {
		return turnNumber;
	}

	private String getISOCurrentTime(int plus) {
		Calendar startTime = Calendar.getInstance();
		startTime.add(Calendar.SECOND, plus);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return formatter.format(startTime.getTime()); // e.g. 2000-12-31T23:59:59
	}
}
