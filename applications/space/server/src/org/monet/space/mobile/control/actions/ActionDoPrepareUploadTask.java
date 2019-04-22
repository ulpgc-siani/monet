package org.monet.space.mobile.control.actions;

import org.monet.mobile.model.TaskMetadata;
import org.monet.mobile.service.results.PrepareUploadTaskResult;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.constants.TaskState;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.utils.PersisterHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

public class ActionDoPrepareUploadTask extends AuthenticatedAction {

	@Override
	public void onExecute(org.monet.http.Request httpRequest, org.monet.http.Response httpResponse) throws Exception {

		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		File messageFile = null;
		String taskId = null;
		String jobId = null;
		boolean isNewTask = false;
		try {
			messageFile = this.getRequestContentAsFile(httpRequest, false);

			TaskMetadata taskMetadata = PersisterHelper.load(messageFile, TaskMetadata.class);
			taskId = taskMetadata.getId();
			if (taskId == null || taskId.equals("-1")) {
				Task task = taskLayer.createTask(taskMetadata.getCode());
				taskId = task.getId();
				task.setOwner(this.getAccount().getUser());
				jobId = taskLayer.createJob(task, null, null, null, null);
				isNewTask = true;

				if (!task.getOwnerId().equals(this.getAccount().getId()))
					throw new RuntimeException(String.format("User '%s' has no permission to job with id '%s'", this.getAccount().getUser().getName(), taskId));

				task.setState(TaskState.FINISHED);
				task.setStartDate(taskMetadata.getStartDate());
				task.setEndDate(taskMetadata.getEndDate());
				taskLayer.saveTask(task);
			}

			PrepareUploadTaskResult result = new PrepareUploadTaskResult();
			result.ID = taskId;
			this.writeResponse(httpResponse, result);
		} catch (Exception ex) {
			if (isNewTask) {
				taskLayer.deleteTask(taskId);
				taskLayer.removeJob(jobId);
			}
			agentLogger.error(ex);
		} finally {
			if (messageFile != null)
				messageFile.delete();
		}
	}

}
