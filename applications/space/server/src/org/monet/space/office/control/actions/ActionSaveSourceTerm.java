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

package org.monet.space.office.control.actions;

import org.monet.metamodel.SourceDefinition;
import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.SerializerData;
import org.monet.space.kernel.model.Source;
import org.monet.space.kernel.model.Term;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

import java.util.Date;
import java.util.LinkedHashSet;

public class ActionSaveSourceTerm extends Action {
	private SourceLayer sourceLayer;

	public ActionSaveSourceTerm() {
		super();
		this.sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
	}

	public String execute() {
		String idSource = LibraryRequest.getParameter(Parameter.ID_SOURCE, this.request);
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String codeParent = LibraryRequest.getParameter(Parameter.PARENT, this.request);
		String typeValue = LibraryRequest.getParameter(Parameter.TYPE, this.request);
		String label = LibraryRequest.getParameter(Parameter.LABEL, this.request);
		String tags = LibraryRequest.getParameter(Parameter.TAGS, this.request);
		Boolean enable = Boolean.valueOf(LibraryRequest.getParameter(Parameter.ENABLE, this.request));
		int type = Term.TERM;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (code == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SAVE_SOURCE_TERM);
		}

		if (typeValue.equals("0")) type = Term.TERM;
		else if (typeValue.equals("1")) type = Term.SUPER_TERM;
		else if (typeValue.equals("2")) type = Term.CATEGORY;

		Term term = new Term();
		term.setParent(codeParent);
		term.setCode(code);
		term.setType(type);
		term.setLabel(label);
		term.setEnabled(enable);
		term.setTags(SerializerData.<LinkedHashSet<String>>deserializeSet(tags));

		Source<SourceDefinition> source = this.sourceLayer.loadSource(idSource);
		this.sourceLayer.updateSourceTerm(source, term);
		source.setUpdateDate(new Date());
		this.sourceLayer.saveSource(source);

		return Language.getInstance().getMessage(MessageCode.TERM_SAVED);
	}

}