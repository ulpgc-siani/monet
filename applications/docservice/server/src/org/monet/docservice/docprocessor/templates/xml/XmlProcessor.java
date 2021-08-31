package org.monet.docservice.docprocessor.templates.xml;

import com.google.inject.Inject;
import org.monet.docservice.core.Key;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.templates.DocumentProcessor;
import org.monet.docservice.docprocessor.templates.common.Model;

import java.io.FileOutputStream;
import java.io.InputStream;

public class XmlProcessor implements DocumentProcessor {

	private Logger logger;
	private InputStream dataStream;

	@Inject
	public void injectLogger(Logger logger) {
		this.logger = logger;
	}

	public void setModel(Model model) {
	}

	public void setDocumentKey(Key documentKey) {
	}

	public void setDocument(InputStream dataStream) {
		this.dataStream = dataStream;
	}

	public void process(String sourceDocumentPath) {
		logger.debug("process(%s)", sourceDocumentPath);

		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(sourceDocumentPath, false);

			if (this.dataStream != null) {
				this.dataStream.reset();
				StreamHelper.copy(this.dataStream, outputStream);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException("Error updating document.");
		} finally {
			StreamHelper.close(outputStream);
		}
	}

}
