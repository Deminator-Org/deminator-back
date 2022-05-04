package com.natami.deminator.back.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashSet;
import java.util.Set;

public class Coord {
	private final int x;
	private final int y;

	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// // // In Requests & Responses

	@JsonCreator
	public Coord(String xy) {
		String[] xySplit = xy.split(",");
		this.x = Integer.parseInt(xySplit[0]);
		this.y = Integer.parseInt(xySplit[1]);
	}

	@JsonValue
	public String toString() {
		return x + "," + y;
	}

	// // // Functions

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int hashCode() {
		int tmp = y + ((x+1)/2);
		return x + (tmp * tmp);
	}
	public boolean equals(Object o) {
		if(!(o instanceof Coord)) {
			return false;
		}
		Coord c2 = (Coord) o;
		return this.x == c2.x && this.y == c2.y;
	}

	public Set<Coord> around() {
		Set<Coord> around = new HashSet<>();
		around.add(new Coord(x-1, y-1));
		around.add(new Coord(x  , y-1));
		around.add(new Coord(x+1, y-1));
		around.add(new Coord(x-1, y  ));
		around.add(new Coord(x+1, y  ));
		around.add(new Coord(x-1, y+1));
		around.add(new Coord(x  , y+1));
		around.add(new Coord(x+1, y+1));
		return around;
	}
}
