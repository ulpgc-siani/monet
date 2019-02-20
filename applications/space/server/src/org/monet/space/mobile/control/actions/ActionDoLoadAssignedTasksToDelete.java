package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.requests.LoadAssignedTasksToDeleteRequest;
import org.monet.mobile.service.results.DownloadTasksToDeleteResult;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Account;

public class ActionDoLoadAssignedTasksToDelete extends AuthenticatedTypedAction<LoadAssignedTasksToDeleteRequest, DownloadTasksToDeleteResult> {

	TaskLayer taskLayer;
	SourceLayer sourceLayer;

	public ActionDoLoadAssignedTasksToDelete() {
		super(LoadAssignedTasksToDeleteRequest.class);
		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		this.taskLayer = componentPersistence.getTaskLayer();
		this.sourceLayer = componentPersistence.getSourceLayer();
	}

	@Override
	protected DownloadTasksToDeleteResult onExecute(LoadAssignedTasksToDeleteRequest request) throws ActionException {
		DownloadTasksToDeleteResult result = new DownloadTasksToDeleteResult();
		Account account = this.getAccount();

		result.TasksToDelete = this.taskLayer.loadAssignedJobsToDelete(account.getId(), request.Ids);

		return result;
	}

}
