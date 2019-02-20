package client.core.messages;

import cosmos.model.ServerError;

public class AbortingTaskError extends ServerError {

	public AbortingTaskError(String reason) {
		super("error:abortingtask", reason);
	}

}
