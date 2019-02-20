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

package org.monet.space.kernel.library;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LibraryXML {

	public static Boolean isHeader(String sContent) {
		Pattern oPattern = Pattern.compile("<?xml[^?]*?>");
		Matcher oMatcher = oPattern.matcher(sContent);
		return oMatcher.find();
	}

	public static String clearHeader(String sContent) {
		Pattern oPattern = Pattern.compile("<\\?xml[^\\?]*\\?>");
		Matcher oMatcher = oPattern.matcher(sContent);
		return oMatcher.replaceAll("");
	}

	public static String outputString(Document oDocument) {
		XMLOutputter oSerializer = new XMLOutputter();
		String sResult;

		sResult = oSerializer.outputString(oDocument);

		return sResult;
	}

	public static String clean(String content) {
		content = content.trim();
		content = content.replaceAll("&amp;", "@@@amp@@@");
		content = content.replaceAll("&", "&amp;");
		content = content.replaceAll("@@@amp@@@", "&amp;");
		return content;
	}
}