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

import org.monet.metamodel.*;
import org.monet.metamodel.DatastoreDefinitionBase.CubeProperty;
import org.monet.metamodel.DatastoreDefinitionBase.DimensionProperty;
import org.monet.metamodel.interfaces.IsInitiable;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.metamodel.internal.LayoutDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.metamodel.internal.TaskOrderDefinition;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.bpi.java.locator.PackageReader;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.DataException;
import org.xmlpull.v1.XmlSerializer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

public class Dictionary extends org.monet.metamodel.Dictionary implements ILoadListener {
	private Map<String, NodeDefinition> nodesMap;
	private Map<String, ContainerDefinition> containersMap;
	private Map<String, DesktopDefinition> desktopsMap;
	private List<DesktopDefinition> desktopsList;
	private Map<String, CollectionDefinition> collectionsMap;
	private Map<String, FormDefinition> formsMap;
	private Map<String, DocumentDefinition> documentsMap;
	private List<DocumentDefinition> documentsList;
	private Map<String, CatalogDefinition> catalogsMap;
	private Map<String, TaskDefinition> servicesMap;
	private Map<String, JobDefinition> jobsMap;
	private Map<String, TaskDefinition> taskMap;
	private List<TaskDefinition> taskList;
	private Map<String, IndexDefinition> indexMap;
	private List<IndexDefinition> indexList;
	private Map<String, SourceDefinition> sourceMap;
	private List<SourceDefinition> sourceList;
	private Map<String, ThesaurusDefinition> thesaurusMap;
	private List<ThesaurusDefinition> thesaurusList;
	private Map<String, GlossaryDefinition> glossaryMap;
	private List<GlossaryDefinition> glossaryList;
	private Map<String, MeasureUnitDefinition> measureUnitMap;
	private List<MeasureUnitDefinition> measureUnitList;
	private Map<String, KpiDefinition> kpiMap;
	private List<KpiDefinition> kpiList;
	private List<ImporterDefinition> importersList;
	private Map<String, ImporterDefinition> importersMap;
	private Map<String, ExporterDefinition> exportersMap;
	private Map<String, DashboardDefinition> dashboardMap;
	private List<DashboardDefinition> dashboardList;
	private Map<String, Class<?>> indicatorFormulaClassMap;
	private Map<String, Class<?>> taxonomyClassifierClassMap;
	private Map<String, DatastoreDefinition> datastoreMap;
	private List<DatastoreDefinition> datastoreList;
	private Map<String, DimensionProperty> dimensionMap;
	private Map<String, CubeProperty> cubeMap;
	private Map<String, Definition> definitionMap;
	private List<Definition> definitionList;
	private List<NodeDefinition> singletonDefinitionList;
	private Map<String, RoleDefinition> rolesMap;
	private List<RoleDefinition> rolesList;
	private List<String> rolesNames;
	private Map<String, FieldProperty> fieldsMap;
	private Map<String, DatastoreBuilderDefinition> datastoreBuilderMap;
	private List<DatastoreBuilderDefinition> datastoreBuilderList;
	private Map<String, LayoutDefinition> layoutDefinitionMap;
	private String basePackage = null;

	private Map<String, List<DatastoreBuilderDefinition>> datastoreBuilders = new HashMap<>();
	private List<NodeDefinition> environments;
	private AgentLogger agentLogger = AgentLogger.getInstance();
	private org.monet.metamodel.interfaces.Language defaultLanguage;
	private Map<String, org.monet.metamodel.interfaces.Language> languages = new HashMap<>();
	public static final String DICTIONARY = "Dictionary";

	private Map<String, String> preDependencyMap = new HashMap<>();
	private Map<String, List<String>> dependencyMap = new HashMap<>();
	private Map<String, String> defineMap = new HashMap<>();
	private List<Replacer> replacersList = new ArrayList<>();
	private Map<String, List<String>> ontologiesMap = new HashMap<>();
	private Map<String, List<ThesaurusDefinition>> thesaurusListGeneratedByIndex = new HashMap<>();

	private Dictionary() {
		super();
		this.linkLoadListener(this);
		this.nodesMap = new HashMap<>();
		this.containersMap = new HashMap<>();
		this.desktopsMap = new HashMap<>();
		this.desktopsList = new ArrayList<>();
		this.collectionsMap = new HashMap<>();
		this.formsMap = new HashMap<>();
		this.documentsMap = new HashMap<>();
		this.documentsList = new ArrayList<>();
		this.catalogsMap = new HashMap<>();
		this.servicesMap = new HashMap<>();
		this.jobsMap = new HashMap<>();
		this.taskMap = new HashMap<>();
		this.taskList = new ArrayList<>();
		this.indexMap = new HashMap<>();
		this.indexList = new ArrayList<>();
		this.sourceMap = new HashMap<>();
		this.sourceList = new ArrayList<>();
		this.thesaurusMap = new HashMap<>();
		this.thesaurusList = new ArrayList<>();
		this.glossaryMap = new HashMap<>();
		this.glossaryList = new ArrayList<>();
		this.measureUnitList = new ArrayList<>();
		this.measureUnitMap = new HashMap<>();
		this.dashboardList = new ArrayList<>();
		this.dashboardMap = new HashMap<>();
		this.indicatorFormulaClassMap = new HashMap<>();
		this.taxonomyClassifierClassMap = new HashMap<>();
		this.datastoreList = new ArrayList<>();
		this.datastoreMap = new HashMap<>();
		this.dimensionMap = new HashMap<>();
		this.cubeMap = new HashMap<>();
		this.kpiList = new ArrayList<>();
		this.kpiMap = new HashMap<>();
		this.importersList = new ArrayList<>();
		this.importersMap = new HashMap<>();
		this.exportersMap = new HashMap<>();
		this.environments = new ArrayList<>();
		this.definitionMap = new HashMap<>();
		this.definitionList = new ArrayList<>();
		this.singletonDefinitionList = new ArrayList<>();
		this.rolesMap = new HashMap<>();
		this.rolesList = new ArrayList<>();
		this.rolesNames = new ArrayList<>();
		this.fieldsMap = new HashMap<>();
		this.datastoreBuilderMap = new HashMap<>();
		this.datastoreBuilderList = new ArrayList<>();
		this.layoutDefinitionMap = new HashMap<>();
	}

	private <T> List<T> getDefinition(String key, Map<String, T> definitionMap) {
		List<T> definitions = new ArrayList<>();

		Definition mainDefinition = this.definitionMap.get(key.toLowerCase());
		String mainDefinitionName = null;
		if (mainDefinition != null)
			mainDefinitionName = mainDefinition.getName().toLowerCase();
		List<String> implementers = dependencyMap.get(mainDefinitionName);
		if (implementers == null) {
			DataException e = new DataException(ErrorCode.BUSINESS_MODEL_DEFINITION_NOT_FOUND, key);
			this.agentLogger.errorInModel(e);
			throw e;
		}
		if (implementers.size() == 0) {
			DataException e = new DataException(ErrorCode.BUSINESS_MODEL_DEFINITION_NO_IMPLEMENTATION_FOUND, key);
			this.agentLogger.errorInModel(e);
			throw e;
		}

		for (String implementerKey : implementers)
            definitions.add(definitionMap.get(implementerKey.toLowerCase()));

		return definitions;
	}

	public Collection<Definition> getAllDefinitions() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.definitionList;
	}

	public String getDefinitionCode(String key) {
		Definition definition = this.definitionMap.get(key.toLowerCase());
		if (definition == null)
			return null;
		return definition.getCode();
	}

	public NodeDefinition getNodeDefinition(String key) {
		return getDefinition(key, nodesMap).get(0);
	}

	public List<NodeDefinition> getAllImplementersOfNodeDefinition(String key) {
		return getDefinition(key, nodesMap);
	}

	public FormDefinition getFormDefinition(String key) {
		return getDefinition(key, formsMap).get(0);
	}

	public Boolean isFormDefinition(String key) {
		onLoad(this, Dictionary.DICTIONARY);
		return this.formsMap.containsKey(key.toLowerCase());
	}

	public Boolean isCatalogDefinition(String key) {
		onLoad(this, Dictionary.DICTIONARY);
		return this.catalogsMap.containsKey(key.toLowerCase());
	}

	public FieldProperty getFormFieldDefinition(String nodeKey, String fieldKey) {
		FormDefinition formDefinition = this.getFormDefinition(nodeKey);
		return formDefinition.getField(fieldKey);
	}

	public List<NodeDefinition> getEnvironmentDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.environments;
	}

	public Map<String, List<NodeDefinition>> getEnvironmentDefinitionListByRole() {
		Map<String, List<NodeDefinition>> environmentDefinitions = new HashMap<>();
		List<NodeDefinition> environmentDefinitionList = this.getEnvironmentDefinitionList();

		for (RoleDefinition roleDefinition : this.getRoleDefinitionList()) {
			String roleCode = roleDefinition.getCode();
			List<NodeDefinition> environmentDefinitionWithRole = new ArrayList<>();

			environmentDefinitions.put(roleCode, environmentDefinitionWithRole);
			for (NodeDefinition environmentDefinition : environmentDefinitionList) {
				if (environmentDefinition.isAbstract())
					continue;

				List<Ref> roleRefs = environmentDefinition.getRoles();
				for (Ref roleRef : roleRefs) {
					String environmentRoleCode = this.getDefinitionCode(roleRef.getValue());

					if (environmentRoleCode.equals(roleCode)) {
						environmentDefinitionWithRole.add(environmentDefinition);
						break;
					}
				}
			}
		}

		return environmentDefinitions;
	}

	public Map<String, List<DashboardDefinition>> getDashboardDefinitionListByRole() {
		Map<String, List<DashboardDefinition>> dashboardDefinitions = new HashMap<>();
		List<DashboardDefinition> dashboardDefinitionList = this.getDashboardDefinitionList();

		for (RoleDefinition roleDefinition : this.getRoleDefinitionList()) {
			String roleCode = roleDefinition.getCode();
			List<DashboardDefinition> dashboardDefinitionWithRole = new ArrayList<>();

			dashboardDefinitions.put(roleCode, dashboardDefinitionWithRole);
			for (DashboardDefinition dashboardDefinition : dashboardDefinitionList) {
				if (dashboardDefinition.isAbstract())
					continue;

				if (dashboardDefinition.getFor() == null)
					continue;

				List<Ref> roleRefs = dashboardDefinition.getFor().getRole();
				for (Ref roleRef : roleRefs) {
					String dashboardRoleCode = this.getDefinitionCode(roleRef.getValue());

					if (dashboardRoleCode.equals(roleCode)) {
						dashboardDefinitionWithRole.add(dashboardDefinition);
						break;
					}
				}
			}
		}

		return dashboardDefinitions;
	}

	public List<SerialFieldProperty> getSerialFieldPropertyList() {
		List<SerialFieldProperty> result = new ArrayList<>();
		for (Definition definition : getAllDefinitions()) {
			if (! (definition instanceof FormDefinition)) continue;
			FormDefinition formDefinition = (FormDefinition)definition;
			for (FieldProperty fieldProperty : formDefinition.getFields().values()) {
				if (! (fieldProperty instanceof SerialFieldProperty)) continue;
				result.add((SerialFieldProperty) fieldProperty);
			}
		}
		return result;
	}

	public List<NodeDefinition> getSingletonDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.singletonDefinitionList;
	}

	public DesktopDefinition getDesktopDefinition(String key) {
		return getDefinition(key, this.desktopsMap).get(0);
	}

	public List<DesktopDefinition> getDesktopDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.desktopsList;
	}

	public DocumentDefinition getDocumentDefinition(String key) {
		return this.getDefinition(key, this.documentsMap).get(0);
	}

	public List<DocumentDefinition> getDocumentDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.documentsList;
	}

	public CollectionDefinition getCollectionDefinition(String key) {
		return this.getDefinition(key, this.collectionsMap).get(0);
	}

	public CatalogDefinition getCatalogDefinition(String key) {
		return getDefinition(key, catalogsMap).get(0);
	}

	public TaskDefinition getTaskDefinitionByServiceName(String key) {
		return servicesMap.get(key);
	}

	public List<SetDefinition> getSetDefinitionsWithIndex(IndexDefinition definition) {
		Set<SetDefinition> result = new HashSet<>();
		String name = definition.getName();
		String code = definition.getCode();

		for (CatalogDefinition catalogDefinition : catalogsMap.values()) {
			String definitionKey = catalogDefinition.getIndex().getValue();
			if (definitionKey.equals(name) || definitionKey.equals(code)) {
				result.add(catalogDefinition);
			}
		}

		for (CollectionDefinition collectionDefinition : collectionsMap.values()) {
			Ref index = collectionDefinition.getIndex();
			if (index == null) continue;
			String definitionKey = index.getValue();
			if (definitionKey.equals(name) || definitionKey.equals(code)) {
				result.add(collectionDefinition);
			}
		}

		return new ArrayList<>(result);
	}

	public Collection<TaskDefinition> getTaskDefinitionsWithServiceName() {
		return servicesMap.values();
	}

	public Collection<JobDefinition> getJobDefinitions() {
		return jobsMap.values();
	}

	public TaskDefinition getTaskDefinition(String key) {
		return getDefinition(key, taskMap).get(0);
	}

	public IndexDefinition getIndexDefinition(String key) {
		if (key.equals(DescriptorDefinition.CODE))
			return new DescriptorDefinition();
		return getDefinition(key, indexMap).get(0);
	}

	public Collection<IndexDefinition> getIndexDefinitionList() {
		return indexList;
	}

	public List<ThesaurusDefinition> getThesaurusGeneratedByIndex(IndexDefinition definition) {
		return thesaurusListGeneratedByIndex.get(definition.getCode());
	}

	public List<ImporterDefinition> getImporterDefinitionList() {
		return importersList;
	}

	public ImporterDefinition getImporterDefinition(String key) {
		return getDefinition(key, importersMap).get(0);
	}

	public ExporterDefinition getExporterDefinition(String key) {
		return getDefinition(key, exportersMap).get(0);
	}

	public List<TaskDefinition> getTaskDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return taskList;
	}

	public List<ThesaurusDefinition> getThesaurusDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return thesaurusList;
	}

	public Map<String, SourceDefinition> getSourceDefinitionMap() {
		return this.sourceMap;
	}

	public List<SourceDefinition> getSourceDefinitionList() {
		return this.sourceList;
	}

	public Map<String, ThesaurusDefinition> getThesaurusDefinitionMap() {
		return this.thesaurusMap;
	}

	public List<GlossaryDefinition> getGlossaryDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.glossaryList;
	}

	public Map<String, GlossaryDefinition> getGlossaryDefinitionMap() {
		return this.glossaryMap;
	}

	public List<MeasureUnitDefinition> getMeasureUnitDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.measureUnitList;
	}

	public Map<String, MeasureUnitDefinition> getMeasureUnitDefinitionMap() {
		return this.measureUnitMap;
	}

	public MeasureUnitDefinition getMeasureUnitDefinition(String key) {
		return this.getDefinition(key, this.measureUnitMap).get(0);
	}

	public IndexDefinition locateIndex(NodeDefinition definition) {
		Dictionary dictionary = Dictionary.getInstance();

		if (definition.isContainer() ) {
			ContainerDefinition.ContainProperty containDefinition = ((ContainerDefinition)definition).getContain();
			if (containDefinition == null) return null;
			for (Ref contain : containDefinition.getNode()) {
				NodeDefinition childDefinition = dictionary.getNodeDefinition(contain.getValue());
				if (!childDefinition.isForm()) continue;
				return locateIndex((FormDefinition)childDefinition);
			}
		}
		else if (definition.isForm()) return locateIndex((FormDefinition) definition);
		else if (definition.isDocument()) {
			ArrayList<DocumentDefinitionBase.MappingProperty> mappingList = ((DocumentDefinition) definition).getMappingList();
			return mappingList.size() > 0 ? Dictionary.getInstance().getIndexDefinition(mappingList.get(0).getIndex().getValue()) : null;
		}

		return null;
	}

	private IndexDefinition locateIndex(FormDefinition definition) {
		ArrayList<FormDefinitionBase.MappingProperty> mappingList = definition.getMappingList();
		if (mappingList.size() <= 0) return null;
		return Dictionary.getInstance().getIndexDefinition(mappingList.get(0).getIndex().getValue());
	}

	public Map<String, FieldProperty> getFieldDefinitionMap() {
		return this.fieldsMap;
	}

	public boolean existsFieldDefinition(String key) {
		return this.fieldsMap.containsKey(key);
	}

	public FieldProperty getFieldDefinition(String key) {
		if (!this.fieldsMap.containsKey(key))
			return null;
		return this.fieldsMap.get(key);
	}

	public List<DashboardDefinition> getDashboardDefinitionList() {
		return this.dashboardList;
	}

	public Map<String, DashboardDefinition> getDashboardDefinitionMap() {
		return this.dashboardMap;
	}

	public DashboardDefinition getDashboardDefinition(String key) {
		return this.getDefinition(key, this.dashboardMap).get(0);
	}

	public Class<?> getDashboardIndicatorFormulaClass(DashboardDefinition definition, String indicatorCode) {
		return this.indicatorFormulaClassMap.get(indicatorCode);
	}

	public InputStream getDashboardIndicatorFormulaClassAsStream(DashboardDefinition definition, String indicatorCode) {
		String basePath = Configuration.getInstance().getBusinessModelClassesDir();
		String classPath = this.indicatorFormulaClassMap.get(indicatorCode).getName().replace(".", "/") + ".class";
		return AgentFilesystem.getInputStream(basePath + "/" + classPath);
	}

	public Class<?> getDashboardTaxonomyClassifierClass(DashboardDefinition definition, String taxonomyCode) {
		return this.taxonomyClassifierClassMap.get(taxonomyCode);
	}

	public InputStream getDashboardTaxonomyClassifierClassAsStream(DashboardDefinition definition, String taxonomyCode) {
		String basePath = Configuration.getInstance().getBusinessModelClassesDir();
		String classPath = this.taxonomyClassifierClassMap.get(taxonomyCode).getName().replace(".", "/") + ".class";
		return AgentFilesystem.getInputStream(basePath + "/" + classPath);
	}

	public List<DatastoreDefinition> getDatastoreDefinitionList() {
		return this.datastoreList;
	}

	public Map<String, DatastoreDefinition> getDatastoreDefinitionMap() {
		return this.datastoreMap;
	}

	public DatastoreDefinition getDatastoreDefinition(String key) {
		return this.getDefinition(key, this.datastoreMap).get(0);
	}

	public DimensionProperty getDimensionDefinition(String key) {

		if (this.dimensionMap.containsKey(key))
			return this.dimensionMap.get(key);

		return this.dimensionMap.get(key.toLowerCase());
	}

	public CubeProperty getCubeDefinition(String key) {

		if (this.cubeMap.containsKey(key))
			return this.cubeMap.get(key);

		return this.cubeMap.get(key.toLowerCase());
	}

	public SourceDefinition getSourceDefinition(String key) {
		return this.getDefinition(key, this.sourceMap).get(0);
	}

	public ThesaurusDefinition getThesaurusDefinition(String key) {
		return this.getDefinition(key, this.thesaurusMap).get(0);
	}

	public GlossaryDefinition getGlossaryDefinition(String key) {
		return this.getDefinition(key, this.glossaryMap).get(0);
	}

	public RoleDefinition getRoleDefinition(String key) {
		return this.getDefinition(key, this.rolesMap).get(0);
	}

	public boolean existsRoleDefinition(String key) {
		return this.rolesMap.containsKey(key.toLowerCase());
	}

	public List<RoleDefinition> getRoleDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.rolesList;
	}

	public List<String> getRolesNames() {
		return this.rolesNames;
	}

	public List<DatastoreBuilderDefinition> getDatastoreBuildersForNode(String nodeKey) {
		if (!datastoreBuilders.containsKey(nodeKey))
			return new ArrayList<>();
		return datastoreBuilders.get(nodeKey);
	}

	public List<DatastoreBuilderDefinition> getDatastoreBuilders() {
		return this.datastoreBuilderList;
	}

	public DatastoreBuilderDefinition getDatastoreBuilder(String key) {
		return this.datastoreBuilderMap.get(key.toLowerCase());
	}

	public Definition getDefinition(String key) {
		return this.getDefinition(key, this.definitionMap).get(0);
	}

	public boolean existsDefinition(String key) {
        onLoad(this, Dictionary.DICTIONARY);
        return key != null && this.definitionMap.containsKey(key.toLowerCase());
    }

	public boolean reset(Distribution distribution, Project project) {
		this.removeLoadedAttribute(Dictionary.DICTIONARY);
		onLoad(this, Dictionary.DICTIONARY);

		this.defineMap.clear();
		this.defineMap.putAll(project.getDefineMap());
		this.defineMap.putAll(distribution.getDefineMap());
		return true;
	}

	public String getVariable(String varName) {
		return this.defineMap.get(varName);
	}

	public File getLayoutDefinitionFile(String key) {
		return new File(Configuration.getInstance().getBusinessModelResourcesDir() + "/" + key);
	}

	public LayoutDefinition getLayoutDefinition(String key) {
		LayoutDefinition layoutDefinition = null;

		if (this.layoutDefinitionMap.containsKey(key))
			return this.layoutDefinitionMap.get(key);

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(LayoutDefinition.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			File layoutFile = new File(Configuration.getInstance().getBusinessModelResourcesDir() + "/" + key);
			layoutDefinition = (LayoutDefinition) unmarshaller.unmarshal(layoutFile);
			this.layoutDefinitionMap.put(key, layoutDefinition);
		} catch (JAXBException exception) {
			AgentLogger.getInstance().error(exception);
		}

		return layoutDefinition;
	}

	@SuppressWarnings("unchecked")
	private <T extends Definition> void addDefinition(Definition definition, Map<String, T> definitionMap, List<T> definitionList) {
		String code = definition.getCode().toLowerCase();
		String name = definition.getName().toLowerCase();

		if (definitionMap.containsKey(name))
			return;

		definitionMap.put(code, (T) definition);
		definitionMap.put(name, (T) definition);

		if (definition instanceof NodeDefinition) {
			NodeDefinition nodeDefinition = (NodeDefinition) definition;
			this.nodesMap.put(code, nodeDefinition);
			this.nodesMap.put(name, nodeDefinition);

			boolean isEnvironment = (nodeDefinition.isDesktop() || (nodeDefinition.isContainer() && ((ContainerDefinition) nodeDefinition).isEnvironment()));
			if (isEnvironment && !nodeDefinition.isAbstract())
				this.environments.add(nodeDefinition);

			if (nodeDefinition.isSingleton() && !nodeDefinition.isAbstract())
				this.singletonDefinitionList.add(nodeDefinition);
		}

		if (definition instanceof RoleDefinition)
			this.rolesNames.add(name);

		if (definitionList != null)
			definitionList.add((T) definition);

		if (definition instanceof DatastoreDefinition) {
			for (DimensionProperty dimensionDefinition : ((DatastoreDefinition) definition).getDimensionList()) {
				this.dimensionMap.put(name + "." + dimensionDefinition.getName().toLowerCase(), dimensionDefinition);
				this.dimensionMap.put(dimensionDefinition.getCode(), dimensionDefinition);
			}

			for (CubeProperty cubeDefinition : ((DatastoreDefinition) definition).getCubeList()) {
				this.cubeMap.put(name + "." + cubeDefinition.getName().toLowerCase(), cubeDefinition);
				this.cubeMap.put(cubeDefinition.getCode(), cubeDefinition);
			}
		}

		this.definitionMap.put(code, definition);
		this.definitionMap.put(name, definition);

		this.definitionList.add(definition);
	}

	private void prepareBuilders() {
		prepareDatastoreBuilders();
	}

	private void prepareDatastoreBuilders() {
		for (DatastoreBuilderDefinition builderDefinition : this.datastoreBuilderList) {
			String nodeDefinitionKey = builderDefinition.getSource().getValue();

			for (NodeDefinition nodeDefinition : this.getDefinition(nodeDefinitionKey, this.nodesMap)) {
				List<DatastoreBuilderDefinition> builders = this.datastoreBuilders.get(nodeDefinition.getName());
				if (builders == null) {
					builders = new ArrayList<>();
					this.datastoreBuilders.put(nodeDefinition.getName(), builders);
				}
				builders.add(builderDefinition);
			}
		}
	}

	public void replace(Class<? extends Definition> clazz, String name, String replacedName) {
		this.replacersList.add(new Replacer(clazz, name, replacedName));
		this.register(clazz, name, replacedName);
	}

	public void register(Class<? extends Definition> clazz, String name, String parentName) {
		Definition definition;
		AgentLogger logger = AgentLogger.getInstance();
		try {
			definition = clazz.newInstance();
			if (definition instanceof ContainerDefinition)
				this.addDefinition(definition, this.containersMap, null);
			else if (definition instanceof DesktopDefinition)
				this.addDefinition(definition, this.desktopsMap, this.desktopsList);
			else if (definition instanceof CollectionDefinition)
				this.addDefinition(definition, this.collectionsMap, null);
			else if (definition instanceof FormDefinition)
				this.addDefinition(definition, this.formsMap, null);
			else if (definition instanceof DocumentDefinition)
				this.addDefinition(definition, this.documentsMap, this.documentsList);
			else if (definition instanceof CatalogDefinition)
				this.addDefinition(definition, this.catalogsMap, null);
			else if (definition instanceof TaskDefinition) {
				this.addDefinition(definition, this.taskMap, this.taskList);
				if (definition instanceof ServiceDefinition)
					this.servicesMap.put(definition.getName(), (TaskDefinition) definition);
				else if (definition instanceof JobDefinition)
					this.jobsMap.put(definition.getName(), (JobDefinition) definition);
			} else if (definition instanceof IndexDefinition)
				this.addDefinition(definition, this.indexMap, this.indexList);
			else if (definition instanceof ThesaurusDefinition) {
				this.addDefinition(definition, this.thesaurusMap, this.thesaurusList);
				this.sourceMap.put(definition.getCode().toLowerCase(), (SourceDefinition) definition);
				this.sourceMap.put(definition.getName().toLowerCase(), (SourceDefinition) definition);
				this.sourceList.add((SourceDefinition) definition);
			} else if (definition instanceof GlossaryDefinition) {
				this.addDefinition(definition, this.glossaryMap, this.glossaryList);
				this.sourceMap.put(definition.getCode().toLowerCase(), (SourceDefinition) definition);
				this.sourceMap.put(definition.getName().toLowerCase(), (SourceDefinition) definition);
				this.sourceList.add((SourceDefinition) definition);
			} else if (definition instanceof KpiDefinition)
				this.addDefinition(definition, this.kpiMap, this.kpiList);
			else if (definition instanceof MeasureUnitDefinition)
				this.addDefinition(definition, this.measureUnitMap, this.measureUnitList);
			else if (definition instanceof DashboardDefinition)
				this.addDefinition(definition, this.dashboardMap, this.dashboardList);
			else if (definition instanceof DatastoreDefinition)
				this.addDefinition(definition, this.datastoreMap, this.datastoreList);
			else if (definition instanceof KpiDefinition)
				this.addDefinition(definition, this.kpiMap, this.kpiList);
			else if (definition instanceof ImporterDefinition)
				this.addDefinition(definition, this.importersMap, this.importersList);
			else if (definition instanceof ExporterDefinition)
				this.addDefinition(definition, this.exportersMap, null);
			else if (definition instanceof RoleDefinition)
				this.addDefinition(definition, this.rolesMap, this.rolesList);
			else if (definition instanceof DatastoreBuilderDefinition)
				this.addDefinition(definition, this.datastoreBuilderMap, this.datastoreBuilderList);

			if (definition instanceof IsInitiable)
				((IsInitiable) definition).init();

			if (definition instanceof FormDefinition) {
				FormDefinition formDefinition = (FormDefinition) definition;
				this.fieldsMap.putAll(formDefinition.getFields());
			}

			this.preDependencyMap.put(name.toLowerCase(), parentName != null ? parentName.toLowerCase() : null);

			logger.debug("Loaded definition with name='%s' and code='%s'", definition.getName(), definition.getCode());
		} catch (Throwable e) {
			logger.error(e);
		}
	}

	public void registerDashboardIndicatorFormulaClass(Class<?> clazz, String indicatorCode) {
		this.indicatorFormulaClassMap.put(indicatorCode, clazz);
	}

	public void registerDashboardTaxonomyClassifierClass(Class<?> clazz, String taxonomyCode) {
		this.taxonomyClassifierClassMap.put(taxonomyCode, clazz);
	}

	private void registerSelfGeneratedThesaurus(ThesaurusDefinition definition) {

        if (definition.isSelfGenerated() && definition.getSelfGenerated().getFromIndex() == null)
            return;

		IndexDefinition indexDefinition = this.getIndexDefinition(definition.getSelfGenerated().getFromIndex().getIndex().getValue());
		List<ThesaurusDefinition> indexThesaurus = this.thesaurusListGeneratedByIndex.get(indexDefinition.getCode());
		if (indexThesaurus == null) {
			indexThesaurus = new ArrayList<>();
			this.thesaurusListGeneratedByIndex.put(indexDefinition.getCode(), indexThesaurus);
		}

		indexThesaurus.add(definition);
	}

	public void registerLanguage(Class<? extends org.monet.metamodel.interfaces.Language> clazz, String localeCode) {
		org.monet.metamodel.interfaces.Language language;
		AgentLogger logger = AgentLogger.getInstance();
		try {
			language = clazz.newInstance();
			language.init(localeCode);
			this.addLanguage(localeCode, language);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public Set<String> getLanguages() {
		return this.languages.keySet();
	}

	private void checkLanguagesRegistered() {
		if (!this.languages.containsKey("es")) {
			org.monet.metamodel.interfaces.Language language = new org.monet.metamodel.interfaces.Language();
			language.init("es");
			this.addLanguage("es", language);
		}

		if (!this.languages.containsKey("en")) {
			org.monet.metamodel.interfaces.Language language = new org.monet.metamodel.interfaces.Language();
			language.init("en");
			this.addLanguage("en", language);
		}
	}

	private void addLanguage(String localeCode, org.monet.metamodel.interfaces.Language language) {
		if (localeCode.equals("default")) {
			if (this.defaultLanguage == null)
				this.defaultLanguage = language;
			else
				this.defaultLanguage.merge(language);
		} else {
			org.monet.metamodel.interfaces.Language currentInstance = this.languages.get(localeCode);
			if (currentInstance == null)
				this.languages.put(localeCode, language);
			else
				currentInstance.merge(language);
		}
	}

	public org.monet.metamodel.interfaces.Language getDefaultLanguage() {
		return this.defaultLanguage;
	}

	public org.monet.metamodel.interfaces.Language getLanguage(String localeCode) {
		return this.languages.get(localeCode);
	}

	public void initialize() {
		Configuration configuration = Configuration.getInstance();
		String classesDir = configuration.getBusinessModelClassesDir();
		PackageReader reader = new PackageReader(classesDir);
		ClassLoader classLoader = new BusinessModelClassLoader();

		try {
			this.clean();

			for (String classname : reader.read()) {
				Class.forName(classname, true, classLoader);
				// We only need to load the class
			}

			this.checkLanguagesRegistered();
			this.register(TaskOrderDefinition.class, TaskOrderDefinition.NAME, null);

			buildDependencyMap();
			prepareBuilders();
			initReplacers();
			createOntologiesMap();
			createSelfGeneratedThesaurusMap();
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

	}

	@SuppressWarnings("unchecked")
	private void initReplacers() {
		AgentLogger logger = AgentLogger.getInstance();

		for (Replacer replacer : this.replacersList) {
			try {
				Definition definition = this.getDefinition(replacer.Name);
				Definition replacedDefinition = this.getDefinition(replacer.ReplacedName);

				if (definition instanceof ContainerDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.containersMap);
				else if (definition instanceof DesktopDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.desktopsMap);
				else if (definition instanceof CollectionDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.collectionsMap);
				else if (definition instanceof FormDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.formsMap);
				else if (definition instanceof DocumentDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.documentsMap);
				else if (definition instanceof CatalogDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.catalogsMap);
				else if (definition instanceof TaskDefinition) {
					this.replaceDefinition(definition, replacedDefinition, this.taskMap, this.servicesMap);
					this.replaceDefinition(definition, replacedDefinition, this.jobsMap);
				} else if (definition instanceof IndexDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.indexMap);
				else if (definition instanceof ThesaurusDefinition) {
					this.replaceDefinition(definition, replacedDefinition, this.thesaurusMap);
					this.replaceDefinition(definition, replacedDefinition, this.sourceMap);
				} else if (definition instanceof GlossaryDefinition) {
					this.replaceDefinition(definition, replacedDefinition, this.glossaryMap);
					this.replaceDefinition(definition, replacedDefinition, this.sourceMap);
				} else if (definition instanceof KpiDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.kpiMap);
				else if (definition instanceof MeasureUnitDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.measureUnitMap);
				else if (definition instanceof DashboardDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.dashboardMap);
				else if (definition instanceof DatastoreDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.datastoreMap);
				else if (definition instanceof ImporterDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.importersMap);
				else if (definition instanceof ExporterDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.exportersMap);
				else if (definition instanceof RoleDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.rolesMap);
				else if (definition instanceof DatastoreBuilderDefinition)
					this.replaceDefinition(definition, replacedDefinition, this.datastoreBuilderMap);

			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	private List<String> getOntologiesMap(NodeDefinition definition) {
		List<String> ontologies = new ArrayList<>();

		if (definition.isContainer()) {
			ContainerDefinition containerDefinition = (ContainerDefinition) definition;
			for (Ref containDefinition : containerDefinition.getContain().getNode()) {
				List<String> childOntologies = this.getOntologiesMap(this.getNodeDefinition(containDefinition.getValue()));
				ontologies.addAll(childOntologies);
			}
		} else if (definition.isCollection()) {
			CollectionDefinition collectionDefinition = (CollectionDefinition) definition;
			for (Ref addDefinition : collectionDefinition.getAdd().getNode()) {
				List<String> childOntologies = this.getOntologiesMap(this.getNodeDefinition(addDefinition.getValue()));
				ontologies.addAll(childOntologies);
			}
		} else if (definition.isForm()) {
			FormDefinition formDefinition = (FormDefinition) definition;
			List<String> sourceList = formDefinition.getSources();

			for (String source : sourceList) {
				SourceDefinition sourceDefinition = this.getSourceDefinition(source);
				if (sourceDefinition instanceof GlossaryDefinition)
					ontologies.add(sourceDefinition.getOntology());
			}
		}

		return ontologies;
	}

	private void createOntologiesMap() {
		this.ontologiesMap.clear();
		for (NodeDefinition definition : this.nodesMap.values()) {
			if (definition.requirePartnerContext()) {
				List<String> ontologies = this.getOntologiesMap(definition);
				this.ontologiesMap.put(definition.getName(), ontologies);
				this.ontologiesMap.put(definition.getCode(), ontologies);
			}
		}
	}

	private void createSelfGeneratedThesaurusMap() {
		for (ThesaurusDefinition definition : this.thesaurusList)
			if (definition.isSelfGenerated())
				registerSelfGeneratedThesaurus(definition);
	}

	@SuppressWarnings("unchecked")
	private <T extends Definition> void replaceDefinition(Definition definition, Definition replacedDefinition, Map<String, T>... maps) {
		String code = replacedDefinition.getCode().toLowerCase();
		String name = replacedDefinition.getName().toLowerCase();

		replacedDefinition.setIsAbstract(true);

		for (Map<String, T> map : maps) {
			map.put(name, (T) definition);
			map.put(code, (T) definition);
		}
		this.definitionMap.put(name, definition);
		this.definitionMap.put(code, definition);
	}

	private void buildDependencyMap() {
		for (String definitionName : this.preDependencyMap.keySet()) {
			List<String> implementers = new ArrayList<>();
			this.dependencyMap.put(definitionName, implementers);

			Definition definition = this.definitionMap.get(definitionName);
			if (definition != null && !definition.isAbstract())
				implementers.add(definitionName);
			else if (definition == null)
				throw new RuntimeException(definitionName);
		}

		for (Entry<String, String> definitionEntry : this.preDependencyMap.entrySet()) {
			if (definitionEntry.getValue() != null) {
				addImplementerTo(definitionEntry.getValue(), definitionEntry.getKey());
			}
		}
		this.preDependencyMap.clear();
	}

	private void addImplementerTo(String definitionName, String implementerName) {
		this.dependencyMap.get(definitionName).add(implementerName);
		String parent = this.preDependencyMap.get(definitionName);
		if (parent != null)
			this.addImplementerTo(parent, implementerName);
	}

	public void clean() {
		this.nodesMap.clear();
		this.containersMap.clear();
		this.collectionsMap.clear();
		this.desktopsMap.clear();
		this.desktopsList.clear();
		this.formsMap.clear();
		this.documentsMap.clear();
		this.documentsList.clear();
		this.catalogsMap.clear();
		this.servicesMap.clear();
		this.jobsMap.clear();
		this.taskMap.clear();
		this.taskList.clear();
		this.indexMap.clear();
		this.indexList.clear();
		this.sourceMap.clear();
		this.sourceList.clear();
		this.thesaurusMap.clear();
		this.thesaurusList.clear();
		this.glossaryMap.clear();
		this.glossaryList.clear();
		this.measureUnitMap.clear();
		this.measureUnitList.clear();
		this.dashboardMap.clear();
		this.dashboardList.clear();
		this.indicatorFormulaClassMap.clear();
		this.taxonomyClassifierClassMap.clear();
		this.datastoreMap.clear();
		this.datastoreList.clear();
		this.dimensionMap.clear();
		this.cubeMap.clear();
		this.importersList.clear();
		this.importersMap.clear();
		this.exportersMap.clear();
		this.definitionMap.clear();
		this.definitionList.clear();
		this.environments.clear();
		this.rolesMap.clear();
		this.rolesList.clear();
		this.rolesNames.clear();
		this.fieldsMap.clear();
		this.datastoreBuilders.clear();
		this.datastoreBuilderMap.clear();
		this.datastoreBuilderList.clear();
		this.layoutDefinitionMap.clear();
		this.kpiMap.clear();

		this.languages.clear();
		this.replacersList.clear();
		this.defaultLanguage = null;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
		this.initialize();
	}

	public static Dictionary getInstance() {
		if (instance == null)
			instance = new Dictionary();
		return (Dictionary) instance;
	}

	public List<String> getOntologies(String definitionKey) {
		if (!this.ontologiesMap.containsKey(definitionKey))
			return null;
		return this.ontologiesMap.get(definitionKey);
	}

	public String basePackage() {
		if (basePackage != null) return basePackage;
		for (Definition d : getAllDefinitions()) {
			if (d instanceof TaskOrderDefinition) continue;
			String name = d.getName();
			if (name.contains(".")) name = name.substring(0, name.lastIndexOf("."));
			if (basePackage == null) basePackage = name;
			else basePackage = greatestCommonPrefix(basePackage, name);
		}
		return basePackage;
	}

	private String greatestCommonPrefix(String a, String b) {
		int minLength = Math.min(a.length(), b.length());
		for (int i = 0; i < minLength; i++) {
			if (a.charAt(i) != b.charAt(i)) {
				return a.substring(0, i);
			}
		}
		return a.substring(0, minLength);
	}

}