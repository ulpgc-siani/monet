package org.monet.docservice.docprocessor.templates.impl;

import com.google.inject.Inject;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.model.DocumentType;
import org.monet.docservice.docprocessor.templates.DocumentProcessor;
import org.monet.docservice.docprocessor.templates.DocumentReplacer;
import org.monet.docservice.docprocessor.templates.ModelProducer;
import org.monet.docservice.docprocessor.templates.opendocument.OpenDoc;
import org.monet.docservice.docprocessor.templates.openxml.OpenXml;
import org.monet.docservice.docprocessor.templates.portabledocument.PortableDocument;
import org.monet.docservice.docprocessor.templates.xml.XmlDocument;
import org.monet.docservice.docprocessor.templates.xml.XmlProcessor;

import java.io.InputStream;

public class DocumentReplacerImpl implements DocumentReplacer {

	private DocumentProcessor openDocumentProcessor;
	private DocumentProcessor openXmlProcessor;
	private DocumentProcessor portableDocumentProcessor;
	private XmlProcessor xmlDocumentProcessor;
	private ModelProducer modelProducer;
	private Logger logger;

	@Inject
	public void injectLogger(Logger logger) {
		this.logger = logger;
	}

	@Inject
	public void injectOpenDocumentProcessor(@OpenDoc DocumentProcessor openDocumentProcessor) {
		this.openDocumentProcessor = openDocumentProcessor;
	}

	@Inject
	public void injectOpenXmlProcessor(@OpenXml DocumentProcessor openXmlProcessor) {
		this.openXmlProcessor = openXmlProcessor;
	}

	@Inject
	public void injectPortableDocumentProcessor(@PortableDocument DocumentProcessor portableDocumentProcessor) {
		this.portableDocumentProcessor = portableDocumentProcessor;
	}

	@Inject
	public void injectXmlDocumentProcessor(@XmlDocument DocumentProcessor xmlDocumentProcessor) {
		this.xmlDocumentProcessor = (XmlProcessor) xmlDocumentProcessor;
	}

	@Inject
	public void setModelProducer(ModelProducer modelProducer) {
		this.modelProducer = modelProducer;
	}

	public void updateDocument(String documentId, String sDocumentPath, InputStream dataStream, int type) {
		logger.debug("updateDocument(%s, %s, %s, %s)", documentId, sDocumentPath, dataStream, String.valueOf(type));

		DocumentProcessor docProcessor;
		switch (type) {
			case DocumentType.OPEN_DOCUMENT:
				docProcessor = this.openDocumentProcessor;
				break;
			case DocumentType.OPEN_XML:
				docProcessor = this.openXmlProcessor;
				break;
			case DocumentType.XML_DOCUMENT:
				this.xmlDocumentProcessor.setDocument(dataStream);
				docProcessor = this.xmlDocumentProcessor;
				break;
			default: //DocumentType.PORTABLE_DOCUMENT
				docProcessor = this.portableDocumentProcessor;
				break;
		}

		docProcessor.setModel(modelProducer.create(dataStream));
		docProcessor.setDocumentId(documentId);
		docProcessor.process(sDocumentPath);
	}

}
