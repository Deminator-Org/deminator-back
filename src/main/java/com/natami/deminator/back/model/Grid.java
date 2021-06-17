package com.natami.deminator.back.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.natami.deminator.back.entities.EntityCell;
import com.natami.deminator.back.entities.EntityGrid;
import com.natami.deminator.back.util.Coord;
import com.natami.deminator.back.util.InvalidSettingsException;

public class Grid implements EntityGrid {
	private final int width; // 0 ... x ... width-1
	private final int height; // 0 ... y ... height -1
	private Map<Coord, Cell> cells = new HashMap<>();

	public Grid(int width, int height, int minesCount, String genSeed) throws InvalidSettingsException {
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

		// generate cells
		for(int y=height-1; y>=0; y--) {
			for(int x=0; x<width; x++) {
				cells.put(new Coord(x,y), new Cell());
			}
		}

		// add mines
		Set<Coord> mines = new HashSet<>();
		while(mines.size() < minesCount) {
			Coord randomCoord = new Coord(r.nextInt(this.width), r.nextInt(this.height));
			mines.add(randomCoord);
		}
		for(Coord coord : mines) {
			cells.get(coord).setAsMine(true);
		}
	}
	public Cell getCell(int x, int y) {
		return cells.get(new Coord(x, y));
	}

	public void openAllCells() {
		cells.forEach((x, c) -> c.setAsOpen(true));
	}

	@Override
	public Map<Coord, ? extends EntityCell> getCells() {
		return cells;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}
}
