package client.core.messages;

import cosmos.model.ServerError;

public class SolvingTaskLineError extends ServerError {

	public SolvingTaskLineError(String reason) {
		super("error:solvingtaskline", reason);
	}

}
