package client.core.messages;

import cosmos.model.ServerError;

public class LoadingNodeError extends ServerError {

	public LoadingNodeError(String reason) {
		super("error:loadingnode", reason);
	}

}
