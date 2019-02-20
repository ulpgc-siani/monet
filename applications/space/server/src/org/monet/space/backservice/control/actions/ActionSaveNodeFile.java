package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.library.LibraryFile;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.*;
import java.util.UUID;

public class ActionSaveNodeFile extends Action {

	public ActionSaveNodeFile() {
	}

	@Override
	public String execute() {
		String name = (String) this.parameters.get(Parameter.NAME);
		InputStream fileStream = (InputStream) this.parameters.get(Parameter.DATA);
		MimeTypes mimeTypes = MimeTypes.getInstance();
		File tempFile = null;
		FileOutputStream tempFileStream = null;
		FileInputStream sourceStream = null;
		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();

		try {
			tempFile = File.createTempFile(UUID.randomUUID().toString(), "");
			tempFileStream = new FileOutputStream(tempFile);
			StreamHelper.copyData(fileStream, tempFileStream);
			sourceStream = new FileInputStream(tempFile);
			String contentType = getContentType(name, tempFile);
			componentDocuments.uploadDocument(name, sourceStream, contentType, false);
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

		return MessageCode.NODE_FILE_SAVED;
	}

	protected String getContentType(String name, File file) {
		MimeTypes mimeTypes = MimeTypes.getInstance();

		if (!name.contains("."))
			return mimeTypes.getFromFile(file);

		String extension = LibraryFile.getExtension(name);
		String contentType = mimeTypes.get(extension);

		if (contentType == MimeTypes.DEFAULT_CONTENT_TYPE)
			contentType = mimeTypes.getFromFile(file);

		return contentType;
	}

}
