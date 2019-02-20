package client.core.messages;

import cosmos.model.ServerError;

public class LogoutError extends ServerError {

	public LogoutError(String reason) {
		super("error:logout", reason);
	}

}
