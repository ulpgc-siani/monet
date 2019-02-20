package client.core.messages;

import cosmos.model.ServerError;

public class SettingTaskDelegationOrderError extends ServerError {

	public SettingTaskDelegationOrderError(String reason) {
		super("error:settingtasksorder", reason);
	}

}
