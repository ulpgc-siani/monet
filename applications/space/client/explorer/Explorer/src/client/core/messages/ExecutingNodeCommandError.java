package client.core.messages;

import cosmos.model.ServerError;

public class ExecutingNodeCommandError extends ServerError {

	public ExecutingNodeCommandError(String reason) {
		super("error:loadingnode", reason);
	}

}
