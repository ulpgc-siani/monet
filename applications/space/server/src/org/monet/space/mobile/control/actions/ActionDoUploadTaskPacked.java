package org.monet.space.mobile.control.actions;

import org.monet.metamodel.PlaceProperty;
import org.monet.metamodel.ProcessDefinition;
import org.monet.mobile.model.TaskMetadata;
import org.monet.mobile.service.results.AckResult;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.constants.TaskState;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.JobCallbackTask;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.utils.MessageHelper;
import org.monet.space.kernel.utils.StreamHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

public class ActionDoUploadTaskPacked extends AuthenticatedAction {

	@Override
	public void onExecute(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = null;

		Account userAccount = this.getAccount();

		File messageFile = null;
		File messageDir = null;
		String taskId = null;
        String jobId = null;
		boolean isNewTask = false;

		try {
			messageFile = this.getRequestContentAsFile(httpRequest, false);

			messageDir = new File(messageFile.getAbsolutePath() + "_content/");
			messageDir.mkdirs();

			AgentNotifier agentNotifier = AgentNotifier.getInstance();

			Message message = new Message();
			message.setType(Message.Type.JOB);
			MessageHelper.parseMessageContent(messageFile, messageDir, message);

			TaskMetadata metadata = message.getMetadata();
			taskId = metadata.getId();
			if (taskId == null || taskId.equals("-1")) {
				task = taskLayer.createTask(metadata.getCode());
				taskId = task.getId();
				task.setOwner(userAccount.getUser());
                jobId = taskLayer.createJob(task, null, null, null, null);
				isNewTask = true;
			} else {
				task = taskLayer.loadTask(taskId);
			}

			if (!task.getOwnerId().equals(userAccount.getUser().getId()))
				throw new RuntimeException(String.format("User '%s' has no permission to job with id '%s'", userAccount.getUser().getName(), taskId));

			FileInputStream messageStream = null;
			try {
				messageStream = new FileInputStream(messageFile);
				taskLayer.saveJobResponse(taskId, messageStream);
			} finally {
				StreamHelper.close(messageStream);
			}

			if (task.getDefinition() instanceof org.monet.metamodel.SensorDefinition) {
				MonetEvent event = new MonetEvent(MonetEvent.TASK_SENSOR_FINISHED, null, taskId);
				event.addParameter(MonetEvent.PARAMETER_MESSAGE, message);
				agentNotifier.notify(event);
			} else {
				JobCallbackTask jobCallbackTask = taskLayer.getJobCallbackTask(taskId);

				if (jobCallbackTask != null) {
					PlaceProperty placeProperty = ((ProcessDefinition) taskLayer.loadTask(jobCallbackTask.getId()).getDefinition()).getPlace(jobCallbackTask.getCode());

					MonetEvent event = new MonetEvent(MonetEvent.TASK_FINISHED_JOB_MESSAGE, null, jobCallbackTask.getId());
					event.addParameter(MonetEvent.PARAMETER_CODE, task.getCode());
					event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
					event.addParameter(MonetEvent.PARAMETER_ACTION, placeProperty.getSendJobActionProperty().getCode());
					event.addParameter(MonetEvent.PARAMETER_MESSAGE, message);
					agentNotifier.notify(event);
				}
			}

			task.setState(TaskState.FINISHED);
			task.setStartDate(metadata.getStartDate());
			task.setEndDate(metadata.getEndDate());
			taskLayer.saveTask(task);

			this.writeResponse(httpResponse, new AckResult());
		} catch (Exception ex) {
			if (isNewTask) {
				taskLayer.deleteTask(taskId);
                taskLayer.removeJob(jobId);
			}
			agentLogger.error(ex);
		} finally {
			AgentFilesystem.removeDir(messageDir);

			if (messageFile != null)
				messageFile.delete();
		}
	}

}
