package client.core.messages;

import cosmos.model.ServerError;

public class CreatingNodeError extends ServerError {

	public CreatingNodeError(String reason) {
		super("error:creatingnode", reason);
	}

}
