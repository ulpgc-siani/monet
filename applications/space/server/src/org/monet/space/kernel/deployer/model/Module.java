package org.monet.space.kernel.deployer.model;

import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;

public class Module {

	public static final int UNKNOWN_MODULE = 0;
	public static final int DEFINITION_MODULE = 1;
	public static final int DATASTORE_MODULE = 2;
	public static final int RESOURCE_MODULE = 3;
	public static final String PATH_NAME_SEPARATOR = "/";

	private Document document;
	private File resource;
	private int type;
	private String code;
	private String nameDefinition;
	private boolean isOutdated;
	private boolean hasProblems;

	public void setDocument(Document document) {
		this.document = document;
	}

	public Document getDocument() {
		return document;
	}

	public void setResource(File resource) {
		this.resource = resource;
	}

	public File getResource() {
		return resource;
	}

	public String getName() {
		return resource.getName();
	}

	public void setNameDefinition(String nameDefinition) {
		this.nameDefinition = nameDefinition;
	}

	public String getNameDefinition() {
		return nameDefinition;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public Module(File resource, Document doc) {
		this.resource = resource;
		this.document = doc;
		this.type = UNKNOWN_MODULE;
	}

	public Module(File resource) {
		this.resource = resource;
		this.type = RESOURCE_MODULE;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public boolean isOutdated() {
		return isOutdated;
	}

	public void setOutdated(boolean outdated) {
		this.isOutdated = outdated;
	}

	public void setHasProblems(boolean hasProblems) {
		this.hasProblems = hasProblems;
	}

	public boolean HasProblems() {
		return hasProblems;
	}

	public boolean isDefinition() {
		return this.type == DEFINITION_MODULE;
	}

	public void flush() throws Exception {
		if (this.document == null) return;
		FileOutputStream oOutput = new FileOutputStream(this.resource);
		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.transform(new DOMSource(this.document), new StreamResult(oOutput));
		oOutput.close();
	}


}
