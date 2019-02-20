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

import net.minidev.json.JSONObject;
import org.monet.metamodel.Project;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.translators.Translator;
import org.monet.space.kernel.translators.TranslatorsFactory;
import org.monet.space.kernel.utils.PackageProjectHelper;
import org.monet.space.kernel.utils.cache.CacheFactory;

import java.io.File;
import java.util.HashMap;

public class BusinessModel {
	private AgentLogger agentException = AgentLogger.getInstance();
	private Project project;

	public static HashMap<String, HashMap<String, String>> definitionsMap = new HashMap<String, HashMap<String, String>>();

	public Dictionary getDictionary() {
		return Dictionary.getInstance();
	}

	public String getFilename(String sFilename) {
		String absoluteFilename = Configuration.getInstance().getBusinessModelResourcesDir() + Strings.BAR45 + sFilename;

		if (!AgentFilesystem.existFile(absoluteFilename)) {
			DataException ex = new DataException(ErrorCode.BUSINESS_MODEL_FILE_NOT_FOUND, sFilename);
			agentException.errorInModel(new Exception("Not found file: " + sFilename));
			throw ex;
		}

		return absoluteFilename;
	}

	public String getFileContent(String sFilename) {
		return AgentFilesystem.readFile(this.getFilename(sFilename));
	}

	public String getAbsoluteFilename(String sFilename) {
		return this.getFilename(sFilename);
	}

	public String getTemplateFilename(String codeType, String sViewType, String sViewMode) {
		Configuration oConfiguration = Configuration.getInstance();
		String sTemplateFilename, sTemplatesDirname;

		sTemplatesDirname = oConfiguration.getBusinessModelCompilationDir();
		sTemplateFilename = sViewType + Strings.BAR45 + codeType + Strings.BAR45 + sViewMode;

		String sFilename = sTemplatesDirname + Strings.BAR45 + sTemplateFilename;
		if (!AgentFilesystem.existFile(sFilename)) {
			agentException.errorInModel(new Exception("Not found template: " + sFilename));
			return null;
		}

		return sTemplateFilename;
	}

	public Translator getTranslator(String codeFormat) {
		TranslatorsFactory oFactory = TranslatorsFactory.getInstance();
		Translator oTranslator = (Translator) oFactory.get(codeFormat);
		return oTranslator;
	}

	public boolean isInstalled() {
		String absoluteFilename = Configuration.getInstance().getBusinessModelDir();
		return AgentFilesystem.existFile(absoluteFilename);
	}

	public static BusinessModel reload() {
		BusinessModel businessModel = new BusinessModel();

		String filename = null;
		try {
			BusinessModel.definitionsMap.clear();
			BusinessModelClassLoader.reset();
			businessModel.project = reloadProject();
			CacheFactory.getInstance().resetAll();
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
			throw new SystemException(ErrorCode.BUSINESS_MODEL_DEFINITION_ERROR, filename);
		}

		return businessModel;
	}

	public static final boolean checkProjectExists(File businessModelDir) {
		HashMap<String, String> packageManifest = PackageProjectHelper.getPackageManifest(businessModelDir);
		String projectClassname = packageManifest.get("project").replace(".", "/") + ".class";
		return new File(Configuration.getInstance().getBusinessModelClassesDir(businessModelDir.getAbsolutePath()) + "/" + projectClassname).exists();
	}

	private static final Project reloadProject() throws Exception {
		HashMap<String, String> packageManifest = PackageProjectHelper.getPackageManifest(new File(Configuration.getInstance().getBusinessModelDir()));
		if (packageManifest == null)
			return new Project() {
				public String getMetamodelVersion() {
					return null;
				}
			};
		try {
			String projectClassname = packageManifest.get("project");
			Class<?> projectClass = Class.forName(projectClassname, true, new BusinessModelClassLoader());

			Project project = (Project) projectClass.newInstance();
			project.setRelease(packageManifest.get("release"));
			project.setUUID(packageManifest.get("uuid"));

			return project;
		} catch (Throwable e) {
			AgentLogger.getInstance().error(e);
			return new Project() {
				public String getMetamodelVersion() {
					return null;
				}
			};
		}
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();

		json.put("code", this.project.getName());
		json.put("version", this.project.getVersion());
		json.put("label", Language.getInstance().getModelResource(this.project.getTitle()));
		// json.put("update-date", getUpdateInfo().getDate());
		json.put("update_date", 0);

		JSONObject jsonImages = new JSONObject();
		jsonImages.put("top", "images/project.top.png");
		jsonImages.put("init", "images/project.initial.png");
		json.put("images", jsonImages);

		return json;
	}

	public static BusinessModel getInstance() {
		return BusinessUnit.getInstance().getBusinessModel();
	}

	public Project getProject() {
		return this.project;
	}

}