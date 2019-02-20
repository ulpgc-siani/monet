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

package org.monet.api.space.backservice.impl.model;

import org.jdom.Element;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

public class TermList extends BaseModelList<Term> {

	public TermList() {
		super();
	}

	public void add(Term baseModel) {
		String id;

		id = baseModel.getId();
		if ((id.equals("-1")) || (id.isEmpty())) id = String.valueOf(this.items.size());

		this.items.put(id, baseModel);
	}

	@SuppressWarnings("unchecked")
	public void deserializeFromXML(Element termList) throws ParseException {
		List<Element> terms;
		Iterator<Element> iterator;

		if (termList == null) return;

		terms = termList.getChildren("term");
		iterator = terms.iterator();

		this.clear();

		while (iterator.hasNext()) {
			Term term = new Term();
			term.deserializeFromXML(iterator.next());
			this.add(term);
		}

	}

}
