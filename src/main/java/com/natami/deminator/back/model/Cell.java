package com.natami.deminator.back.model;

import com.natami.deminator.back.entities.EntityCell;

public class Cell implements EntityCell {
	private boolean isMine = false;
	private boolean isOpen = false;

	public void setAsMine(boolean isMine) {
		this.isMine = isMine;
	}
	public void setAsOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}

	@Override
	public Boolean isMine() {
		return isMine; // isOpen() ? isMine : null;
	}
}
