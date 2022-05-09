package com.natami.deminator.back.model;

import java.util.*;

import com.natami.deminator.back.io.requests.SettingsRequest;
import com.natami.deminator.back.io.responses.SettingsResponse;

public class Settings implements SettingsRequest, SettingsResponse {

	private final long date;
	private int width;
	private int height;
	private int minesCount;
	private long seed;
	private int turnDuration;
	private int startTimeout;

	public Settings() {
		// Initiate default values
		seed = ((Long)System.currentTimeMillis()).hashCode();
		date = new Date().getTime();
	}

	// // // As Request

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getMinesCount() {
		return minesCount;
	}

	@Override
	public long getSeed() {
		return seed;
	}

	@Override
	public int getTurnDuration() {
		return turnDuration;
	}

	@Override
	public int getStartTimeout() {
		return startTimeout;
	}

	// // // As Response

	@Override
	public Date getStartDate() {
		return new Date(date + startTimeout * 1000);
	}


	// // // Other Functions

	public Set<String> validate() {
		Set<String> errors = new HashSet<>();

		if(width <= 0) {
			errors.add("Width should be grater than or equal to 1");
		}
		if(height <= 0) {
			errors.add("Height should be greater than or equal to 1");
		}
		if(minesCount <= 0) {
			errors.add("Mines count should be greater than or equal to 1");
		}
		if(width > 0 && height > 0 && minesCount > width * height / 2) {
			errors.add("Mines count should be lower than half the grid size (max:" + (width * height / 2) + ")");
		}
		if(startTimeout < 0) {
			errors.add("Start timeout should be greater than or equal to 0");
		}
		if(turnDuration < 0) {
			errors.add("Turn duration should be greater than or equal to 0");
		}
		return errors;
	}
}
