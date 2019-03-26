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

package org.monet.space.kernel.model;

import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.constants.Common;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.FilesystemException;
import org.monet.space.kernel.utils.Resources;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Language {
	private static Language oInstance;
	protected Map<String, Properties> labels;
	protected Map<String, Properties> messages;
	protected Map<String, Properties> errorMessages;

	protected static Set<String> supportedLanguages;

	public static final String ENGLISH = "en";
	public static final String SPANISH = "es";
	public static final String DEUTSCH = "de";
	public static final String FRENCH = "ft";
	public static final String PORTUGUESE = "pt";

	public static final String USER_LANGUAGE = "USER_LANGUAGE";
	public static final String USER_TIMEZONE = "USER_TIMEZONE";

	protected static final Integer TYPE_LABELS = 1;
	protected static final Integer TYPE_MESSAGES = 2;
	protected static final Integer TYPE_ERROR_MESSAGES = 3;

	private static final String RESOURCES_LANGUAGES_PATH = "/kernel/languages";

	protected Language() {
		this.labels = new HashMap<>();
		this.messages = new HashMap<>();
		this.errorMessages = new HashMap<>();
	}

	public static void initLanguages() {
		if (supportedLanguages == null) {
			supportedLanguages = new HashSet<>();
			supportedLanguages.add(SPANISH);
			supportedLanguages.add(ENGLISH);
		}
	}

	public static boolean supportsLanguage(String language) {
		Language.initLanguages();
		return supportedLanguages.contains(language);
	}

	public synchronized static Language getInstance() {
		if (oInstance == null) {
			oInstance = new Language();
		}
		return oInstance;
	}

	public static String getCurrent() {
		String langCode = null;
		Session session = Context.getInstance().getCurrentSession();
		if (session != null)
			langCode = session.getVariable(USER_LANGUAGE);
		if (langCode == null)
			langCode = SPANISH;
		return langCode;
	}

	public static TimeZone getCurrentTimeZone() {
		TimeZone timeZone = null;
		Session session = Context.getInstance().getCurrentSession();
		if (session != null)
			timeZone = session.getVariable(USER_TIMEZONE);
		if (timeZone == null)
			return BusinessUnit.getTimeZone();
		return timeZone;
	}

	public static void fillCurrentTimeZone(HttpServletRequest request) {
		Session session = Context.getInstance().getCurrentSession();

		if (session == null)
			session = AgentSession.getInstance().add(request.getSession().getId());

		//TimeZone timeZone = LibraryRequest.getTimeZone(request);
		session.setVariable(USER_TIMEZONE, BusinessUnit.getTimeZone());
	}

	public static void fillCurrentTimeZone(TimeZone timeZone) {
		Session session = Context.getInstance().getCurrentSession();
		session.setVariable(USER_TIMEZONE, timeZone);
	}

	public static void fillCurrentLanguage(HttpServletRequest request) {
		Session session = AgentSession.getInstance().get(request.getSession().getId());

		if (session == null)
			session = AgentSession.getInstance().add(request.getSession().getId());

		initLanguages();

		String requestLanguage = request.getParameter("language");
		if (requestLanguage != null)
			session.setVariable(USER_LANGUAGE, requestLanguage);
		else {
			String prefLanguage = request.getHeader("Accept-Language");

			Account account = session.getAccount();
			if (account != null) {
				session.setVariable(USER_LANGUAGE, account.getUser().getLanguage());
				return;
			}

			if (prefLanguage != null) {
				String language = null;
				StringTokenizer acceptLanguageTokenizer = new StringTokenizer(prefLanguage, ",");

				int elements = acceptLanguageTokenizer.countTokens();

				for (int idx = 0; idx < elements; idx++) {
					String acceptLang = acceptLanguageTokenizer.nextToken();
					StringTokenizer langTokenizer = new StringTokenizer(acceptLang, "-");
					if (langTokenizer.countTokens() == 2)
						acceptLang = langTokenizer.nextToken();
					if (supportedLanguages.contains(acceptLang)) {
						language = acceptLang;
						break;
					}
				}

				if (language == null)
					language = Language.SPANISH;

				session.setVariable(USER_LANGUAGE, language);
			} else {
				String language = session.getVariable(USER_LANGUAGE);
				if (language == null)
					session.setVariable(USER_LANGUAGE, Language.SPANISH);
			}
		}
	}

	protected Boolean initializeTags(String resourcesPath, String language, Integer type, Map<String, Properties> containerMap) {
		String filename = resourcesPath + Strings.BAR45 + language + getExtension(type);

		try {
			InputStream stream = Resources.getAsStream(filename);
			Properties properties = new Properties();
			properties.loadFromXML(stream);
			containerMap.put(language, properties);
			stream.close();
		} catch (IOException | FilesystemException ex) {
			throw new DataException(ErrorCode.READ_LANGUAGE_FILE, filename, ex);
		}

		return true;
	}

	private String getExtension(Integer type) {
		if (type == TYPE_LABELS)
			return Common.FileExtensions.LABELS;
		if (type == TYPE_MESSAGES)
			return Common.FileExtensions.MESSAGES;
		if (type == TYPE_ERROR_MESSAGES)
			return Common.FileExtensions.ERROR_MESSAGES;
		return "";
	}

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

	public String getLabel(String code) {
		return getLabel(code, getCurrent());
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

	public String getMessage(String code) {
		return getMessage(code, getCurrent());
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

	public String getErrorMessage(String code) {
		return getErrorMessage(code, getCurrent());
	}

	public String getModelResource(Object resId) {
		if (resId instanceof String)
			return (String) resId;
		return getModelResource(resId, getCurrent());
	}

	public String getModelResource(Object res, String codeLanguage) {
		if (res == null)
			return "";

		if (res instanceof String)
			return (String) res;

		Integer resId = (Integer) res;
		String[] parts = codeLanguage.split("-");
		String langCode = parts[0];
		String countryCode = parts.length > 1 ? parts[1] : null;
		Locale locale = countryCode != null ? new Locale(langCode, countryCode) : new Locale(langCode);
		String resource = null;
		Dictionary dictionary = Dictionary.getInstance();

		org.monet.metamodel.interfaces.Language language = dictionary.getLanguage(codeLanguage);
		if (language != null)
			resource = language.get(resId);

		if (!locale.getLanguage().equals(codeLanguage) && (language == null || resource == null)) {
			//Check only with locale language
			language = dictionary.getLanguage(locale.getLanguage());

			if (language != null)
				resource = language.get(resId);
		}

		if (resource == null) {
			//From default language
			language = dictionary.getDefaultLanguage();
			return language.get(resId);
		}

		return resource;
	}

}