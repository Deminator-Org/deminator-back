package com.natami.deminator.back.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Coord {
	private final int x;
	private final int y;

	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@JsonCreator
	public Coord(String xy) {
		String[] xySplit = xy.split(",");
		this.x = Integer.parseInt(xySplit[0]);
		this.y = Integer.parseInt(xySplit[1]);
	}

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

	@JsonValue
	public String toString() {
		return x + "," + y;
	}
}
