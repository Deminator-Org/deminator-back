package com.natami.deminator.back.model;

import java.security.cert.X509CRL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.natami.deminator.back.entities.EntityCell;
import com.natami.deminator.back.entities.EntityRoom;
import com.natami.deminator.back.util.Coord;
import com.natami.deminator.back.util.InvalidSettingsException;

public class Room implements EntityRoom {
	public static final String VALID_ROOM_NUMBER_REGEXP = "^[0-9A-Za-z]{4,12}$";
	public static final int DEFAULT_SQUARE_SIZE = 16;
	public static final int DEFAULT_MINES_COUNT = 16;

	private int width = DEFAULT_SQUARE_SIZE; // 0 ... x ... width-1
	private int height = DEFAULT_SQUARE_SIZE; // 0 ... y ... height -1
	private int minesCount = DEFAULT_MINES_COUNT;
	private String randomSeed = null;
	private Map<Coord, Cell> cells = new HashMap<>();

	public Room() {
	}

	public void setSeed(String seed) {
		this.randomSeed = seed;
	}

	private void checkSettings() throws InvalidSettingsException {
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
	public void generateGrid() throws InvalidSettingsException {
		checkSettings();

		// generate cells
		for(int y=height-1; y>=0; y--) {
			for(int x=0; x<width; x++) {
				cells.put(new Coord(x,y), new Cell());
			}
		}

		// add mines
		Set<Coord> mines = new HashSet<>();
		while(mines.size() < minesCount) {
			Coord randomCoord = new Coord((int)(Math.random()*this.width), (int)(Math.random()*this.height));
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
	public Map<Coord, ? extends EntityCell> getGrid() {
		return cells;
	}


	// // // STATIC // // //

	public static boolean isValidRoomNumber(String roomNumber) {
		return roomNumber != null && roomNumber.matches(VALID_ROOM_NUMBER_REGEXP);
	}


}
