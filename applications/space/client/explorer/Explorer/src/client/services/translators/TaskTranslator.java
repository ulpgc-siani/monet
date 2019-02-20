package client.services.translators;

import client.core.model.User;
import client.core.model.Task;
import client.core.model.workmap.DelegationAction.Message;
import client.core.model.workmap.LineAction.Stop;
import cosmos.types.Date;

public interface TaskTranslator {
	String getTaskAssignedToMeMessage();
	String getTaskAssignedToMeBySenderMessage(User sender);
	String getTaskAssignedToUserMessage(User Owner);
	String getTaskAssignedToUserBySenderMessage(User owner, User sender);
	String getTaskDateMessage(Date createDate, Date updateDate);
	String getTaskNotificationsMessage(int count);
	String getTaskDelegationMessage(Message message, Date failureDate, String roleTypeLabel);
	String getTaskLineTimeoutMessage(Date timeout, Stop timeoutStop);
	String getTaskWaitMessage(Date dueDate);

	String getTaskStateLabel(Task.State state);
	String getTaskTypeIcon(Task.Type type);
}
