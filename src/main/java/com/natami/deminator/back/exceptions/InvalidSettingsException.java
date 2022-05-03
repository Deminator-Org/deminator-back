package com.natami.deminator.back.exceptions;

import java.util.List;

public class InvalidSettingsException extends Exception {

	public InvalidSettingsException(List<String> messages) {
		super(messages.size() + " invalid setting(s):\n\t- " + String.join("\n\t- ", messages));
	}

}
