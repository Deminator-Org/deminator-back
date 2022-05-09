package com.natami.deminator.back.exceptions;

import java.util.Set;

public class InvalidSettingsException extends Exception {

	public InvalidSettingsException(Set<String> messages) {
		super(messages.size() + " error(s):\n\t- " + String.join("\n\t- ", messages));
	}

}
