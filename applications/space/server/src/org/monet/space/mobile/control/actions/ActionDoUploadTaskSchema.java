package org.monet.space.mobile.control.actions;

import org.monet.metamodel.PlaceProperty;
import org.monet.metamodel.ProcessDefinition;
import org.monet.mobile.model.TaskMetadata;
import org.monet.mobile.service.results.AckResult;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.constants.TaskState;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.model.JobCallbackTask;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.StreamHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

public class ActionDoUploadTaskSchema extends AuthenticatedAction {

	@Override
	public void onExecute(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		File messageFile = null;
		File messageDir = null;

		try {
			TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();

			String taskId = httpRequest.getParameter(ID);
			messageFile = this.getRequestContentAsFile(httpRequest, false);
			messageDir = new File(messageFile.getAbsolutePath() + "_content/");

			if (messageDir.exists())
				AgentFilesystem.removeDir(messageDir);

			AgentFilesystem.forceDir(messageDir.getAbsolutePath());

			Task task = taskLayer.loadTask(taskId);
			if (task == null)
				throw new RuntimeException(String.format("Task '%s' not found", taskId));

			TaskMetadata metadata = generateMetadata(task);
			addSchemaAsAttachment(task, messageFile);
			Message message = generateMessage(task, metadata, messageFile, messageDir);
			saveJobResponse(task, messageFile);
			processJob(task, message);

			task.setState(TaskState.FINISHED);
			task.setStartDate(metadata.getStartDate());
			task.setEndDate(metadata.getEndDate());
			taskLayer.saveTask(task);

			this.writeResponse(httpResponse, new AckResult());
		}
		finally {
			if (messageFile != null)
				messageFile.delete();
			if (messageDir != null)
				AgentFilesystem.removeDir(messageDir);
		}
	}

	private void addSchemaAsAttachment(Task task, File messageFile) throws FileNotFoundException {
		FileInputStream stream = null;

		try {
			stream = new FileInputStream(messageFile);

			ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
			TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
			String realFileId = task.getId() + "_" + ".schema";

			boolean registerAttachmentInDatabase = true;
			if (componentDocuments.existsDocument(realFileId)) {
				componentDocuments.removeDocument(realFileId);
				registerAttachmentInDatabase = false;
			}

			componentDocuments.uploadDocument(realFileId, stream, MimeTypes.XML, false);

			if (registerAttachmentInDatabase)
				taskLayer.addJobAttachment(task.getId(), ".schema");

		} catch (FileNotFoundException e) {
			String errorMessage = String.format("Could not process job schema. Id: %s", task.getId());
			this.agentLogger.error(errorMessage, e);
			throw e;
		}
		finally {
			StreamHelper.close(stream);
		}
	}

	private TaskMetadata generateMetadata(Task task) {
		TaskMetadata metadata = new TaskMetadata();

		metadata.setId(task.getId());
		metadata.setCode(task.getCode());
		metadata.setStartDate(task.getInternalStartDate());
		metadata.setEndDate(task.getInternalEndDate());

		return metadata;
	}

	private Message generateMessage(Task task, TaskMetadata metadata, File content, File messageDir) throws IOException {
		FileInputStream stream = null;
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		List<String> attachments = taskLayer.loadJobAttachments(task.getId());
		Message message = new Message();
		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
		InputStream fileContent = null;

		try {
			stream = new FileInputStream(content);

			message.setType(Message.Type.JOB);
			message.setMetadata(metadata);

			for (String attachment : attachments) {
				String realFileId = task.getId() + "_" + attachment;
				File entryFile = new File(messageDir, attachment);
				FileOutputStream entryOutputStream = null;

				if (!entryFile.exists())
					entryFile.createNewFile();

				try {
					entryOutputStream = new FileOutputStream(entryFile);
					fileContent = componentDocuments.getDocumentContent(realFileId);
					org.apache.commons.io.IOUtils.copy(fileContent, entryOutputStream);
				} finally {
					StreamHelper.close(entryOutputStream);
					StreamHelper.close(fileContent);
				}

				message.addAttachment(new Message.MessageAttach(attachment, entryFile));
			}

		} catch (IOException e) {
			String errorMessage = String.format("Could not process job content. Id: %s", task.getId());
			this.agentLogger.error(errorMessage, e);
			throw e;
		}
		finally {
			StreamHelper.close(stream);
		}

		return message;
	}

	private void saveJobResponse(Task task, File messageFile) throws FileNotFoundException {
		FileInputStream contentStream = null;
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		try {
			contentStream = new FileInputStream(messageFile);
			taskLayer.saveJobResponse(task.getId(), contentStream);
		} catch (FileNotFoundException e) {
			this.agentLogger.error(String.format("Could not save job response. Job id: %s", task.getId()), e);
			throw e;
		} finally {
			StreamHelper.close(contentStream);
		}
	}

	private void processJob(Task task, Message message) {
		AgentNotifier agentNotifier = AgentNotifier.getInstance();
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		String taskId = task.getId();

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
	}

}
