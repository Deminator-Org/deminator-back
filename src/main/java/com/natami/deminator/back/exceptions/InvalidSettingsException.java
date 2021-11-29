package com.natami.deminator.back.exceptions;

public class InvalidSettingsException extends Exception {

	public InvalidSettingsException(String string) {
		super("Invalid Settings: " + string);
	}

}
