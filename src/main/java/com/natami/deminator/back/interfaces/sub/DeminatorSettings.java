package com.natami.deminator.back.interfaces.sub;

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
}
