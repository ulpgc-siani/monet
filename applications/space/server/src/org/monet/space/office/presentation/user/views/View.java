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

package org.monet.space.office.presentation.user.views;

import org.monet.metamodel.AbstractManifestBase;
import org.monet.metamodel.Distribution;
import org.monet.metamodel.Project;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.BusinessModel;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Federation;
import org.monet.space.kernel.model.User;
import org.monet.space.office.configuration.Configuration;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.core.constants.Tags;
import org.monet.space.office.core.model.Language;
import org.monet.space.office.presentation.user.agents.AgentRender;
import org.monet.space.office.presentation.user.agents.AgentTemplates;
import org.monet.space.office.presentation.user.constants.ViewTag;
import org.monet.space.office.presentation.user.constants.ViewType;
import org.monet.space.office.presentation.user.util.Context;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

public abstract class View {
	private String sTitle;
	protected Context context;
	protected User oUser;
	protected String mode;
	protected String sTemplateParameters;
	protected HashMap<String, String> hmTemplateParameters;
	protected HashMap<String, String> hmParameters;
	protected String type;
	protected Object target;
	protected AgentTemplates agentTemplates;
	protected AgentRender agentRender;
	protected AgentLogger agentLogger;
	protected String codeLanguage;
	protected ViewsFactory viewsFactory;

	public View(Context oContext, AgentRender oAgentRender, String codeLanguage) {
		this.sTitle = Strings.EMPTY;
		this.context = new Context();
		this.oUser = null;
		this.mode = null;
		this.sTemplateParameters = null;
		this.hmTemplateParameters = new HashMap<String, String>();
		this.hmParameters = new HashMap<String, String>();
		this.type = ViewType.BROWSE;
		this.target = null;
		this.agentTemplates = AgentTemplates.getInstance();
		this.agentRender = oAgentRender;
		this.agentLogger = AgentLogger.getInstance();
		this.codeLanguage = codeLanguage;
		this.viewsFactory = ViewsFactory.getInstance();
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
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		Configuration officeConfiguration = Configuration.getInstance();
		Language language = Language.getInstance();
        BusinessUnit businessUnit = BusinessUnit.getInstance();
		String subTitle = businessUnit.getSubTitle();

		AbstractManifestBase.DefaultLocationProperty defaultLocation = BusinessModel.getInstance().getProject().getDefaultLocation();
		if (defaultLocation == null) defaultLocation = businessUnit.getDistribution().getDefaultLocation();

		this.putLanguageTags(language.getLabels());
		this.putLanguageTags(language.getMessages());
		this.putLanguageTags(language.getErrorMessages());

        this.context.put(Tags.PAGE_TITLE, businessUnit.getTitle() + ((subTitle != null && !subTitle.isEmpty())?" - " + subTitle:""));
		this.context.put(Tags.BUSINESS_UNIT, businessUnit.getName());
		this.context.put(Tags.DOMAIN, monetConfiguration.getDomain());
		this.context.put(Tags.URL, configuration.getUrl());
		this.context.put(Tags.PORT, String.valueOf(configuration.getPort()));
		this.context.put(Tags.API_URL, configuration.getApiUrl());
		this.context.put(Tags.PUSH_API_URL, configuration.getPushApiUrl());
		this.context.put(Tags.GOOGLE_API_KEY, configuration.getGoogleApiKey());
		this.context.put(Tags.PUSH_ENABLED, configuration.isPushEnable());
		this.context.put(Tags.API_PORT, String.valueOf(configuration.getApiPort()));
		this.context.put(Tags.IMAGES_PATH, configuration.getImagesPath());
		this.context.put(Tags.APPLICATION_OFFICE_RESOURCE_URL, officeConfiguration.getUrl());
		this.context.put(Tags.APPLICATION_FMS_URL, officeConfiguration.getFmsServletUrl());
		this.context.put(Tags.APPLICATION_SIGNATORY_URL, officeConfiguration.getSignatoryServletUrl());
		this.context.put(Tags.LANGUAGES_URL, configuration.getLanguagesUrl());
		this.context.put(Tags.JAVASCRIPT_URL, configuration.getJavascriptUrl());
		this.context.put(Tags.WEB_COMPONENTS_URL, configuration.getWebComponentsUrl());
		this.context.put(Tags.URL_PROTOCOL, monetConfiguration.useSSL() ? "https" : "http");
		this.context.put(Tags.ANALYTICS_URL, configuration.getAnalyticsUrl());
		this.context.put(Tags.STYLES_URL, configuration.getStylesUrl());
		this.context.put(Tags.ENTERPRISE_LOGIN_URL, String.valueOf(configuration.getEnterpriseLoginUrl()));
		this.context.put(Tags.ACTION_LOGOUT, Actions.LOGOUT);
		this.context.put(Tags.ENCRIPT_DATA, monetConfiguration.encriptParameters());
		this.context.put(Tags.TEST_CASE, "false");
		this.context.put(Tags.CURRENT_YEAR, String.valueOf(LibraryDate.getCurrentYear()));
		this.context.put(Tags.DEFAULT_LOCATION_LATITUDE, defaultLocation != null ? defaultLocation.getLatitude() : 28);
		this.context.put(Tags.DEFAULT_LOCATION_LONGITUDE, defaultLocation != null ? defaultLocation.getLongitude() : -15);
		this.context.put(Tags.LANGUAGE, language.getCurrent());

		return true;
	}

	private Boolean generateTemplateParameters() {
		String[] aParameters;
		Integer iPos = 0;

		if (this.sTemplateParameters == null) return true;

		aParameters = this.sTemplateParameters.split(Strings.AMPERSAND);

		this.hmTemplateParameters.clear();
		for (iPos = 0; iPos < aParameters.length; iPos++) {
			String[] aParameter = aParameters[iPos].split(Strings.EQUAL);
			if (aParameter.length < 2) continue;
			if ((aParameter[0] != null) && (aParameter[1] != null)) {
				this.hmTemplateParameters.put(aParameter[0], aParameter[1]);
			}
		}

		return true;
	}

	private Boolean printParameter(String sName, String sValue) {
		Boolean bIsInteger;
		Integer iValue = 0;

		if (sValue == null) return true;

		bIsInteger = false;
		if (Character.isDigit(sValue.charAt(0))) {
			try {
				iValue = Integer.valueOf(sValue);
				bIsInteger = true;
			} catch (NumberFormatException ex) {
			}
		}

		if (bIsInteger) this.context.put(sName, iValue);
		else this.context.put(sName, sValue);

		return true;
	}

	private Boolean printParameters() {
		Iterator<String> iter;

		iter = this.hmTemplateParameters.keySet().iterator();
		while (iter.hasNext()) {
			String sName = iter.next();
			this.printParameter(sName, this.hmTemplateParameters.get(sName));
		}

		iter = this.hmParameters.keySet().iterator();
		while (iter.hasNext()) {
			String sName = iter.next();
			this.printParameter(sName, this.hmParameters.get(sName));
		}

		return true;
	}

	protected String getTemplateFilename(String code) {
		BusinessUnit oBusinessUnit = BusinessUnit.getInstance();
		String sTemplateFilename = oBusinessUnit.getTemplateFilename(code, this.type, this.mode);
		return sTemplateFilename;
	}

	public String getTitle() {
		return this.sTitle;
	}

	public Boolean setTitle(String sTitle) {
		this.sTitle = sTitle;
		return true;
	}

	public Boolean setUser(User oUser) {
		this.oUser = oUser;
		return true;
	}

	public Boolean setMode(String sMode) {
		Integer iPos = sMode.lastIndexOf(Strings.QUESTION);
		if (iPos == -1) iPos = sMode.length();
		this.mode = sMode.substring(0, iPos);
		if (iPos < sMode.length()) this.sTemplateParameters = sMode.substring(iPos + 1);
		return true;
	}

	public Boolean setType(String sType) {
		this.type = sType;
		return true;
	}

	public Object getTarget() {
		return target;
	}

	public Boolean setTarget(Object oTarget) {
		this.target = oTarget;
		return true;
	}

	public Boolean addParameter(String sName, String sValue) {
		this.hmParameters.put(sName, sValue);
		return true;
	}

	public String execute() {
		StringWriter writer = new StringWriter();
		this.execute(writer);
		return writer.toString();
	}

	public void execute(Writer writer) {
		Configuration configuration = Configuration.getInstance();
		org.monet.space.kernel.configuration.Configuration kernelConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Federation federation = businessUnit.getFederation();

		if (this.oUser != null) this.context.put(ViewTag.USER, this.oUser);

		this.context.put(ViewTag.LANGUAGE, Language.getCurrent());
		this.context.put(ViewTag.DATETIME_FORMAT_TEXT, LibraryDate.Format.TEXT);
		this.context.put(ViewTag.DATETIME_FORMAT_NUMERIC, LibraryDate.Format.NUMERIC);
		this.context.put(ViewTag.RENDER, this.agentRender);

        Distribution distribution = businessUnit.getDistribution();
        Project project = businessUnit.getBusinessModel().getProject();
		this.context.put(ViewTag.TITLE, BusinessUnit.getTitle(distribution, project));
		this.context.put(ViewTag.SUBTITLE, businessUnit.getLabel());

		this.context.put(ViewTag.MONET_LOGO_SPLASH, configuration.getMonetSplashLogoUrl());
		this.context.put(ViewTag.MODEL_LOGO, configuration.getBusinessModelLogoUrl());
		this.context.put(ViewTag.MODEL_LOGO_SPLASH, configuration.getBusinessModelSplashLogoUrl());

		String federationLogoUrl, federationSplashLogoUrl;
		if (AgentFilesystem.existFile(kernelConfiguration.getFederationLogoFile())) {
			federationLogoUrl = configuration.getFederationLogoUrl();
			federationSplashLogoUrl = configuration.getFederationSplashLogoUrl();
		} else {
			federationLogoUrl = federation.getLogoUrl();
			federationSplashLogoUrl = federation.getLogoUrl();
		}

		this.context.put(ViewTag.FEDERATION_LABEL, federation.getLabel());
		this.context.put(ViewTag.FEDERATION_URL, federation.getUri());
		this.context.put(ViewTag.FEDERATION_LOGO, federationLogoUrl);
		this.context.put(ViewTag.FEDERATION_LOGO_ORIGINAL, federation.getLogoUrl());
		this.context.put(ViewTag.FEDERATION_LOGO_SPLASH, federationSplashLogoUrl);

		if (businessUnit != null) this.context.put(Tags.BUSINESSUNIT, businessUnit);

		this.createCommonTags();
		this.generateTemplateParameters();
		this.printParameters();
	}

}