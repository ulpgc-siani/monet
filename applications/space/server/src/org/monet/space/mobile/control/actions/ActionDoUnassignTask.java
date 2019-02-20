package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.requests.UnassignTaskRequest;
import org.monet.mobile.service.results.AckResult;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Account;

public class ActionDoUnassignTask extends AuthenticatedTypedAction<UnassignTaskRequest, AckResult> {

	TaskLayer taskLayer;

	public ActionDoUnassignTask() {
		super(UnassignTaskRequest.class);
		this.taskLayer = ComponentPersistence.getInstance().getTaskLayer();
	}

	@Override
	protected AckResult onExecute(UnassignTaskRequest request) throws ActionException {
		Account account = this.getAccount();


		this.taskLayer.unassignJob(account, request.TaskId);

		return new AckResult();
	}

}
