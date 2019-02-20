package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.requests.LoadNewAssignedTasksRequest;
import org.monet.mobile.service.results.DownloadTasksResult;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.components.layers.TaskLayer;

import java.util.Date;

public class ActionDoLoadNewAssignedTasks extends AuthenticatedTypedAction<LoadNewAssignedTasksRequest, DownloadTasksResult> {

	TaskLayer taskLayer;
	SourceLayer sourceLayer;

	public ActionDoLoadNewAssignedTasks() {
		super(LoadNewAssignedTasksRequest.class);
		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		this.taskLayer = componentPersistence.getTaskLayer();
		this.sourceLayer = componentPersistence.getSourceLayer();
	}

	@Override
	protected DownloadTasksResult onExecute(LoadNewAssignedTasksRequest request) throws ActionException {
		DownloadTasksResult result = new DownloadTasksResult();

		result.Tasks = taskLayer.loadJobsNotRead(getAccount().getId(), request.SyncMark);

		result.SyncMark = new Date().getTime();

		return result;
	}

}
