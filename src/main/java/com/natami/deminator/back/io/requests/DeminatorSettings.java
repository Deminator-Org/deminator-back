package com.natami.deminator.back.io.requests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeminatorSettings {

	private final long date;
	private int width;
	private int height;
	private int minesCount;
	private long seed;
	private int turnDuration;
	private int startTimeout;

	public DeminatorSettings() {
		// Initiate default values
		seed = ((Long)System.currentTimeMillis()).hashCode();
		date = new Date().getTime();
	}

	// // // Request Parameters

	@JsonProperty(value="width", required = true)
	public void setWidth(int width) {
		this.width = width;
	}

	@JsonProperty(value="height", required = true)
	public void setHeight(int height) {
		this.height = height;
	}

	@JsonProperty(value="mines", required = true)
	public void setMinesCount(int minesCount) {
		this.minesCount = minesCount;
	}

	@JsonProperty(value="turnDuration", required = true)
	public void setTurnDuration(int turnDuration) {
		this.turnDuration = turnDuration;
	}

	@JsonProperty(value="startTimeout", required = false, defaultValue = "3")
	public void setStartTimeout(int timeout) {
		this.startTimeout = timeout;
	}

	@JsonProperty(value="seed", required=false)
	public void setSeed(long seed) {
		this.seed = seed;
	}

	// // // Getters

	public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

	public Integer getMinesCount() {
		return minesCount;
	}

	public long getSeed() {
		return seed;
	}

	public int getTurnDuration() {
		return turnDuration;
	}

	public int getStartTimeout() {
		return startTimeout;
	}

	public Date getStartDate() {
		return new Date(date + startTimeout * 1000);
	}

	// // // Other Functions

	public List<String> validate() {
		List<String> errors = new ArrayList<>();

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
