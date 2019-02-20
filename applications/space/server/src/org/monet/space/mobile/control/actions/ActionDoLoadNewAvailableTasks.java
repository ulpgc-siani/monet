package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.requests.LoadNewAvailableTasksRequest;
import org.monet.mobile.service.results.DownloadTasksResult;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Account;

import java.util.Date;

public class ActionDoLoadNewAvailableTasks extends AuthenticatedTypedAction<LoadNewAvailableTasksRequest, DownloadTasksResult> {

	TaskLayer taskLayer;
	SourceLayer sourceLayer;

	public ActionDoLoadNewAvailableTasks() {
		super(LoadNewAvailableTasksRequest.class);
		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		this.taskLayer = componentPersistence.getTaskLayer();
		this.sourceLayer = componentPersistence.getSourceLayer();
	}

	@Override
	protected DownloadTasksResult onExecute(LoadNewAvailableTasksRequest request) throws ActionException {
		DownloadTasksResult result = new DownloadTasksResult();
		Account account = this.getAccount();

		result.Tasks = this.taskLayer.loadAvailableJobsNotRead(account.getId(), request.SyncMark);

		result.SyncMark = (new Date()).getTime();

		return result;
	}

}
