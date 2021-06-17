package com.natami.deminator.back.entities;

import java.util.Map;

import com.natami.deminator.back.util.Coord;

public interface EntityRoom {
	public Map<Coord, ? extends EntityCell> getGrid();
}
