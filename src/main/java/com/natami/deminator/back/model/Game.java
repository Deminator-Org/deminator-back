package com.natami.deminator.back.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.natami.deminator.back.responses.EntityGame;
import com.natami.deminator.back.util.Coord;
import com.natami.deminator.back.util.InvalidSettingsException;

public class Game implements EntityGame {
	private final int width; // 0 ... x ... width-1
	private final int height; // 0 ... y ... height -1
	private Set<Coord> discoveredCells = new HashSet<>();
	private Set<Coord> mines = new HashSet<>();

	public Game(int width, int height, int minesCount, String genSeed) throws InvalidSettingsException {
		this.width = width;
		this.height = height;
		this.generateGrid(minesCount, genSeed);
	}

	private void checkSettings(int minesCount) throws InvalidSettingsException {
		if(width < 1) {
			throw new InvalidSettingsException("width:" + width);
		}
		if(height < 1) {
			throw new InvalidSettingsException("height:" + height);
		}
		if(minesCount >= width*height) {
			throw new InvalidSettingsException("minesCount:" + minesCount + ", width:" + width + ", height:" + height);
		}
	}
	public void generateGrid(int minesCount, String genString) throws InvalidSettingsException {
		checkSettings(minesCount);
		Random r = new Random(genString.hashCode());

		// add mines
		while(mines.size() < minesCount) {
			Coord randomCoord = new Coord(r.nextInt(this.width), r.nextInt(this.height));
			mines.add(randomCoord);
		}
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getMinesCount() {
		return mines.size();
	}

	@Override
	public List<Coord> getMines() {
		return new ArrayList<>(mines);
	}

	@Override
	public List<Coord> getOpenCells() {
		return new ArrayList<>(discoveredCells);
	}

	@Override
	public Map<String, Integer> getScores() {
		return new HashMap<>(); // TODO
	}

	@Override
	public String getNextTurnStartTime() {
		return null; // TODO
	}

	@Override
	public int getTurnNumber() {
		return 0; // TODO
	}
}
