package client.core.messages;

import cosmos.model.ServerError;

public class SolvingTaskEditionError extends ServerError {

	public SolvingTaskEditionError(String reason) {
		super("error:solvingtaskedition", reason);
	}

}
