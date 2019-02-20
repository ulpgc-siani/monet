package client.core.messages;

import cosmos.model.ServerError;

public class LoadingNotificationListError extends ServerError {

	public LoadingNotificationListError(String reason) {
		super("error:loadingnotificationlist", reason);
	}

}
