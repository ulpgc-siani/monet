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

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.presentation.user.constants.Views;
import org.monet.space.office.presentation.user.views.ViewMail;

public class ActionSendMail extends Action {

	private static final String OFFICE_TEMPLATES_MAIL_TEXT = "/office/templates/mail.text.tpl";
	private static final String OFFICE_TEMPLATES_MAIL_HTML = "/office/templates/mail.html.tpl";

	public ActionSendMail() {
		super();
	}

	public String execute() {
		Configuration configuration = Configuration.getInstance();
		String subject = LibraryRequest.getParameter(Parameter.SUBJECT, this.request);
		String body = LibraryRequest.getParameter(Parameter.BODY, this.request);
		ViewMail viewMail;
		String htmlBody, textBody;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if ((subject == null) || (body == null))
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SEND_MAIL);

		viewMail = (ViewMail) this.viewsFactory.get(Views.MAIL, this.agentRender, this.codeLanguage);
		viewMail.setTemplate(OFFICE_TEMPLATES_MAIL_HTML);
		viewMail.setSubject(subject);
		viewMail.setBody(body);

		htmlBody = viewMail.execute();

		viewMail.setTemplate(OFFICE_TEMPLATES_MAIL_TEXT);
		textBody = viewMail.execute();

		HtmlEmail email = new HtmlEmail();

		try {
			email.setHostName(configuration.getValue(Configuration.MAIL_ADMIN_HOST));
			email.setAuthentication(configuration.getValue(Configuration.MAIL_ADMIN_USERNAME), configuration.getValue(Configuration.MAIL_ADMIN_PASSWORD));
			email.setSmtpPort(Integer.valueOf(configuration.getValue(Configuration.MAIL_ADMIN_PORT)));
			email.setSSL(Boolean.valueOf(configuration.getValue(Configuration.MAIL_ADMIN_SECURE)));
			email.setTLS(Boolean.valueOf(configuration.getValue(Configuration.MAIL_ADMIN_TLS)));

			email.setFrom(configuration.getValue(Configuration.MAIL_ADMIN_FROM));
			email.addTo(configuration.getValue(Configuration.MAIL_ADMIN_TO));

			email.setSubject(subject);
			email.setHtmlMsg(htmlBody);
			email.setTextMsg(textBody);

			email.send();
		} catch (EmailException exception) {
			throw new SystemException(ErrorCode.SEND_MAIL, subject, exception);
		}

		return Language.getInstance().getMessage(MessageCode.MAIL_SENT);
	}

}