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

import org.monet.space.kernel.constants.Strings;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class LibrarySearch {

	public class Section {
		public static final String TITLE = "title";
		public static final String DESCRIPTION = "description";
		public static final String CONTENT = "content";
	}

	public synchronized static LinkedHashSet<String> analyzeContent(String sContent) {
		String sAlphabet = "abcdefghijklmnopqrstuvwxyz1234567890 .=";
		String sValue;
		LinkedHashSet<String> hsWords = new LinkedHashSet<String>();
		LinkedHashSet<String> hsNewWords = new LinkedHashSet<String>();
		LinkedHashSet<String> hsAux = null;
		Iterator<String> oIterator;

		if (sContent.equals("")) return hsWords;

		sContent = sContent.replaceAll("<[/!]*?[^<>]*?>", Strings.SPACE); // Remove HTML tags
		sContent = sContent.replaceAll("([\r\n])[\\s]+", "\1"); // Remover blank spaces
		sContent = sContent.replaceAll("[\r]*", Strings.EMPTY);
		sContent = sContent.replaceAll("[\n]*", Strings.EMPTY);
		sContent = sContent.replaceAll("&(quot|#34);", Strings.DOBLE_QUOTE); // Reemplazar HTML entities
		sContent = sContent.replaceAll("&(amp|#38);", Strings.AMPERSAND);
		sContent = sContent.replaceAll("&(lt|#60);", Strings.LOWER);
		sContent = sContent.replaceAll("&(gt|#62);", Strings.GREATER);
		sContent = sContent.replaceAll("&(nbsp|#160);", Strings.SPACE);
		sContent = sContent.replaceAll("á", "a");

		sContent = sContent.toLowerCase();
		sContent = LibraryString.cleanAccents(sContent);
		sContent = LibraryString.cleanString(sContent, sAlphabet);

		hsWords.addAll(Arrays.asList(sContent.split(Strings.SPACE)));
		hsAux = new LinkedHashSet<String>(hsWords);

		hsWords.clear();
		oIterator = hsAux.iterator();
		while (oIterator.hasNext()) {
			sValue = oIterator.next();

			if (sValue.length() > 2) {
				while (sValue.substring(sValue.length() - 1, sValue.length()).equals(Strings.DOT) && (sValue.length() > 2))
					sValue = sValue.substring(0, sValue.length() - 1);

				if (sValue.length() <= 2) continue;

				sValue = sValue.trim();
				if (sValue.indexOf(Strings.SPACE) != -1) {
					hsNewWords = LibrarySearch.analyzeContent(sValue);
					hsWords.addAll(hsNewWords);
				} else {
					hsWords.add(sValue);
				}
			}
		}

		return hsWords;
	}

}
