package com.natami.deminator.back.model;

import com.natami.deminator.back.entities.EntityRoom;
import com.natami.deminator.back.util.InvalidSettingsException;

public class Room implements EntityRoom {
	public static final String VALID_ROOM_NUMBER_REGEXP = "^[0-9A-Za-z]{4,12}$";
	public static final int DEFAULT_SQUARE_SIZE = 16;
	public static final int DEFAULT_MINES_COUNT = 16;

	private int width = DEFAULT_SQUARE_SIZE; // 0 ... x ... width-1
	private int height = DEFAULT_SQUARE_SIZE; // 0 ... y ... height -1
	private int minesCount = DEFAULT_MINES_COUNT;
	private String randomSeed = null;
	private Grid grid = null;

	public Room() {
	}

	public void setSeed(String seed) {
		this.randomSeed = seed;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeigth(int height) {
		this.height = height;
	}
	public void setMinesCount(int minesCount) {
		this.minesCount = minesCount;
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
		return this.minesCount;
	}

	@Override
	public Grid getGrid() {
		return this.grid;
	}

	public void start() throws InvalidSettingsException {
		if(this.grid != null) {
			return;
		}
		this.grid = new Grid(this.width, this.height, this.minesCount, this.randomSeed);
	}

	public void stop() {
		this.grid = null;
	}

	// // // STATIC // // //

	public static boolean isValidRoomNumber(String roomNumber) {
		return roomNumber != null && roomNumber.matches(VALID_ROOM_NUMBER_REGEXP);
	}
}
