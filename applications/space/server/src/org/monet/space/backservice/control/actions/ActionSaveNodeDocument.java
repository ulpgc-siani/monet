package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.*;

public class ActionSaveNodeDocument extends Action {

	public ActionSaveNodeDocument() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		InputStream fileStream = (InputStream) this.parameters.get(Parameter.DATA);
		OutputStream tempFileStream = null;
		InputStream sourceStream = null;
		String contentType = (String) this.parameters.get(Parameter.CONTENT_TYPE);
		MimeTypes mimeTypes = MimeTypes.getInstance();
		File tempFile = null;

		try {
			tempFile = File.createTempFile(id, "");
			tempFileStream = new FileOutputStream(tempFile);
			StreamHelper.copyData(fileStream, tempFileStream);
			sourceStream = new FileInputStream(tempFile);

			ComponentDocuments.getInstance().uploadDocument(id, sourceStream, contentType, mimeTypes.isPreviewable(contentType));

		} catch (IOException e) {
			AgentLogger.getInstance().error(e);
			throw new RuntimeException(e);
		}
		finally {
			StreamHelper.close(tempFileStream);
			StreamHelper.close(sourceStream);

			if (tempFile != null && tempFile.exists())
				tempFile.delete();
		}

		return MessageCode.NODE_DOCUMENT_SAVED;
	}

}
