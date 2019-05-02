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

package org.monet.space.kernel;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.monet.metamodel.Manifest;
import org.monet.space.kernel.agents.*;
import org.monet.space.kernel.applications.Application;
import org.monet.space.kernel.components.*;
import org.monet.space.kernel.configuration.*;
import org.monet.space.kernel.constants.Common;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.translators.TranslatorsFactory;
import org.monet.space.kernel.utils.Resources;
import org.monet.space.kernel.utils.VersionHelper;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kernel {
	private static final String COMPONENT_FEDERATIONMONET = "federationmonet";
	private static final String COMPONENT_SECURITYMONET = "securitymonet";
	private static final String COMPONENT_PERSISTENCEMONET = "persistencemonet";
	private static final String COMPONENT_DOCUMENTSMONET = "documentsmonet";
	private static final String COMPONENT_DATAWAREHOUSEMONET = "datawarehousemonet";

	private Map<String, ComponentInfo> componentsInfo;
	private Map<String, ComponentType> componentTypes;
	private Map<String, ApplicationInfo> applications;
	private Map<String, ComponentInfo> components;
	private Map<String, TranslatorInfo> translators;
	private AgentLogger agentLogger;
	private static Kernel instance;

	private Kernel() {
		this.componentsInfo = new HashMap<>();
		this.componentTypes = new HashMap<>();
		this.applications = new HashMap<>();
		this.components = new HashMap<>();
		this.translators = new HashMap<>();
		this.agentLogger = AgentLogger.getInstance();
	}

	private String getComponentsDataDirname() {
		return this.getConfiguration().getAppDataComponentsDir();
	}

	private Boolean loadComponentsInfo() {
		String[] components = AgentFilesystem.listDir(this.getComponentsDataDirname());
		Integer pos;

		this.componentsInfo.clear();

		if (components != null) {
			for (pos = 0; pos < components.length; pos++) {
				ComponentInfo componentInfo = new ComponentInfo(components[pos]);
				this.componentsInfo.put(componentInfo.getCode(), componentInfo);
			}
		}

		// Internal default components
		this.componentsInfo.put(COMPONENT_FEDERATIONMONET, new InternalComponentInfo(COMPONENT_FEDERATIONMONET));
		this.componentsInfo.put(COMPONENT_DOCUMENTSMONET, new InternalComponentInfo(COMPONENT_DOCUMENTSMONET));
		this.componentsInfo.put(COMPONENT_PERSISTENCEMONET, new InternalComponentInfo(COMPONENT_PERSISTENCEMONET));
		this.componentsInfo.put(COMPONENT_SECURITYMONET, new InternalComponentInfo(COMPONENT_SECURITYMONET));
		this.componentsInfo.put(COMPONENT_DATAWAREHOUSEMONET, new InternalComponentInfo(COMPONENT_DATAWAREHOUSEMONET));

		return true;
	}

	private void initializeDatabase() {
		Configuration configuration = this.getConfiguration();
		String databaseType = configuration.getValue(Configuration.JDBC_TYPE);

		AgentDatabase.setType(databaseType);
		AgentDatabase.getInstance().initialize(configuration.getDatabaseSource());
	}

	private void registerListeners() {
		AgentNotifier agentNotifier = AgentNotifier.getInstance();
		agentNotifier.register(Common.Codes.BPI, org.monet.space.kernel.listeners.ListenerBPI.class);
		agentNotifier.register(Common.Codes.LOG_SUBSCRIBER, org.monet.space.kernel.listeners.ListenerLogSubscriber.class);
		agentNotifier.register(Common.Codes.SERVICES_LISTENER, org.monet.space.kernel.listeners.ListenerTasks.class);
		agentNotifier.register(Common.Codes.NEWS_POST_LISTENER, org.monet.space.kernel.listeners.ListenerNewsPost.class);
		agentNotifier.register(Common.Codes.NOTIFICATIONS_LISTENER, org.monet.space.kernel.listeners.ListenerNotifications.class);
		agentNotifier.register(Common.Codes.RULE_MANAGER_LISTENER, org.monet.space.kernel.listeners.ListenerRuleManager.class);
		agentNotifier.register(Common.Codes.PUSH_LISTENER, org.monet.space.kernel.listeners.ListenerPushService.class);
		agentNotifier.register(Common.Codes.MOBILE_PUSH_NOTIFICATIONS_LISTENER, org.monet.space.kernel.listeners.ListenerMobilePushNotifications.class);
	}

	private void runComponents() {
        try {
			for (ComponentInfo componentInfo : getComponents().values()) {
				Class<?> componentClass = Class.forName(componentInfo.getClassName());
				Method method = componentClass.getMethod("getInstance", new Class[0]);
				Component component = (Component) method.invoke(new Object[0], new Object[0]);
				component.run();
			}
		} catch (Exception exception) {
			throw new SystemException(ErrorCode.BUSINESS_UNIT_COULD_NOT_START, null, exception);
		}
	}

	private void stopComponents() {
        try {
			for (ComponentInfo componentInfo : getComponents().values()) {
				Class<?> componentClass = Class.forName(componentInfo.getClassName());
				Method method = componentClass.getMethod("getInstance", new Class[0]);
				Component component = (Component) method.invoke(new Object[0], new Object[0]);
				component.stop();
			}
		} catch (Exception exception) {
			throw new SystemException(ErrorCode.BUSINESS_UNIT_COULD_NOT_STOP, null, exception);
		}
	}

	private void registerTranslators() {
		Map<String, TranslatorInfo> translators = getTranslators();
		TranslatorsFactory translatorsFactory = TranslatorsFactory.getInstance();

		try {
			for (TranslatorInfo translatorInfo : translators.values()) {
				translatorsFactory.register(translatorInfo.getFormat(), Class.forName(translatorInfo.getClassName()));
			}
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.BUSINESS_UNIT_COULD_NOT_START, null, oException);
		}
	}

	public synchronized static Kernel getInstance() {
		if (instance == null)
			instance = new Kernel();
		return instance;
	}

	public String getVersion() {
		return Manifest.getVersion();
	}

    public String getDatabaseVersion() {
        Map<String, Object> parameters = new HashMap<>();
        ResultSet result = null;
        AgentDatabase agentDatabase = AgentDatabase.getInstance();

        try {
            result = agentDatabase.executeRepositorySelectQuery(Database.Queries.INFO_LOAD_VERSION, parameters);

            if (!result.next())
                return VersionHelper.getMajorVersion(this.getVersion()) + ".0";

            return result.getString("value");
        } catch (Exception exception) {
            return VersionHelper.getMajorVersion(this.getVersion()) + ".0";
        } finally {
            agentDatabase.closeQuery(result);
        }
    }

    public static void updateDatabaseVersion(Connection connection, String databaseVersion) {
        AgentDatabase agentDatabase = AgentDatabase.getInstance();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(Database.QueryFields.VALUE, databaseVersion);

        agentDatabase.executeRepositoryUpdateQuery(connection, Database.Queries.INFO_UPDATE_VERSION, parameters);
    }

    public boolean isRunning() {
		return BusinessUnit.isRunning();
	}

	public Date getRunningDate() {
		return BusinessUnit.getRunningDate();
	}

	public Boolean run(ConfigurationMap map, DatabaseConfiguration database) {
		DatabaseLoader.load(database);
		return run(map);
	}

	public Boolean run(ConfigurationMap map) {
		String content = "";
		BusinessUnit businessUnit;

		try {
			content = AgentFilesystem.readStream(Resources.getAsStream("/kernel/monet.xml"));
		} catch (IOException e) {
			this.agentLogger.error("Could not read monet.xml file!", e);
		}

		if (map != null) ConfigurationLoader.load(map);
		this.loadComponentsInfo();
		this.unserializeFromXML(content);
		this.initializeDatabase();

        if (! AgentUpgrade.update()) {
            this.agentLogger.error("An error found executing monet update scripts", null);
            throw new RuntimeException("An error found executing monet update scripts");
        }

		this.registerListeners();
		this.runComponents();
		this.runApplications();
		this.registerTranslators();

		businessUnit = BusinessUnit.reload();
		if (BusinessUnit.autoRun())
			businessUnit.run();

		ComponentFederation.getInstance().reset(false);

		this.agentLogger.info(String.format("Monet running for %s!", Context.getInstance().getFrameworkName()));

		return true;
	}

	public void runApplications() {
        String sClassName = "";

		try {
			for (ApplicationInfo applicationInfo : getApplications().values()) {
				sClassName = applicationInfo.getClassName();
				Class<?> applicationClass = Class.forName(sClassName);
				Method method = applicationClass.getMethod("getInstance", new Class[0]);
				Application application = (Application) method.invoke(new Object[0], new Object[0]);
				application.run();
				application.clean();
			}
			this.agentLogger.info(Context.getInstance().getFrameworkName() + " Applications running!");
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.BUSINESS_UNIT_COULD_NOT_START, sClassName, oException);
		}
	}

	public void stopApplications() {
        try {
			for (ApplicationInfo applicationInfo : getApplications().values()) {
				Class<?> applicationClass = Class.forName(applicationInfo.getClassName());
				Method method = applicationClass.getMethod("getInstance", new Class[0]);
				Application application = (Application) method.invoke(new Object[0], new Object[0]);
				application.stop();
			}
			this.agentLogger.info(Context.getInstance().getFrameworkName() + " Applications stopped!");
		} catch (Exception exception) {
			throw new SystemException(ErrorCode.BUSINESS_UNIT_COULD_NOT_START, null, exception);
		}
	}

	public void stop() throws SystemException {
		this.stopApplications();
		this.stopComponents();
		BusinessUnit.getInstance().stop();
		this.agentLogger.info("Monet stopped!");
	}

	public Configuration getConfiguration() {
		return Configuration.getInstance();
	}

	public Map<String, ComponentInfo> getAvailableComponents() {
		return this.componentsInfo;
	}

	public ComponentInfo getComponentInfo(String code) {
		return this.componentsInfo.get(code);
	}

	public Map<String, ComponentType> getComponentTypes() {
		return this.componentTypes;
	}

	public ComponentType getComponentType(String code) {
		return this.componentTypes.get(code);
	}

	public Map<String, ApplicationInfo> getApplications() {
		return this.applications;
	}

	public Map<String, ComponentInfo> getComponents() {
		return this.components;
	}

	public Map<String, TranslatorInfo> getTranslators() {
		return this.translators;
	}

	public boolean isCompatible(String version) {
        String mayorVersion = VersionHelper.getMajorVersion(version);
        String mayorSpaceVersion = VersionHelper.getMajorVersion(getVersion());
		return mayorVersion.equals(mayorSpaceVersion);
	}

	@SuppressWarnings("unchecked")
	public Boolean unserializeFromXML(String sContent) {
		SAXBuilder builder = new SAXBuilder();
		StringReader reader;
		org.jdom.Document document;
		Element node;
		List<Element> applications, components, translators, componentTypes;

		if (sContent.equals(Strings.EMPTY))
			return true;
		while (!sContent.substring(sContent.length() - 1).equals(">"))
			sContent = sContent.substring(0, sContent.length() - 1);

		reader = new StringReader(sContent);

		this.componentTypes.clear();
		this.applications.clear();
		this.components.clear();
		this.translators.clear();

		try {
			document = builder.build(reader);
			node = document.getRootElement();

			componentTypes = node.getChild("componenttypes").getChildren("componenttype");
			for (Element element : componentTypes) {
				ComponentType componentType = new ComponentType();
				componentType.unserializeFromXML(element);
				this.componentTypes.put(componentType.getCode(), componentType);
			}

			applications = node.getChild("applications").getChildren("application");
			for (Element element : applications) {
				this.applications.put(element.getAttributeValue("code"), new ApplicationInfo(element.getAttributeValue("code")));
			}

			components = node.getChild("components").getChildren("component");
			for (Element element : components) {
				this.components.put(element.getAttributeValue("code"), Kernel.getInstance().getComponentInfo(element.getAttributeValue("code")));
			}

			translators = node.getChild("translators").getChildren("translator");
			for (Element element : translators) {
				this.translators.put(element.getAttributeValue("code"), new TranslatorInfo(element.getAttributeValue("code")));
			}
		} catch (JDOMException | IOException exception) {
			throw new DataException(ErrorCode.UNSERIALIZE_BUSINESS_UNIT_FROM_XML, sContent, exception);
		}

        return true;
	}

	public void reset() {
		ComponentDatawareHouse.getInstance().reset();
		ComponentDocuments.getInstance().reset();
		ComponentFederation.getInstance().reset();
		ComponentPersistence.getInstance().reset();
		ComponentSecurity.getInstance().reset();

		AgentNotifier.getInstance().notify(new MonetEvent(MonetEvent.KERNEL_RESET, null, null));
	}

}