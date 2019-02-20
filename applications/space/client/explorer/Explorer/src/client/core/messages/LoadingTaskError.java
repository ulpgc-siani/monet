package client.core.messages;

import cosmos.model.ServerError;

public class LoadingTaskError extends ServerError {

	public LoadingTaskError(String reason) {
		super("error:loadingtask", reason);
	}

}
