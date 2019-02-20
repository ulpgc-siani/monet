package client.core.messages;

import cosmos.model.ServerError;

public class LoadingSpaceError extends ServerError {

	public LoadingSpaceError(String reason) {
		super("error:loadingspace", reason);
	}

}
