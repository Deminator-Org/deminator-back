package com.natami.deminator.back.util;

public class InvalidSettingsException extends Exception {

	public InvalidSettingsException(String string) {
		super("Invalid Settings: " + string);
	}

}
