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

import org.monet.metamodel.Distribution;
import org.monet.metamodel.Project;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Context.ContextInfo;
import org.monet.space.kernel.utils.StreamHelper;
import org.monet.space.setup.control.constants.Parameter;
import org.monet.space.setup.control.constants.SessionVariable;
import org.monet.space.setup.core.constants.Tags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ViewApplication extends View {
	private Boolean isBusinessUnitStarted;
	private BusinessUnit businessUnit;
	private Map<String, ComponentInfo> componentsInfoMap;
	private Map<String, ComponentType> componentTypesMap;
	private Map<String, Master> mastersMap;
	private String selectedTab;

	public ViewApplication(org.monet.space.setup.presentation.user.util.Context oContext, String codeLanguage) {
		super(oContext, codeLanguage);
		this.isBusinessUnitStarted = false;
		this.businessUnit = null;
		this.componentsInfoMap = null;
		this.componentTypesMap = null;
		this.mastersMap = null;
	}

	public void setIsBusinessUnitStarted(Boolean value) {
		this.isBusinessUnitStarted = value;
	}

	public void setBusinessUnit(BusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}

	public void setComponentsInfo(Map<String, ComponentInfo> componentsInfoMap) {
		this.componentsInfoMap = componentsInfoMap;
	}

	public void setComponentTypes(Map<String, ComponentType> componentTypesMap) {
		this.componentTypesMap = componentTypesMap;
	}

	public void setMasters(Map<String, Master> mastersMap) {
		this.mastersMap = mastersMap;
	}

	public void setSelectedTab(String tab) {
		this.selectedTab = tab;
	}

	private void printComponents() {
		Map<String, Map<String, ComponentInfo>> components = new HashMap<>();

		if (this.componentsInfoMap == null) return;

		for (ComponentInfo componentInfo : this.componentsInfoMap.values()) {
			if (!components.containsKey(componentInfo.getCodeType()))
				components.put(componentInfo.getCodeType(), new HashMap<String, ComponentInfo>());
			components.get(componentInfo.getCodeType()).put(componentInfo.getCode(), componentInfo);
		}

		this.context.put(Tags.COMPONENTS, components);
	}

	private void printMasters() {
		if (this.mastersMap == null) return;
		this.context.put(Tags.MASTERS, this.mastersMap);
	}

	private void printVersionInfo() {
		String versionFile = org.monet.space.kernel.model.Context.getInstance().getVersionFilename();
		StringBuilder versionInfo = new StringBuilder();
		Reader is = null;
		BufferedReader br = null;

		if (!AgentFilesystem.existFile(versionFile)) return;

		try {
			is = AgentFilesystem.getReader(versionFile);
			br = new BufferedReader(is);
			String read = br.readLine();

			while (read != null) {
				versionInfo.append(read).append("<br/>");
				read = br.readLine();
			}

		} catch (IOException e) {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
				}
			}
			StreamHelper.close(is);
		}

		this.context.put(Tags.VERSION_INFO, versionInfo.toString());
	}

	private void printContextInfo() {
		ContextInfo contextInfo = org.monet.space.kernel.model.Context.getInstance().getInfo();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String currentDate = dateFormat.format(contextInfo.Date);

		this.context.put(Tags.APPLICATION_DIR, contextInfo.ApplicationDir);
		this.context.put(Tags.HOME_DIR, contextInfo.HomeDir);
		this.context.put(Tags.CONTEXT_NAME, contextInfo.ContextName);
		this.context.put(Tags.CHARSET, contextInfo.Charset);
		this.context.put(Tags.CURRENT_DATE, currentDate);
	}

	private void printBusinessModelInfo() {
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Distribution distribution = businessUnit.getDistribution();
		Project project = businessUnit.getBusinessModel().getProject();

		if (distribution != null) {
			this.context.put(Tags.DISTRIBUTION, "exists");
			this.context.put(Tags.DISTRIBUTION_NAME, distribution.getName());
			this.context.put(Tags.DISTRIBUTION_TITLE, project.getTitle());
			this.context.put(Tags.DISTRIBUTION_SUBTITLE, project.getSubtitle());
			this.context.put(Tags.DISTRIBUTION_AUTHOR, project.getAuthor());
			this.context.put(Tags.DISTRIBUTION_FEDERATION, distribution.getFederation().getTitle());
			this.context.put(Tags.DISTRIBUTION_VERSION, project.getVersion());
			this.context.put(Tags.DISTRIBUTION_METAMODEL_VERSION, project.getMetamodelVersion());
			this.context.put(Tags.DISTRIBUTION_RELEASE, project.getRelease());
		}
	}

	private void printDistribution() {
		this.printVersionInfo();
		this.printBusinessModelInfo();
		this.printContextInfo();
	}

	private Boolean printComponentTypes() {
		if (this.componentTypesMap != null) this.context.put(Tags.COMPONENT_TYPES, this.componentTypesMap);
		return true;
	}

	public void execute(Writer writer) {
		Session session = org.monet.space.kernel.model.Context.getInstance().getCurrentSession();
		String codeError = session.getVariable(SessionVariable.ERROR);

		super.execute(writer);

		this.context.put(Tags.IS_BUSINESS_UNIT_STARTED, this.isBusinessUnitStarted);
		this.context.put(Tags.COMPONENT_PREFIX, Parameter.COMPONENT_PREFIX);
		this.context.put(Tags.TAB, this.selectedTab);
		this.context.put(Tags.RUNNING, BusinessUnit.isRunning());

		if (this.businessUnit != null) this.context.put(Tags.BUSINESSUNIT, this.businessUnit);
		if (codeError != null) {
			this.context.put(Tags.ERROR_CODE, codeError);
			session.setVariable(SessionVariable.ERROR, null);
		}

		this.printComponents();
		this.printComponentTypes();
		this.printMasters();
		this.printDistribution();

		this.oAgentTemplates.merge("/setup/templates/application.tpl", this.context, writer);
	}

}