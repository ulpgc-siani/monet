package client.core.messages;

import cosmos.model.ServerError;

public class SettingTasksOwnerError extends ServerError {

	public SettingTasksOwnerError(String reason) {
		super("error:settingtasksowner", reason);
	}

}
