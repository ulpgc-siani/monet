package org.monet.space.mobile.control.actions;

import org.monet.mobile.service.requests.DownloadTaskPackedRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionDoDownloadTaskPacked extends AuthenticatedAction {

	@Override
	public void onExecute(org.monet.http.Request httpRequest, org.monet.http.Response httpResponse) throws Exception {
		DownloadTaskPackedRequest request = this.getRequest(httpRequest, DownloadTaskPackedRequest.class);
		Account userAccount = this.getAccount();

		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		String taskId = request.ID;

		if (!taskLayer.loadTask(taskId).getOwnerId().equals(userAccount.getUser().getId()))
			throw new RuntimeException(String.format("User '%s' has no permission to task with id '%s'", userAccount.getUser().getName(), taskId));

		taskLayer.loadJobRequest(taskId, httpResponse.getOutputStream());
		this.writeResponse(httpResponse, "application/vnd.monet.task", null, "task_" + taskId);
	}

}
