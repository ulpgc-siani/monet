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
import org.monet.metamodel.Definition;
import org.monet.metamodel.Distribution;
import org.monet.metamodel.Project;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLauncher;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.extension.Enricher;
import org.monet.space.kernel.guice.InjectorFactory;
import org.monet.space.kernel.library.LibraryMatcher;
import org.monet.space.kernel.machines.ttm.TimerService;
import org.monet.space.kernel.utils.PackageProjectHelper;
import org.monet.space.kernel.utils.StreamHelper;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.Matcher;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Root(name = "space")
public class BusinessUnit extends BaseObject {
	private BusinessModel businessModel;
	private Date runningDate;
	private AgentLogger agentLogger;
	private Distribution distribution;

	private static TimeZone timeZone;
	private static final String SPACE_FILENAME = "space.xml";
	private static final Distribution.TimezoneEnumeration DEFAULT_TIMEZONE = Distribution.TimezoneEnumeration.ATLANTIC_BAR_CANARY;

	public static TimeZone getTimeZone() {

		if (timeZone != null)
			return timeZone;

		Distribution.TimezoneEnumeration timeZoneValue = getInstance().getDistribution().getTimezone();
		if (timeZoneValue == null)
			timeZoneValue = DEFAULT_TIMEZONE;

		timeZone = TimeZone.getTimeZone(getTimeZoneId(timeZoneValue));

		return timeZone;
	}

	public static class UpdateInfo {

		public static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
		private
		@Attribute(name = "date")
		Date date;
		public UpdateInfo(@Attribute(name = "date") Date date) {
			this.date = date;
		}

		public Date getDate() {
			return this.date;
		}

	}

	private static String getTimeZoneId(Distribution.TimezoneEnumeration timeZoneValue) {
		String timeZoneId = timeZoneValue.toString().replace("_PLUS_", "+").replace("_MINUS_", "-").replace("_COLON_", ":").replace("_BAR_", "/").toLowerCase();
		String[] availableIDs = TimeZone.getAvailableIDs();

		for (String availableID : availableIDs) {
			if (availableID.toLowerCase().equals(timeZoneId))
				return availableID;
		}

		return "Atlantic/Canary";
	}

	@Attribute(name = "name", required = true)
	private String name;

	@Element(name = "label", required = false)
	private String label;

	@Element(name = "updated", required = false)
	private UpdateInfo updateInfo;

	@Element(name = "federation", required = true)
	private Federation federation;

	private static BusinessUnit instance;

	private BusinessUnit() {
		super();
		this.businessModel = new BusinessModel();
		this.federation = new Federation("", "");
		this.agentLogger = AgentLogger.getInstance();
		this.runningDate = null;
	}

	public synchronized static BusinessUnit getInstance() {
		if (instance == null) instance = new BusinessUnit();
		return instance;
	}

	public void setUpdateInfo(UpdateInfo updateInfo) {
		this.updateInfo = updateInfo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public UpdateInfo getUpdateInfo() {
		return this.updateInfo;
	}

	public Federation getFederation() {
		return this.federation;
	}

	public void setFederation(Federation federation) {
		this.federation = federation;
	}

	public BusinessModel getBusinessModel() {
		return this.businessModel;
	}

	public void setBusinessModel(BusinessModel businessModel) {
		this.businessModel = businessModel;
	}

	public void setDistribution(Distribution distribution) {
		this.distribution = distribution;
	}

	public String getFilename(String filename) {
		return this.businessModel.getFilename(filename);
	}

	public String getFileContent(String filename) {
		return AgentFilesystem.readFile(getFilename(filename));
	}

	public String getAbsoluteFilename(String filename) {
		return getFilename(filename);
	}

	public String getTemplateFilename(String codeType, String viewType, String viewMode) {
		String templateFilename;
		Integer index;

		if ((templateFilename = this.businessModel.getTemplateFilename(codeType, viewType, viewMode)) == null) {
			if ((index = codeType.lastIndexOf(Strings.DOT)) == -1) return null;
			codeType = codeType.substring(0, index);
			templateFilename = this.getTemplateFilename(codeType, viewType, viewMode);
		}

		return templateFilename;
	}

	public Distribution getDistribution() {
		return distribution;
	}

	public static boolean isRunning() {
        return instance != null && instance.runningDate != null;
    }

	public static Date getRunningDate() {
		if (instance == null) return null;
		return instance.runningDate;
	}

	public static Boolean autoRun() {
		return Configuration.getInstance().autoRun();
	}

	public void run() throws SystemException {
        runningDate = new Date();
        Enricher.getInstance().setHistoryStoreLink(HistoryStoreProvider.getInstance());
        InjectorFactory.getInstance().getInstance(TimerService.class).init();

		try {
			AgentLauncher.getInstance().init();
		} catch (IllegalStateException ex) {
			// task is already scheduled. Nothing to do.
		}
		agentLogger.info(Context.getInstance().getFrameworkName() + " running!");
	}

	public void stop() throws SystemException {
		runningDate = null;
		agentLogger.info(Context.getInstance().getFrameworkName() + " stopped!");
	}

	public boolean isInstalled() {
		String absoluteFilename = Configuration.getInstance().getBusinessSpaceDir() + Strings.BAR45 + SPACE_FILENAME;
		return AgentFilesystem.existFile(absoluteFilename) && this.businessModel.isInstalled();
	}

	public static BusinessUnit reload() {
		Matcher dateMatcher = LibraryMatcher.dateMatcher(UpdateInfo.DATE_FORMAT);
		Persister persister = new Persister(dateMatcher);
		FileInputStream inputStream = null;
		BusinessUnit businessUnit;

		String absoluteFilename = Configuration.getInstance().getBusinessSpaceDir() + Strings.BAR45 + SPACE_FILENAME;
		if (!AgentFilesystem.existFile(absoluteFilename)) {
			BusinessUnit.instance = new BusinessUnit();
			return BusinessUnit.instance;
		}

		try {
			inputStream = new FileInputStream(absoluteFilename);
			businessUnit = persister.read(BusinessUnit.class, inputStream);
			businessUnit.businessModel = BusinessModel.reload();
			businessUnit.distribution = reloadDistribution();
			Dictionary.getInstance().reset(businessUnit.distribution, businessUnit.businessModel.getProject());
			instance = businessUnit;
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
			throw new SystemException(ErrorCode.BUSINESS_MODEL_DEFINITION_ERROR, SPACE_FILENAME);
		} finally {
			StreamHelper.close(inputStream);
		}

		return businessUnit;
	}

	public static boolean checkDistributionExists(File businessModelDir) {
		Map<String, String> packageManifest = PackageProjectHelper.getPackageManifest(businessModelDir);
		String distributionClassname = packageManifest.get("distribution").replace(".", "/") + ".class";
		return new File(Configuration.getInstance().getBusinessModelClassesDir(businessModelDir.getAbsolutePath()) + "/" + distributionClassname).exists();
	}

	public static Distribution reloadDistribution() {
		HashMap<String, String> packageManifest = PackageProjectHelper.getPackageManifest(new File(Configuration.getInstance().getBusinessModelDir()));

		if (packageManifest == null)
			return new Distribution() {
			};

		try {
			String distributionClassname = packageManifest.get("distribution");
			Class<?> distributionClass = Class.forName(distributionClassname, true, new BusinessModelClassLoader());
			return (Distribution) distributionClass.newInstance();
		} catch (Throwable e) {
			AgentLogger.getInstance().error(e);
			return new Distribution() {
			};
		}
	}

	public void save() {
		Matcher dateMatcher = LibraryMatcher.dateMatcher(UpdateInfo.DATE_FORMAT);
		Persister persister = new Persister(dateMatcher);
		final String filename = "space.xml";

		try {
			persister.write(this, new File(Configuration.getInstance().getBusinessSpaceDir() + Strings.BAR45 + filename));
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
			throw new SystemException(ErrorCode.BUSINESS_MODEL_DEFINITION_ERROR, filename);
		}
	}

	public Page loadNodeHelperPage(String code) {
		Definition definition = this.getBusinessModel().getDictionary().getDefinition(code);
		return loadHelperPage(code, definition == null ? "" : Language.getInstance().getModelResource(definition.getHelp()));
	}

	public Page loadHelperPage(String path) {
        return loadHelperPage(code, path);
	}

    private Page loadHelperPage(String code, String path) {
        Page page = new Page();
        page.setCode(code);
        page.setFilename(path);
        return page;
    }

    public JSONObject toJson() {
        return new JSONObject();
	}

    public String getTitle() {
        return getTitle(getDistribution(), getBusinessModel().getProject());
    }

    public static String getTitle(Distribution distribution, Project project) {
        if (distribution.getTitle() != null)
            return Language.getInstance().getModelResource(distribution.getTitle());

        if (project.getTitle() != null)
            return Language.getInstance().getModelResource(project.getTitle());

        return "";
    }

    public String getSubTitle() {
        return getSubTitle(getDistribution(), getBusinessModel().getProject());
    }

    public static String getSubTitle(Distribution distribution, Project project) {
        if (distribution.getSubtitle() != null)
            return Language.getInstance().getModelResource(distribution.getSubtitle());

        if (project.getSubtitle() != null)
            return Language.getInstance().getModelResource(project.getSubtitle());

        return "";
    }

	public void serializeToXML(XmlSerializer serializer, int depth) {
	}
}