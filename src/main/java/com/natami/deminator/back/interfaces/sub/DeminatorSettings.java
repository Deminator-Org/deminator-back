package com.natami.deminator.back.interfaces.sub;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeminatorSettings {

	private int width;
	private int height;
	private int minesCount;

	@JsonProperty(value="width")
	public Integer getWidth() {
		return width;
	}

	@JsonProperty(value="height")
	public Integer getHeight() {
		return height;
	}

	@JsonProperty(value="mines")
	public Integer getMinesCount() {
		return minesCount;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setMinesCount(int minesCount) {
		this.minesCount = minesCount;
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
