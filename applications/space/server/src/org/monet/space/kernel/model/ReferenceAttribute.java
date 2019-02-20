/*efini
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

import org.monet.bpi.types.*;
import org.monet.bpi.types.Number;
import org.monet.bpi.types.Term;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class ReferenceAttribute<T> extends BaseObject {
	private T value;

	public ReferenceAttribute(String code) {
		this(code, null);
	}

	public ReferenceAttribute(String code, T value) {
		super();
		this.code = code;
		this.value = value;
	}

	public Boolean setValue(T value) {
		this.value = value;
		return true;
	}

	public T getValue() {
		return this.value;
	}

	public String getValueAsString() {
		String result = "";

		if (this.value instanceof Boolean) result = Boolean.toString((Boolean) this.value);
		else if (this.value instanceof Integer) result = Integer.toString((Integer) this.value);
		else if (this.value instanceof Number) result = ((Number) this.value).formattedValue();
		else if (this.value instanceof Double) result = Double.toString((Double) this.value);
		else if (this.value instanceof String) result = (String) this.value;
		else if (this.value instanceof Check) result = ((Check) this.value).getLabel();
		else if (this.value instanceof Date) result = ((Date) this.value).getFormattedValue();
		else if (this.value instanceof Picture) result = ((Picture) this.value).getFilename();
		else if (this.value instanceof Term) result = ((Term) this.value).getLabel();
		else if (this.value instanceof Link) result = ((Link) this.value).getLabel();

		if (result == null)
			result = "";

		return result;
	}

	public ReferenceAttribute<?> clone() {
		ReferenceAttribute<?> result = null;

		if (this.value instanceof Boolean)
			result = new ReferenceAttribute<Boolean>(this.code, new Boolean((Boolean) this.value));
		else if (this.value instanceof Integer)
			result = new ReferenceAttribute<Integer>(this.code, new Integer((Integer) this.value));
		else if (this.value instanceof Number)
			result = new ReferenceAttribute<Number>(this.code, new Number((Number) this.value));
		else if (this.value instanceof Double)
			result = new ReferenceAttribute<Double>(this.code, new Double((Double) this.value));
		else if (this.value instanceof String)
			result = new ReferenceAttribute<String>(this.code, new String((String) this.value));
		else if (this.value instanceof Check)
			result = new ReferenceAttribute<Check>(this.code, new Check((Check) this.value));
		else if (this.value instanceof Date)
			result = new ReferenceAttribute<Date>(this.code, new Date((Date) this.value));
		else if (this.value instanceof Picture)
			result = new ReferenceAttribute<Picture>(this.code, new Picture((Picture) this.value));
		else if (this.value instanceof Term)
			result = new ReferenceAttribute<Term>(this.code, new Term((Term) this.value));
		else if (this.value instanceof Link)
			result = new ReferenceAttribute<Link>(this.code, new Link((Link) this.value));
		else result = new ReferenceAttribute<Object>(this.code, this.value);

		if (result != null)
			result.code = this.code;

		return result;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}
}