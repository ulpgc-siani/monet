package client.core.messages;

import cosmos.model.ServerError;

public class LoadingAccountError extends ServerError {

	public LoadingAccountError(String reason) {
		super("error:loadingaccount", reason);
	}

}
