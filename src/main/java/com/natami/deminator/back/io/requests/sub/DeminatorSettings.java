package com.natami.deminator.back.io.requests.sub;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeminatorSettings {

	private int width;
	private int height;
	private int minesCount;
	private int seed;

	public DeminatorSettings() {
		// Initiate random seed by default
		seed = ((Long)System.currentTimeMillis()).hashCode();
	}

	@JsonProperty(value="width")
	public void setWidth(int width) {
		this.width = width;
	}

	@JsonProperty(value="height")
	public void setHeight(int height) {
		this.height = height;
	}

	@JsonProperty(value="mines")
	public void setMinesCount(int minesCount) {
		this.minesCount = minesCount;
	}

	@JsonProperty(value="seed", required=false)
	public void setSeed(Object seed) {
		this.seed = seed.hashCode();
	}


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
		return errors;
	}
}
