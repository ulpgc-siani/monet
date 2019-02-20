/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.kernel.model;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringReader;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TranslatorInfo extends BaseObject implements ILoadListener {
	private String format;
	private String className;
	private HashMap<String, String> labels;

	private static final String INFO = "Info";

	public TranslatorInfo(String code) {
		super();
		this.code = code;
		this.format = Strings.EMPTY;
		this.className = Strings.EMPTY;
		this.labels = new HashMap<String, String>();
		this.linkLoadListener(this);
	}

	private void loadInfo() {
		Configuration configuration = Configuration.getInstance();
		String dirname = configuration.getAppDataTranslatorsDir() + Strings.BAR45 + this.code;
		String filename = configuration.getValue(Configuration.TRANSLATOR_DEFINITION_FILENAME);
		String content;

		if (!AgentFilesystem.existFile(dirname + Strings.BAR45 + filename)) {
			throw new DataException(ErrorCode.TRANSLATOR_NOT_FOUND, dirname + Strings.BAR45 + filename);
		}

		content = AgentFilesystem.readFile(dirname + Strings.BAR45 + filename);

		this.deserializeFromXML(content);
	}

	public String getFormat() {
		onLoad(this, TranslatorInfo.INFO);
		return this.format;
	}

	public String getClassName() {
		onLoad(this, TranslatorInfo.INFO);
		return this.className;
	}

	public String getLabel(String codeLanguage) {
		onLoad(this, TranslatorInfo.INFO);
		if (!this.labels.containsKey(codeLanguage)) return Strings.EMPTY;
		return this.labels.get(codeLanguage);
	}

	@SuppressWarnings("unchecked")
	public void deserializeFromXML(String content) {
		SAXBuilder builder = new SAXBuilder();
		StringReader reader;
		org.jdom.Document document;
		Element node;
		List<Element> labels;
		Iterator<Element> iterator;

		if (content.equals(Strings.EMPTY)) return;
		while (!content.substring(content.length() - 1).equals(">"))
			content = content.substring(0, content.length() - 1);

		reader = new StringReader(content);

		try {
			document = builder.build(reader);
			node = document.getRootElement();
			if (node.getAttribute("code") != null) this.code = node.getAttributeValue("code");
			if (node.getAttribute("format") != null) this.format = node.getAttributeValue("format");
			if (node.getAttribute("class") != null) this.className = node.getAttributeValue("class");

			labels = node.getChild("labels").getChildren("label");
			iterator = labels.iterator();

			while (iterator.hasNext()) {
				Element element = iterator.next();
				this.labels.put(element.getAttributeValue("language"), element.getText());
			}

		} catch (Exception exception) {
			DataException e = new DataException(ErrorCode.UNSERIALIZE_DEFINITION_FROM_XML, content, exception);
			AgentLogger.getInstance().errorInModel(e);
			throw e;
		}
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}

	public void loadAttribute(EventObject eventObject, String attribute) {
		this.loadInfo();
	}

}