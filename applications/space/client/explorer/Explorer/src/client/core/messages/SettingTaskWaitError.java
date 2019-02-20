package client.core.messages;

import cosmos.model.ServerError;

public class SettingTaskWaitError extends ServerError {

	public SettingTaskWaitError(String reason) {
		super("error:settingtaskwait", reason);
	}

}
