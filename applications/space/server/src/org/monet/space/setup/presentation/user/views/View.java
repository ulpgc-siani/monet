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

package org.monet.space.setup.presentation.user.views;

import org.monet.space.setup.configuration.Configuration;
import org.monet.space.setup.core.constants.Tags;
import org.monet.space.setup.core.model.Language;
import org.monet.space.setup.presentation.user.agents.AgentTemplates;
import org.monet.space.setup.presentation.user.constants.ViewTag;
import org.monet.space.setup.presentation.user.util.Context;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;

import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;


public abstract class View {
	private String sTitle;
	protected Context context;
	protected HashMap<String, View> hmViews;
	protected Object oTarget;
	protected AgentTemplates oAgentTemplates;
	protected ViewsFactory oViewsFactory;
	protected String codeLanguage;

	public View(Context oContext, String codeLanguage) {
		this.sTitle = Strings.EMPTY;
		this.context = new Context();
		this.hmViews = new HashMap<String, View>();
		this.oTarget = null;
		this.oAgentTemplates = AgentTemplates.getInstance();
		this.oViewsFactory = ViewsFactory.getInstance();
		this.codeLanguage = codeLanguage;
	}

	private Boolean putLanguageTags(Properties propTags) {
		Enumeration<?> ePropertyNames = propTags.propertyNames();
		String code;

		while (ePropertyNames.hasMoreElements()) {
			code = (String) ePropertyNames.nextElement();
			this.context.put(code, propTags.getProperty(code));
		}

		return true;
	}

	private Boolean createCommonTags() {
		Configuration configuration = Configuration.getInstance();
		Language oLanguage = Language.getInstance();

		this.putLanguageTags(oLanguage.getLabels(this.codeLanguage));
		this.putLanguageTags(oLanguage.getMessages(this.codeLanguage));
		this.putLanguageTags(oLanguage.getErrorMessages(this.codeLanguage));

		this.context.put(Tags.URL, configuration.getUrl());
		this.context.put(Tags.PORT, configuration.getPort());
		this.context.put(Tags.LANGUAGE, Language.getInstance().getCurrent());
		this.context.put(Tags.API_URL, configuration.getApiUrl());
		this.context.put(Tags.API_PORT, configuration.getApiPort());
		this.context.put(Tags.IMAGES_PATH, configuration.getImagesPath());
		this.context.put(Tags.LANGUAGES_URL, configuration.getLanguagesUrl());
		this.context.put(Tags.JAVASCRIPT_URL, configuration.getJavascriptUrl());
		this.context.put(Tags.WEB_COMPONENTS_URL, configuration.getWebComponentsUrl());
		this.context.put(Tags.STYLES_URL, configuration.getStylesUrl());
		this.context.put(Tags.ENTERPRISE_LOGIN_URL, String.valueOf(configuration.getEnterpriseLoginUrl()));
		this.context.put(Tags.APPLICATION_SIGNATORY_URL, configuration.getSignatoryServletUrl());

		return true;
	}

	public String getTitle() {
		return this.sTitle;
	}

	public Boolean addView(String sViewTag, View oView) {
		this.hmViews.put(sViewTag, oView);
		return true;
	}

	public Boolean setTitle(String sTitle) {
		this.sTitle = sTitle;
		return true;
	}

	public Object getTarget() {
		return oTarget;
	}

	public Boolean setTarget(Object oTarget) {
		this.oTarget = oTarget;
		return true;
	}
  
  /*public String execute() {
    StringWriter writer = new StringWriter();
    this.execute(writer);
    return writer.toString();
  }
  */

	public void execute(Writer writer) {
		this.context.put(ViewTag.LANGUAGE, this.codeLanguage);
		this.context.put(ViewTag.DATETIME_FORMAT_TEXT, LibraryDate.Format.TEXT);
		this.context.put(ViewTag.DATETIME_FORMAT_NUMERIC, LibraryDate.Format.NUMERIC);

		this.createCommonTags();
	}

}