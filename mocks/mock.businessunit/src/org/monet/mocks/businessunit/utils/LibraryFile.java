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

package org.monet.mocks.businessunit.utils;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;

public class LibraryFile {

	public static String getContentType(File file) {
		try {
			return getContentType(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return "application/octet-stream";
		}
	}

	public static String getContentType(InputStream inputStream) {
		String contentType = "application/octet-stream";
		ContentHandler contentHandler = new BodyContentHandler();
		Metadata metadata = new Metadata();

		try {
			Parser parser = new AutoDetectParser();
			parser.parse(inputStream, contentHandler, metadata);
			contentType = metadata.get(Metadata.CONTENT_TYPE);
		} catch (IOException e) {
		} catch (SAXException e) {
		} catch (TikaException e) {
		}

		return contentType;
	}

}