package com.natami.deminator.back.io.requests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeminatorSettings {

	private int width;
	private int height;
	private int minesCount;
	private int seed;
	private int turnDuration;
	private Date startDate;

	public DeminatorSettings() {
		// Initiate default values
		seed = ((Long)System.currentTimeMillis()).hashCode();
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
		long time = new Date().getTime() + timeout * 1000;
		this.startDate = new Date(time);
	}

	@JsonProperty(value="seed", required=false)
	public void setSeed(String seed) {
		this.seed = seed.hashCode();
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

	public int getSeed() {
		return seed;
	}

	public int getTurnDuration() {
		return turnDuration;
	}

	public Date getStartDate() {
		return startDate;
	}

	// // // Other Functions

	public List<String> validate() {
		List<String> errors = new ArrayList<>();

		if(width <= 0) {
			errors.add("Width is not set or is less than 1");
		}
		if(height <= 0) {
			errors.add("Height is not set or is less than 1");
		}
		if(minesCount <= 0) {
			errors.add("Mines count is not set or is less than 1");
		}
		if(minesCount >= width * height / 2) {
			errors.add("There can't be more Mines than free cells");
		}
		if(startDate.before(new Date())) {
			errors.add("Start date is in the past");
		}
		if(turnDuration < 0) {
			errors.add("Turn duration is lower than 0");
		}
		return errors;
	}
}
