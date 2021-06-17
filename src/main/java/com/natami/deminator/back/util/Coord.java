package com.natami.deminator.back.util;

public class Coord {
	private final int x;
	private final int y;

	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
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

	public String toString() {
		return x + "," + y;
	}
}
