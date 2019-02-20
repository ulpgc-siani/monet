package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.requests.AssignTaskRequest;
import org.monet.mobile.service.results.AckResult;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Account;

public class ActionDoAssignTask extends AuthenticatedTypedAction<AssignTaskRequest, AckResult> {

	TaskLayer taskLayer;

	public ActionDoAssignTask() {
		super(AssignTaskRequest.class);
		this.taskLayer = ComponentPersistence.getInstance().getTaskLayer();
	}

	@Override
	protected AckResult onExecute(AssignTaskRequest request) throws ActionException {
		Account account = this.getAccount();

		this.taskLayer.assignJob(account, request.TaskId);

		return new AckResult();
	}

}
