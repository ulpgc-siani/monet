package client.core.messages;

import cosmos.model.ServerError;

public class DeletingNodeError extends ServerError {

	public DeletingNodeError(String reason) {
		super("error:deletingnode", reason);
	}

}
