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

import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.HistoryStoreLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.HistoryStore;
import org.monet.space.kernel.model.TermList;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

import java.util.HashMap;

public class ActionLoadHistoryTerms extends Action {
	private HistoryStoreLayer historyStoreLayer;

	public ActionLoadHistoryTerms() {
		super();
		this.historyStoreLayer = ComponentPersistence.getInstance().getHistoryStoreLayer();
	}

	public String execute() {
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		DataRequest dataRequest;
		HashMap<String, String> parameters;
		HistoryStore historyStore;
		TermList result;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (code == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_HISTORY_TERMS);
		}

		dataRequest = new DataRequest();
		parameters = this.getRequestParameters();
		result = new TermList();

		try {
			dataRequest.setStartPos(Integer.valueOf(parameters.get(Parameter.START)));
		} catch (NumberFormatException oException) {
			dataRequest.setStartPos(0);
		} catch (ClassCastException oException) {
			dataRequest.setStartPos(0);
		}

		try {
			dataRequest.setLimit(Integer.valueOf(parameters.get(Parameter.LIMIT)));
		} catch (NumberFormatException oException) {
			dataRequest.setLimit(Strings.UNDEFINED_INT);
		} catch (ClassCastException oException) {
			dataRequest.setLimit(Strings.UNDEFINED_INT);
		}

		try {
			dataRequest.setCondition(new String(parameters.get(Parameter.CONDITION)));
		} catch (NullPointerException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		} catch (ClassCastException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		}

		historyStore = this.historyStoreLayer.loadHistoryStore(code);
		result = historyStore.getTerms(dataRequest);

		return result.toJson().toJSONString();
	}

}