package client.core.messages;

import cosmos.model.ServerError;

public class SelectingTaskDelegationRoleError extends ServerError {

	public SelectingTaskDelegationRoleError(String reason) {
		super("error:selectingtaskdelegationrole", reason);
	}

}
