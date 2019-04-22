package org.monet.space.mobile.control.actions;

import org.monet.mobile.service.results.AckResult;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.StreamHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ActionDoUploadTaskFile extends AuthenticatedAction {

	@Override
	public void onExecute(org.monet.http.Request httpRequest, org.monet.http.Response httpResponse) throws Exception {
		File messageFile = null;
		FileInputStream inputStream = null, imageStream = null;
		MimeTypes mimeTypes = MimeTypes.getInstance();
		FileOutputStream tempFileStream = null;

		try {
			TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();

			String taskId = httpRequest.getParameter(ID);
			Task task = taskLayer.loadTask(taskId);
			if (task == null)
				throw new RuntimeException(String.format("Task '%s' not found", taskId));

			String fileId = httpRequest.getParameter(EXTRA);
			messageFile = this.getRequestContentAsFile(httpRequest, true);

			String contentType = mimeTypes.getFromFile(messageFile);
			boolean generatePreview = mimeTypes.isPreviewable(contentType);
			inputStream = new FileInputStream(messageFile);

			String realFileId = taskId + "_" + fileId;
			ComponentDocuments componentDocuments = ComponentDocuments.getInstance();

			boolean registerAttachmentInDatabase = true;
			if (componentDocuments.existsDocument(realFileId)) {
				componentDocuments.removeDocument(realFileId);
				registerAttachmentInDatabase = false;
			}

			if (mimeTypes.isImage(contentType)) {
				imageStream = new FileInputStream(messageFile);
				BufferedImage image = ImageIO.read(imageStream);

				componentDocuments.uploadImage(realFileId, inputStream, contentType, image.getWidth(), image.getHeight());
			}
			else
				componentDocuments.uploadDocument(realFileId, inputStream, contentType, generatePreview);

			if (registerAttachmentInDatabase)
				taskLayer.addJobAttachment(task.getId(), fileId);

			this.writeResponse(httpResponse, new AckResult());
		} finally {
			StreamHelper.close(tempFileStream);
			StreamHelper.close(inputStream);
			if (messageFile != null)
				messageFile.delete();
		}
	}

}
