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

import org.monet.space.office.ApplicationOffice;
import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

import java.util.Date;

public class ActionRegisterException extends Action {

	public ActionRegisterException() {
		super();
	}

	public String execute() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		String clientData = LibraryRequest.getParameter(Parameter.DATA, this.request);
		String logFilename = monetConfiguration.getLogsDir() + Strings.BAR45 + ApplicationOffice.LOG_AJAX;
		StringBuffer content = new StringBuffer();

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (clientData == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.REGISTER_EXCEPTION);
		}

		content.append("-----------------------------------------------------------------------------\n");
		content.append(this.getAccount().getUser().getInfo().getFullname() + " with id '" + this.getAccount().getUser().getId() + "' on " + LibraryDate.getDateAndTimeString(new Date(), Language.ENGLISH, null, LibraryDate.Format.TEXT, true, "/") + "\n");
		content.append("-----------------------------------------------------------------------------\n");
		content.append(clientData + "\n\n");

		if (!AgentFilesystem.existFile(logFilename)) {
			AgentFilesystem.createFile(logFilename);
		}

		AgentFilesystem.appendToFile(logFilename, content.toString());

		return Language.getInstance().getMessage(MessageCode.EXCEPTION_REGISTERED);
	}

}