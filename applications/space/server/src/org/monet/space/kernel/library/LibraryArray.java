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

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class LibraryArray {

	public static <T> String implode(T[] elements, String separator) {
		return implode(Arrays.asList(elements), separator);
	}

	public static <T> String implode(List<T> elements, String separator) {
		return implode((Iterable<T>)elements, separator);
	}

	public static <T> String implode(Set<T> elements, String separator) {
		return implode((Iterable<T>)elements, separator);
	}

	private static <T> String implode(Iterable<T> elements, String separator) {
		String result = "";

		for (T element : elements) {
			if (!result.isEmpty())
				result += separator;
			result += element;
		}

		return result;
	}

}