package com.natami.deminator.back.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface EntityRoom {
	public int getWidth();
	public int getHeight();

	@JsonProperty("mines")
	public int getMinesCount();
	public EntityGrid getGrid();
}
