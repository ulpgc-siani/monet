package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.requests.LoadUnassignedTasksToDeleteRequest;
import org.monet.mobile.service.results.DownloadTasksToDeleteResult;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.components.layers.TaskLayer;

public class ActionDoLoadUnassignedTasksToDelete extends AuthenticatedTypedAction<LoadUnassignedTasksToDeleteRequest, DownloadTasksToDeleteResult> {

	TaskLayer taskLayer;
	SourceLayer sourceLayer;

	public ActionDoLoadUnassignedTasksToDelete() {
		super(LoadUnassignedTasksToDeleteRequest.class);
		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		this.taskLayer = componentPersistence.getTaskLayer();
		this.sourceLayer = componentPersistence.getSourceLayer();
	}

	@Override
	protected DownloadTasksToDeleteResult onExecute(LoadUnassignedTasksToDeleteRequest request) throws ActionException {
		DownloadTasksToDeleteResult result = new DownloadTasksToDeleteResult();

		result.TasksToDelete = this.taskLayer.loadUnassignedJobsToDelete(request.Ids);

		return result;
	}

}
