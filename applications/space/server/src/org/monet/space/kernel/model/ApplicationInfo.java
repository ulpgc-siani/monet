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
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.utils.Resources;
import org.monet.space.kernel.utils.StreamHelper;
import org.xmlpull.v1.XmlSerializer;

import java.io.*;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ApplicationInfo extends BaseObject implements ILoadListener {
	private String codeType;
	private String className;
	private HashMap<String, String> labels;

	private static final String INFO = "Info";

	public ApplicationInfo(String code) {
		super();
		this.setCode(code);
		this.codeType = Strings.EMPTY;
		this.className = Strings.EMPTY;
		this.labels = new HashMap<String, String>();
		this.linkLoadListener(this);
	}

	private void loadInfo() {
		Configuration configuration = Configuration.getInstance();
		String dirname = Strings.BAR45 + this.code;
		String filename = configuration.getValue(org.monet.space.kernel.configuration.Configuration.APPLICATION_DEFINITION_FILENAME);

		InputStream stream = null;
		try {
			stream = Resources.getAsStream(dirname + Strings.BAR45 + filename);
			InputStreamReader reader = new InputStreamReader(stream);
			this.deserializeFromXML(reader);
		} finally {
			StreamHelper.close(stream);
		}
	}

	public String getCodeType() {
		onLoad(this, ApplicationInfo.INFO);
		return this.codeType;
	}

	public String getClassName() {
		onLoad(this, ApplicationInfo.INFO);
		return this.className;
	}

	public String getLabel(String codeLanguage) {
		onLoad(this, ApplicationInfo.INFO);
		if (!this.labels.containsKey(codeLanguage)) return Strings.EMPTY;
		return this.labels.get(codeLanguage);
	}

	public void deserializeFromXML(String content) {
		StringReader reader;

		if (content.equals(Strings.EMPTY)) return;
		while (!content.substring(content.length() - 1).equals(">"))
			content = content.substring(0, content.length() - 1);

		reader = new StringReader(content);
		this.deserializeFromXML(reader);
	}

	@SuppressWarnings("unchecked")
	public void deserializeFromXML(Reader reader) {
		SAXBuilder builder = new SAXBuilder();
		org.jdom.Document document;
		Element node;
		List<Element> labels;
		Iterator<Element> iterator;

		try {
			document = builder.build(reader);
			node = document.getRootElement();
			if (node.getAttribute("code") != null) this.code = node.getAttributeValue("code");
			if (node.getAttribute("type") != null) this.codeType = node.getAttributeValue("type");
			if (node.getAttribute("class") != null) this.className = node.getAttributeValue("class");

			labels = node.getChild("labels").getChildren("label");
			iterator = labels.iterator();

			while (iterator.hasNext()) {
				Element element = iterator.next();
				this.labels.put(element.getAttributeValue("language"), element.getText());
			}

		} catch (JDOMException exception) {
			throw new DataException(ErrorCode.UNSERIALIZE_APPLICATION_FROM_XML, null, exception);
		} catch (IOException exception) {
			throw new DataException(ErrorCode.UNSERIALIZE_APPLICATION_FROM_XML, null, exception);
		}
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
		this.loadInfo();
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

}