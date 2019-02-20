package client.core.messages;

import cosmos.model.ServerError;

public class DeletingNodesError extends ServerError {

	public DeletingNodesError(String reason) {
		super("error:deletingnodes", reason);
	}

}
