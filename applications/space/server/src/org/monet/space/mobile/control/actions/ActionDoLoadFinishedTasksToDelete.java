package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.requests.LoadFinishedTasksToDeleteRequest;
import org.monet.mobile.service.results.DownloadTasksToDeleteResult;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Account;

public class ActionDoLoadFinishedTasksToDelete extends AuthenticatedTypedAction<LoadFinishedTasksToDeleteRequest, DownloadTasksToDeleteResult> {

	TaskLayer taskLayer;
	SourceLayer sourceLayer;

	public ActionDoLoadFinishedTasksToDelete() {
		super(LoadFinishedTasksToDeleteRequest.class);
		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		this.taskLayer = componentPersistence.getTaskLayer();
		this.sourceLayer = componentPersistence.getSourceLayer();
	}

	@Override
	protected DownloadTasksToDeleteResult onExecute(LoadFinishedTasksToDeleteRequest request) throws ActionException {
		DownloadTasksToDeleteResult result = new DownloadTasksToDeleteResult();
		Account account = this.getAccount();

		result.TasksToDelete = this.taskLayer.loadFinishedJobsToDelete(account.getId(), request.Ids);

		return result;
	}

}
