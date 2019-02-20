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

package org.monet.space.explorer.model;

import java.util.Properties;

public class Language extends org.monet.space.kernel.model.Language {
	private static final String RESOURCES_LANGUAGES_PATH = "/explorer/languages";

	public Properties getLabels(String codeLanguage) {
		if (!this.labels.containsKey(codeLanguage))
			this.initializeTags(RESOURCES_LANGUAGES_PATH, codeLanguage, TYPE_LABELS, this.labels);
		return this.labels.get(codeLanguage);
	}

	public String getLabel(String code, String codeLanguage) {
		if (!this.labels.containsKey(codeLanguage))
			this.initializeTags(RESOURCES_LANGUAGES_PATH, codeLanguage, TYPE_LABELS, this.labels);
		return this.labels.get(codeLanguage).getProperty(code);
	}

	public Properties getMessages(String codeLanguage) {
		if (!this.messages.containsKey(codeLanguage))
			this.initializeTags(RESOURCES_LANGUAGES_PATH, codeLanguage, TYPE_MESSAGES, this.messages);
		return this.messages.get(codeLanguage);
	}

	public String getMessage(String code, String codeLanguage) {
		if (!this.messages.containsKey(codeLanguage))
			this.initializeTags(RESOURCES_LANGUAGES_PATH, codeLanguage, TYPE_MESSAGES, this.messages);
		return this.messages.get(codeLanguage).getProperty(code);
	}

	public Properties getErrorMessages(String codeLanguage) {
		if (!this.errorMessages.containsKey(codeLanguage))
			this.initializeTags(RESOURCES_LANGUAGES_PATH, codeLanguage, TYPE_ERROR_MESSAGES, this.errorMessages);
		return this.errorMessages.get(codeLanguage);
	}

	public String getErrorMessage(String code, String codeLanguage) {
		if (!this.errorMessages.containsKey(codeLanguage))
			this.initializeTags(RESOURCES_LANGUAGES_PATH, codeLanguage, TYPE_ERROR_MESSAGES, this.errorMessages);
		return this.errorMessages.get(codeLanguage).getProperty(code);
	}

	public Properties getLabels() {
		String codeLanguage = getCurrent();
		if (!this.labels.containsKey(codeLanguage))
			this.initializeTags(RESOURCES_LANGUAGES_PATH, codeLanguage, TYPE_LABELS, this.labels);
		return this.labels.get(codeLanguage);
	}

	public String getLabel(String code) {
		String codeLanguage = getCurrent();
		if (!this.labels.containsKey(codeLanguage))
			this.initializeTags(RESOURCES_LANGUAGES_PATH, codeLanguage, TYPE_LABELS, this.labels);
		return this.labels.get(codeLanguage).getProperty(code);
	}

	public String getTaskStateLabel(String state) {
		String codeLanguage = getCurrent();

		if (!this.labels.containsKey(codeLanguage))
			this.initializeTags(RESOURCES_LANGUAGES_PATH, codeLanguage, TYPE_LABELS, this.labels);

		return this.labels.get(codeLanguage).getProperty("task-state-" + state);
	}

	public Properties getMessages() {
		String codeLanguage = getCurrent();
		if (!this.messages.containsKey(codeLanguage))
			this.initializeTags(RESOURCES_LANGUAGES_PATH, codeLanguage, TYPE_MESSAGES, this.messages);
		return this.messages.get(codeLanguage);
	}

	public String getMessage(String code) {
		String codeLanguage = getCurrent();
		if (!this.messages.containsKey(codeLanguage))
			this.initializeTags(RESOURCES_LANGUAGES_PATH, codeLanguage, TYPE_MESSAGES, this.messages);
		return this.messages.get(codeLanguage).getProperty(code);
	}

	public Properties getErrorMessages() {
		String codeLanguage = getCurrent();
		if (!this.errorMessages.containsKey(codeLanguage))
			this.initializeTags(RESOURCES_LANGUAGES_PATH, codeLanguage, TYPE_ERROR_MESSAGES, this.errorMessages);
		return this.errorMessages.get(codeLanguage);
	}

	public String getErrorMessage(String code) {
		String codeLanguage = getCurrent();
		if (!this.errorMessages.containsKey(codeLanguage))
			this.initializeTags(RESOURCES_LANGUAGES_PATH, codeLanguage, TYPE_ERROR_MESSAGES, this.errorMessages);
		return this.errorMessages.get(codeLanguage).getProperty(code);
	}
}