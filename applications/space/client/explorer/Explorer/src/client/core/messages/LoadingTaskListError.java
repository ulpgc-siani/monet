package client.core.messages;

import cosmos.model.ServerError;

public class LoadingTaskListError extends ServerError {

	public LoadingTaskListError(String reason) {
		super("error:loadingtasklist", reason);
	}

}
