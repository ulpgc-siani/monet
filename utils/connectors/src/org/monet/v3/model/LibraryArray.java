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

package org.monet.v3.model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LibraryArray {

	public static String implode(String[] aObject, String sSeparator) {
		String sResult = "";

		for (int iPos = 0; iPos < aObject.length; iPos++) {
			if (iPos != 0) {
				sResult += sSeparator;
			}
			sResult += aObject[iPos];
		}

		return sResult;
	}

	public static String implode(List<String> alContent, String sSeparator) {
		String sResult = "";
		Iterator<String> oIterator = alContent.iterator();

		while (oIterator.hasNext()) {
			sResult += oIterator.next() + sSeparator;
		}

		if (sResult.length() > 0) sResult = sResult.substring(0, sResult.length() - sSeparator.length());

		return sResult;
	}

	public static String implode(Set<String> hsContent, String sSeparator) {
		String sResult = "";
		Iterator<String> oIter = hsContent.iterator();

		while (oIter.hasNext()) sResult += oIter.next() + sSeparator;
		if (sResult.length() > 0) sResult = sResult.substring(0, sResult.length() - sSeparator.length());

		return sResult;
	}

}