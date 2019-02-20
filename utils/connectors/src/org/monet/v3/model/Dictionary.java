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

package org.monet.v3.model;

import org.apache.commons.lang.NotImplementedException;
import org.monet.bpi.java.locator.PackageReader;
import org.monet.metamodel.*;
import org.monet.metamodel.interfaces.IsInitiable;
import org.monet.metamodel.interfaces.Language;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.metamodel.internal.LayoutDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.metamodel.internal.TaskOrderDefinition;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

public class Dictionary extends org.monet.metamodel.Dictionary {
	private HashMap<String, NodeDefinition> nodesMap;
	private HashMap<String, ContainerDefinition> containersMap;
	private ArrayList<ContainerDefinition> containersList;
	private HashMap<String, DesktopDefinition> desktopsMap;
	private ArrayList<DesktopDefinition> desktopsList;
	private HashMap<String, CollectionDefinition> collectionsMap;
	private ArrayList<CollectionDefinition> collectionsList;
	private HashMap<String, FormDefinition> formsMap;
	private ArrayList<FormDefinition> formsList;
	private HashMap<String, DocumentDefinition> documentsMap;
	private ArrayList<DocumentDefinition> documentsList;
	private HashMap<String, CatalogDefinition> catalogsMap;
	private ArrayList<CatalogDefinition> catalogsList;
	private HashMap<String, TaskDefinition> servicesMap;
	private HashMap<String, JobDefinition> jobsMap;
	private HashMap<String, TaskDefinition> taskMap;
	private ArrayList<TaskDefinition> taskList;
	private HashMap<String, IndexDefinition> indexMap;
	private ArrayList<IndexDefinition> indexList;
	private HashMap<String, SourceDefinition> sourceMap;
	private ArrayList<SourceDefinition> sourceList;
	private HashMap<String, ThesaurusDefinition> thesaurusMap;
	private ArrayList<ThesaurusDefinition> thesaurusList;
	private HashMap<String, GlossaryDefinition> glossaryMap;
	private ArrayList<GlossaryDefinition> glossaryList;
	private HashMap<String, MeasureUnitDefinition> measureUnitMap;
	private ArrayList<MeasureUnitDefinition> measureUnitList;
	private HashMap<String, KpiDefinition> kpiMap;
	private ArrayList<KpiDefinition> kpiList;
	private ArrayList<ImporterDefinition> importersList;
	private HashMap<String, ImporterDefinition> importersMap;
	private HashMap<String, ExporterDefinition> exportersMap;
	private HashMap<String, DashboardDefinition> dashboardMap;
	private ArrayList<DashboardDefinition> dashboardList;
	private HashMap<String, Class<?>> indicatorFormulaClassMap;
	private HashMap<String, Class<?>> taxonomyClassifierClassMap;
	private HashMap<String, DatastoreDefinition> datastoreMap;
	private ArrayList<DatastoreDefinition> datastoreList;
	private HashMap<String, DatastoreDefinition.DimensionProperty> dimensionMap;
	private HashMap<String, DatastoreDefinition.CubeProperty> cubeMap;
	private HashMap<String, Definition> definitionMap;
	private ArrayList<Definition> definitionList;
	private ArrayList<NodeDefinition> singletonDefinitionList;
	private HashMap<String, RoleDefinition> rolesMap;
	private ArrayList<RoleDefinition> rolesList;
	private ArrayList<String> rolesNames;
	private HashMap<String, FieldProperty> fieldsMap;
	private HashMap<String, DatastoreBuilderDefinition> datastoreBuilderMap;
	private ArrayList<DatastoreBuilderDefinition> datastoreBuilderList;
	private HashMap<String, LayoutDefinition> layoutDefinitionMap;

	private HashMap<String, ArrayList<DatastoreBuilderDefinition>> datastoreBuilders = new HashMap<String, ArrayList<DatastoreBuilderDefinition>>();
	private List<NodeDefinition> environments;
	private Language defaultLanguage;
	private HashMap<String, Language> languages = new HashMap<String, Language>();
	public static final String DICTIONARY = "Dictionary";

	private HashMap<String, String> preDependencyMap = new HashMap<String, String>();
	private HashMap<String, ArrayList<String>> dependencyMap = new HashMap<String, ArrayList<String>>();
	private HashMap<String, String> defineMap = new HashMap<String, String>();
	private ArrayList<Replacer> replacersList = new ArrayList<Replacer>();
	private HashMap<String, ArrayList<String>> ontologiesMap = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<ThesaurusDefinition>> selfGeneratedThesaurus = new HashMap<String, ArrayList<ThesaurusDefinition>>();

	public Dictionary() {
		super();
		this.nodesMap = new HashMap<String, NodeDefinition>();
		this.containersMap = new HashMap<String, ContainerDefinition>();
		this.containersList = new ArrayList<>();
		this.desktopsMap = new HashMap<String, DesktopDefinition>();
		this.desktopsList = new ArrayList<DesktopDefinition>();
		this.collectionsMap = new HashMap<String, CollectionDefinition>();
		this.collectionsList = new ArrayList<>();
		this.formsMap = new HashMap<String, FormDefinition>();
		this.formsList = new ArrayList<>();
		this.documentsMap = new HashMap<String, DocumentDefinition>();
		this.documentsList = new ArrayList<DocumentDefinition>();
		this.catalogsMap = new HashMap<String, CatalogDefinition>();
		this.catalogsList = new ArrayList<>();
		this.servicesMap = new HashMap<String, TaskDefinition>();
		this.jobsMap = new HashMap<String, JobDefinition>();
		this.taskMap = new HashMap<String, TaskDefinition>();
		this.taskList = new ArrayList<TaskDefinition>();
		this.indexMap = new HashMap<String, IndexDefinition>();
		this.indexList = new ArrayList<IndexDefinition>();
		this.sourceMap = new HashMap<String, SourceDefinition>();
		this.sourceList = new ArrayList<SourceDefinition>();
		this.thesaurusMap = new HashMap<String, ThesaurusDefinition>();
		this.thesaurusList = new ArrayList<ThesaurusDefinition>();
		this.glossaryMap = new HashMap<String, GlossaryDefinition>();
		this.glossaryList = new ArrayList<GlossaryDefinition>();
		this.measureUnitList = new ArrayList<MeasureUnitDefinition>();
		this.measureUnitMap = new HashMap<String, MeasureUnitDefinition>();
		this.dashboardList = new ArrayList<DashboardDefinition>();
		this.dashboardMap = new HashMap<String, DashboardDefinition>();
		this.indicatorFormulaClassMap = new HashMap<String, Class<?>>();
		this.taxonomyClassifierClassMap = new HashMap<String, Class<?>>();
		this.datastoreList = new ArrayList<DatastoreDefinition>();
		this.datastoreMap = new HashMap<String, DatastoreDefinition>();
		this.dimensionMap = new HashMap<String, DatastoreDefinitionBase.DimensionProperty>();
		this.cubeMap = new HashMap<String, DatastoreDefinitionBase.CubeProperty>();
		this.kpiList = new ArrayList<KpiDefinition>();
		this.kpiMap = new HashMap<String, KpiDefinition>();
		this.importersList = new ArrayList<ImporterDefinition>();
		this.importersMap = new HashMap<String, ImporterDefinition>();
		this.exportersMap = new HashMap<String, ExporterDefinition>();
		this.environments = new ArrayList<NodeDefinition>();
		this.definitionMap = new HashMap<String, Definition>();
		this.definitionList = new ArrayList<Definition>();
		this.singletonDefinitionList = new ArrayList<NodeDefinition>();
		this.rolesMap = new HashMap<String, RoleDefinition>();
		this.rolesList = new ArrayList<RoleDefinition>();
		this.rolesNames = new ArrayList<String>();
		this.fieldsMap = new HashMap<String, FieldProperty>();
		this.datastoreBuilderMap = new HashMap<String, DatastoreBuilderDefinition>();
		this.datastoreBuilderList = new ArrayList<DatastoreBuilderDefinition>();
		this.layoutDefinitionMap = new HashMap<String, LayoutDefinition>();
	}

	private <T> ArrayList<T> getDefinition(String key, HashMap<String, T> definitionMap) {
		ArrayList<T> definitions = new ArrayList<T>();

		Definition mainDefinition = this.definitionMap.get(key.toLowerCase());
		String mainDefinitionName = null;
		if (mainDefinition != null)
			mainDefinitionName = mainDefinition.getName().toLowerCase();
		ArrayList<String> implementers = this.dependencyMap.get(mainDefinitionName);
		if (implementers == null) {
			throw new RuntimeException("Business model definition not found");
		}
		if (implementers.size() == 0) {
			throw new RuntimeException("Business model definition implementation not found");
		}

		for (String implementerKey : implementers) {
			T definition = definitionMap.get(implementerKey.toLowerCase());
			definitions.add(definition);
		}

		return definitions;
	}

	public Collection<Definition> getAllDefinitions() {
		return this.definitionList;
	}

	public String getDefinitionCode(String key) {
		Definition definition = this.definitionMap.get(key.toLowerCase());
		if (definition == null)
			return null;
		return definition.getCode();
	}

	public NodeDefinition getNodeDefinition(String key) {
		return this.getDefinition(key, this.nodesMap).get(0);
	}

	public ArrayList<NodeDefinition> getAllImplementersOfNodeDefinition(String key) {
		return this.getDefinition(key, this.nodesMap);
	}

	public FormDefinition getFormDefinition(String key) {
		return this.getDefinition(key, this.formsMap).get(0);
	}

	public Boolean isFormDefinition(String key) {
		return this.formsMap.containsKey(key.toLowerCase());
	}

	public Boolean isCatalogDefinition(String key) {
		return this.catalogsMap.containsKey(key.toLowerCase());
	}

	public FieldProperty getFormFieldDefinition(String nodeKey, String fieldKey) {
		FormDefinition formDefinition = this.getFormDefinition(nodeKey);
		return formDefinition.getField(fieldKey);
	}

	public List<NodeDefinition> getEnvironmentDefinitionList() {
		return this.environments;
	}

	public HashMap<String, ArrayList<NodeDefinition>> getEnvironmentDefinitionListByRole() {
		HashMap<String, ArrayList<NodeDefinition>> environmentDefinitions = new HashMap<String, ArrayList<NodeDefinition>>();
		List<NodeDefinition> environmentDefinitionList = this.getEnvironmentDefinitionList();

		for (RoleDefinition roleDefinition : this.getRoleDefinitionList()) {
			String roleCode = roleDefinition.getCode();
			ArrayList<NodeDefinition> environmentDefinitionWithRole = new ArrayList<NodeDefinition>();

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

	public HashMap<String, ArrayList<DashboardDefinition>> getDashboardDefinitionListByRole() {
		HashMap<String, ArrayList<DashboardDefinition>> dashboardDefinitions = new HashMap<String, ArrayList<DashboardDefinition>>();
		List<DashboardDefinition> dashboardDefinitionList = this.getDashboardDefinitionList();

		for (RoleDefinition roleDefinition : this.getRoleDefinitionList()) {
			String roleCode = roleDefinition.getCode();
			ArrayList<DashboardDefinition> dashboardDefinitionWithRole = new ArrayList<DashboardDefinition>();

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

	public List<NodeDefinition> getSingletonDefinitionList() {
		return this.singletonDefinitionList;
	}

	public DesktopDefinition getDesktopDefinition(String key) {
		return this.getDefinition(key, this.desktopsMap).get(0);
	}

	public List<DesktopDefinition> getDesktopDefinitionList() {
		return this.desktopsList;
	}

	public List<ContainerDefinition> getContainerDefinitionList() {
		return this.containersList;
	}

	public List<CollectionDefinition> getCollectionDefinitionList() {
		return this.collectionsList;
	}

	public List<CatalogDefinition> getCatalogDefinitionList() {
		return this.catalogsList;
	}

	public List<FormDefinition> getFormDefinitionList() {
		return this.formsList;
	}

	public DocumentDefinition getDocumentDefinition(String key) {
		return this.getDefinition(key, this.documentsMap).get(0);
	}

	public List<DocumentDefinition> getDocumentDefinitionList() {
		return this.documentsList;
	}

	public CollectionDefinition getCollectionDefinition(String key) {
		return this.getDefinition(key, this.collectionsMap).get(0);
	}

	public CatalogDefinition getCatalogDefinition(String key) {
		return this.getDefinition(key, this.catalogsMap).get(0);
	}

	public TaskDefinition getTaskDefinitionByServiceName(String key) {
		return this.servicesMap.get(key);
	}

	public Collection<TaskDefinition> getTaskDefinitionsWithServiceName() {
		return this.servicesMap.values();
	}

	public Collection<JobDefinition> getJobDefinitions() {
		return this.jobsMap.values();
	}

	public TaskDefinition getTaskDefinition(String key) {
		return this.getDefinition(key, this.taskMap).get(0);
	}

	public IndexDefinition getIndexDefinition(String key) {
		if (key.equals(DescriptorDefinition.CODE)) {
			return new DescriptorDefinition();
		}
		return this.getDefinition(key, this.indexMap).get(0);
	}

	public Collection<IndexDefinition> getIndexDefinitionList() {
		return this.indexList;
	}

	public ArrayList<ThesaurusDefinition> getThesaurusGeneratedByIndex(IndexDefinition definition) {
		return this.selfGeneratedThesaurus.get(definition.getCode());
	}

	public List<ImporterDefinition> getImporterDefinitionList() {
		return this.importersList;
	}

	public ImporterDefinition getImporterDefinition(String key) {
		return this.getDefinition(key, this.importersMap).get(0);
	}

	public ExporterDefinition getExporterDefinition(String key) {
		return this.getDefinition(key, this.exportersMap).get(0);
	}

	public List<TaskDefinition> getTaskDefinitionList() {
		return this.taskList;
	}

	public List<ThesaurusDefinition> getThesaurusDefinitionList() {
		return this.thesaurusList;
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
		return this.glossaryList;
	}

	public Map<String, GlossaryDefinition> getGlossaryDefinitionMap() {
		return this.glossaryMap;
	}

	public List<MeasureUnitDefinition> getMeasureUnitDefinitionList() {
		return this.measureUnitList;
	}

	public Map<String, MeasureUnitDefinition> getMeasureUnitDefinitionMap() {
		return this.measureUnitMap;
	}

	public MeasureUnitDefinition getMeasureUnitDefinition(String key) {
		return this.getDefinition(key, this.measureUnitMap).get(0);
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
		throw new NotImplementedException();
	}

	public Class<?> getDashboardTaxonomyClassifierClass(DashboardDefinition definition, String taxonomyCode) {
		return this.taxonomyClassifierClassMap.get(taxonomyCode);
	}

	public InputStream getDashboardTaxonomyClassifierClassAsStream(DashboardDefinition definition, String taxonomyCode) {
		throw new NotImplementedException();
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

	public DatastoreDefinition.DimensionProperty getDimensionDefinition(String key) {

		if (this.dimensionMap.containsKey(key))
			return this.dimensionMap.get(key);

		return this.dimensionMap.get(key.toLowerCase());
	}

	public DatastoreDefinitionBase.CubeProperty getCubeDefinition(String key) {

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

	public ArrayList<RoleDefinition> getRoleDefinitionList() {
		return this.rolesList;
	}

	public List<String> getRolesNames() {
		return this.rolesNames;
	}

	public ArrayList<DatastoreBuilderDefinition> getDatastoreBuildersForNode(String nodeKey) {
		if (!this.datastoreBuilders.containsKey(nodeKey))
			return new ArrayList<DatastoreBuilderDefinition>();
		return this.datastoreBuilders.get(nodeKey);
	}

	public ArrayList<DatastoreBuilderDefinition> getDatastoreBuilders() {
		return this.datastoreBuilderList;
	}

	public DatastoreBuilderDefinition getDatastoreBuilder(String key) {
		return this.datastoreBuilderMap.get(key.toLowerCase());
	}

	public Definition getDefinition(String key) {
		return this.getDefinition(key, this.definitionMap).get(0);
	}

	public boolean existsDefinition(String key) {
		return this.definitionMap.containsKey(key.toLowerCase());
	}

	public boolean reset(Distribution distribution, Project project) {
		this.defineMap.clear();
		this.defineMap.putAll(project.getDefineMap());
		this.defineMap.putAll(distribution.getDefineMap());
		return true;
	}

	public String getVariable(String varName) {
		return this.defineMap.get(varName);
	}

	public LayoutDefinition getLayoutDefinition(String key) {
		throw new NotImplementedException();
	}

	@SuppressWarnings("unchecked")
	private <T extends Definition> void addDefinition(Definition definition, HashMap<String, T> definitionMap, ArrayList<T> definitionList) {
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

			boolean isEnvironment = (nodeDefinition instanceof DesktopDefinition || (nodeDefinition instanceof ContainerDefinition && ((ContainerDefinition) nodeDefinition).isEnvironment()));
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
			for (DatastoreDefinitionBase.DimensionProperty dimensionDefinition : ((DatastoreDefinition) definition).getDimensionList()) {
				this.dimensionMap.put(name + "." + dimensionDefinition.getName().toLowerCase(), dimensionDefinition);
				this.dimensionMap.put(dimensionDefinition.getCode(), dimensionDefinition);
			}

			for (DatastoreDefinitionBase.CubeProperty cubeDefinition : ((DatastoreDefinition) definition).getCubeList()) {
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
				ArrayList<DatastoreBuilderDefinition> builders = this.datastoreBuilders.get(nodeDefinition.getName());
				if (builders == null) {
					builders = new ArrayList<DatastoreBuilderDefinition>();
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

		try {
			definition = clazz.newInstance();
			if (definition instanceof ContainerDefinition)
				this.addDefinition(definition, this.containersMap, this.containersList);
			else if (definition instanceof DesktopDefinition)
				this.addDefinition(definition, this.desktopsMap, this.desktopsList);
			else if (definition instanceof CollectionDefinition)
				this.addDefinition(definition, this.collectionsMap, this.collectionsList);
			else if (definition instanceof FormDefinition)
				this.addDefinition(definition, this.formsMap, this.formsList);
			else if (definition instanceof DocumentDefinition)
				this.addDefinition(definition, this.documentsMap, this.documentsList);
			else if (definition instanceof CatalogDefinition)
				this.addDefinition(definition, this.catalogsMap, this.catalogsList);
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

		} catch (Throwable e) {
			e.printStackTrace();
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
		ArrayList<ThesaurusDefinition> indexThesaurus = this.selfGeneratedThesaurus.get(indexDefinition.getCode());
		if (indexThesaurus == null) {
			indexThesaurus = new ArrayList<ThesaurusDefinition>();
			this.selfGeneratedThesaurus.put(indexDefinition.getCode(), indexThesaurus);
		}

		indexThesaurus.add(definition);
	}

	public void registerLanguage(Class<? extends Language> clazz, String localeCode) {
		Language language;

		try {
			language = clazz.newInstance();
			language.init(localeCode);
			this.addLanguage(localeCode, language);
		} catch (Exception e) {
		}
	}

	public Set<String> getLanguages() {
		return this.languages.keySet();
	}

	private void checkLanguagesRegistered() {
		if (!this.languages.containsKey("es")) {
			Language language = new Language();
			language.init("es");
			this.addLanguage("es", language);
		}

		if (!this.languages.containsKey("en")) {
			Language language = new Language();
			language.init("en");
			this.addLanguage("en", language);
		}
	}

	private void addLanguage(String localeCode, Language language) {
		if (localeCode.equals("default")) {
			if (this.defaultLanguage == null)
				this.defaultLanguage = language;
			else
				this.defaultLanguage.merge(language);
		} else {
			Language currentInstance = this.languages.get(localeCode);
			if (currentInstance == null)
				this.languages.put(localeCode, language);
			else
				currentInstance.merge(language);
		}
	}

	public Language getDefaultLanguage() {
		return this.defaultLanguage;
	}

	public Language getLanguage(String localeCode) {
		return this.languages.get(localeCode);
	}

	public void initialize(String businessModelDir) {
		String classesDir = businessModelDir + "/classes";
		PackageReader reader = new PackageReader(classesDir);

		try {
			this.clean();

			for (String classname : reader.read()) {
				try {
				    Class.forName(classname/*, true, classLoader*/);
					// We only need to load the class
				}
				catch (Exception e) {
					System.out.println(String.format("Could not register definition class in dictionary: %s", classname));
					throw new RuntimeException(String.format("Could not register definition class in dictionary: %s", classname));
				}

			}

			this.checkLanguagesRegistered();
			this.register(TaskOrderDefinition.class, TaskOrderDefinition.NAME, null);

			buildDependencyMap();
			prepareBuilders();
			initReplacers();
			createOntologiesMap();
			createSelfGeneratedThesaurusMap();
		} catch (Exception e) {
		}

	}

	@SuppressWarnings("unchecked")
	private void initReplacers() {

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
			}
		}
	}

	private ArrayList<String> getOntologiesMap(NodeDefinition definition) {
		ArrayList<String> ontologies = new ArrayList<String>();

		if (definition instanceof ContainerDefinition) {
			ContainerDefinition containerDefinition = (ContainerDefinition) definition;
			for (Ref containDefinition : containerDefinition.getContain().getNode()) {
				ArrayList<String> childOntologies = this.getOntologiesMap(this.getNodeDefinition(containDefinition.getValue()));
				ontologies.addAll(childOntologies);
			}
		} else if (definition instanceof CollectionDefinition) {
			CollectionDefinition collectionDefinition = (CollectionDefinition) definition;
			for (Ref addDefinition : collectionDefinition.getAdd().getNode()) {
				ArrayList<String> childOntologies = this.getOntologiesMap(this.getNodeDefinition(addDefinition.getValue()));
				ontologies.addAll(childOntologies);
			}
		} else if (definition instanceof FormDefinition) {
			FormDefinition formDefinition = (FormDefinition) definition;
			ArrayList<String> sourceList = formDefinition.getSources();

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
				ArrayList<String> ontologies = this.getOntologiesMap(definition);
				this.ontologiesMap.put(definition.getName(), ontologies);
				this.ontologiesMap.put(definition.getCode(), ontologies);
			}
		}
	}

	private void createSelfGeneratedThesaurusMap() {
		for (ThesaurusDefinition definition : this.thesaurusList)
			if (((ThesaurusDefinition) definition).isSelfGenerated())
				registerSelfGeneratedThesaurus((ThesaurusDefinition) definition);
	}

	@SuppressWarnings("unchecked")
	private <T extends Definition> void replaceDefinition(Definition definition, Definition replacedDefinition, HashMap<String, T>... maps) {
		String code = replacedDefinition.getCode().toLowerCase();
		String name = replacedDefinition.getName().toLowerCase();

		replacedDefinition.setIsAbstract(true);

		for (HashMap<String, T> map : maps) {
			map.put(name, (T) definition);
			map.put(code, (T) definition);
		}
		this.definitionMap.put(name, definition);
		this.definitionMap.put(code, definition);
	}

	private void buildDependencyMap() {
		for (String definitionName : this.preDependencyMap.keySet()) {
			ArrayList<String> implementers = new ArrayList<String>();
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
		this.containersList.clear();
		this.collectionsMap.clear();
		this.collectionsList.clear();
		this.desktopsMap.clear();
		this.desktopsList.clear();
		this.formsMap.clear();
		this.formsList.clear();
		this.documentsMap.clear();
		this.documentsList.clear();
		this.catalogsMap.clear();
		this.catalogsList.clear();
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

	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
	}

	public ArrayList<String> getOntologies(String definitionKey) {
		if (!this.ontologiesMap.containsKey(definitionKey))
			return null;
		return this.ontologiesMap.get(definitionKey);
	}

}