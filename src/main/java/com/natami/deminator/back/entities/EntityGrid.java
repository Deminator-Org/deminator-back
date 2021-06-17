package com.natami.deminator.back.entities;

import java.util.Map;

import com.natami.deminator.back.util.Coord;

public interface EntityGrid {
	public Map<Coord, ? extends EntityCell> getCells();
	public int getWidth();
	public int getHeight();
}
