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

package org.monet.v2.model;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.monet.v2.metamodel.*;
import org.monet.v2.metamodel.Package;
import org.simpleframework.xml.core.Persister;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Dictionary extends BaseObject implements ILoadListener {
	private HashMap<String, NodeDefinition> nodesMap;
	private HashMap<String, ContainerDefinition> containersMap;
	private HashMap<String, DesktopDefinition> desktopsMap;
	private HashMap<String, CollectionDefinition> collectionsMap;
	private HashMap<String, FormDefinition> formsMap;
	private HashMap<String, DocumentDefinition> documentMap;
	private ArrayList<DocumentDefinition> documentList;
	private HashMap<String, CatalogDefinition> catalogsMap;
	private HashMap<String, MapDefinition> mapMap;
	private ArrayList<MapDefinition> mapList;
	private HashMap<String, ServiceDefinition> servicesMap;
	private ArrayList<ServiceDefinition> servicesList;
	private HashMap<String, ServiceProviderDefinition> servicesProviderMap;
	private ArrayList<ServiceProviderDefinition> servicesProviderList;
	private HashMap<String, MapProviderDefinition> mapProviderMap;
	private ArrayList<MapProviderDefinition> mapProviderList;
	private HashMap<String, ThesaurusProviderDefinition> thesaurusProviderMap;

	private ArrayList<ThesaurusProviderDefinition> thesaurusProviderList;
	private HashMap<String, CubeProviderDefinition> cubeProviderMap;
	private ArrayList<CubeProviderDefinition> cubeProviderList;
	private HashMap<String, TaskDefinition> taskMap;
	private ArrayList<TaskDefinition> taskList;
	private HashMap<String, ReferenceDefinition> referenceMap;
	private ArrayList<ReferenceDefinition> referenceList;
	private HashMap<String, ThesaurusDefinition> thesaurusMap;
	private ArrayList<ThesaurusDefinition> thesaurusList;
	private HashMap<String, CubeDefinition> cubesMap;
	private ArrayList<CubeDefinition> cubeList;
	private ArrayList<ImporterDefinition> importersList;
	private HashMap<String, ImporterDefinition> importersMap;
	private HashMap<String, ExporterDefinition> exportersMap;
	private HashMap<String, Definition> definitionMap;
	private ArrayList<Definition> definitionList;
	private ArrayList<NodeDefinition> singletonDefinitionList;
	private HashMap<String, RoleDefinition> rolesMap;
	private ArrayList<String> rolesNames;

	private List<NodeDefinition> environments;

	public static final String DICTIONARY = "Dictionary";
	private String sourceDirectory;

	public Dictionary() {
		super();
		this.linkLoadListener(this);
		this.nodesMap = new HashMap<String, NodeDefinition>();
		this.containersMap = new HashMap<String, ContainerDefinition>();
		this.desktopsMap = new HashMap<String, DesktopDefinition>();
		this.collectionsMap = new HashMap<String, CollectionDefinition>();
		this.formsMap = new HashMap<String, FormDefinition>();
		this.documentMap = new HashMap<String, DocumentDefinition>();
		this.documentList = new ArrayList<DocumentDefinition>();
		this.catalogsMap = new HashMap<String, CatalogDefinition>();
		this.mapMap = new HashMap<String, MapDefinition>();
		this.mapList = new ArrayList<MapDefinition>();
		this.servicesMap = new HashMap<String, ServiceDefinition>();
		this.servicesList = new ArrayList<ServiceDefinition>();
		this.servicesProviderMap = new HashMap<String, ServiceProviderDefinition>();
		this.servicesProviderList = new ArrayList<ServiceProviderDefinition>();
		this.mapProviderMap = new HashMap<String, MapProviderDefinition>();
		this.mapProviderList = new ArrayList<MapProviderDefinition>();
		this.thesaurusProviderMap = new HashMap<String, ThesaurusProviderDefinition>();
		this.thesaurusProviderList = new ArrayList<ThesaurusProviderDefinition>();
		this.cubeProviderMap = new HashMap<String, CubeProviderDefinition>();
		this.cubeProviderList = new ArrayList<CubeProviderDefinition>();
		this.taskMap = new HashMap<String, TaskDefinition>();
		this.taskList = new ArrayList<TaskDefinition>();
		this.referenceMap = new HashMap<String, ReferenceDefinition>();
		this.referenceList = new ArrayList<ReferenceDefinition>();
		this.thesaurusMap = new HashMap<String, ThesaurusDefinition>();
		this.thesaurusList = new ArrayList<ThesaurusDefinition>();
		this.cubeList = new ArrayList<CubeDefinition>();
		this.cubesMap = new HashMap<String, CubeDefinition>();
		this.importersList = new ArrayList<ImporterDefinition>();
		this.importersMap = new HashMap<String, ImporterDefinition>();
		this.exportersMap = new HashMap<String, ExporterDefinition>();
		this.environments = new ArrayList<NodeDefinition>();
		this.definitionMap = new HashMap<String, Definition>();
		this.definitionList = new ArrayList<Definition>();
		this.singletonDefinitionList = new ArrayList<NodeDefinition>();
		this.rolesMap = new HashMap<String, RoleDefinition>();
		this.rolesNames = new ArrayList<String>();
	}

	public Boolean loadDefinitionList(String sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
		String dictionaryFilename = this.sourceDirectory + "/definitions/dictionary.xml";

		File file = new File(dictionaryFilename);

		if (!file.exists()) {
			return false;
		}

		this.deserializeFromXML(dictionaryFilename);

		return true;
	}

	private <T> T getDefinition(String key, HashMap<String, T> definitionMap) {
		T definition;

		onLoad(this, Dictionary.DICTIONARY);
		definition = definitionMap.get(key);

		if (definition == null) {
			return null;
		}

		return definition;
	}

	public Collection<Definition> getAllDefinitions() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.definitionList;
	}

	public String getDefinitionCode(String key) {
		Definition definition = this.getDefinition(key);
		if (definition == null)
			return null;
		return definition.getCode();
	}

	public NodeDefinition getNodeDefinition(String key) {
		return this.getDefinition(key, this.nodesMap);
	}

	public FormDefinition getFormDefinition(String key) {
		return this.getDefinition(key, this.formsMap);
	}

	public Boolean isFormDefinition(String key) {
		onLoad(this, Dictionary.DICTIONARY);
		return this.formsMap.containsKey(key);
	}

	public Boolean isCatalogDefinition(String key) {
		onLoad(this, Dictionary.DICTIONARY);
		return this.catalogsMap.containsKey(key);
	}

	public FieldDeclaration getFormFieldDefinition(String nodeKey, String fieldKey) {
		FormDefinition formDefinition = this.getFormDefinition(nodeKey);
		return formDefinition.getFieldDeclaration(fieldKey);
	}

	public List<NodeDefinition> getEnvironmentDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.environments;
	}

	public List<NodeDefinition> getSingletonDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.singletonDefinitionList;
	}

	public DocumentDefinition getDocumentDefinition(String key) {
		return this.getDefinition(key, this.documentMap);
	}

	public List<DocumentDefinition> getDocumentDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.documentList;
	}

	public CollectionDefinition getCollectionDefinition(String key) {
		return this.getDefinition(key, this.collectionsMap);
	}

	public CatalogDefinition getCatalogDefinition(String key) {
		return this.getDefinition(key, this.catalogsMap);
	}

	public ProviderDefinition getProviderDefinition(String code) {
		ProviderDefinition providerDefinition = null;
		providerDefinition = this.servicesProviderMap.get(code);
		if (providerDefinition == null)
			providerDefinition = this.thesaurusProviderMap.get(code);
		if (providerDefinition == null)
			providerDefinition = this.mapProviderMap.get(code);
		if (providerDefinition == null)
			providerDefinition = this.cubeProviderMap.get(code);
		return providerDefinition;
	}

	public List<MapDefinition> getMapServiceDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.mapList;
	}

	public ServiceDefinition getServiceDefinition(String key) {
		return this.getDefinition(key, this.servicesMap);
	}

	public List<ServiceDefinition> getServiceDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.servicesList;
	}

	public List<ServiceProviderDefinition> getServiceProviderDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.servicesProviderList;
	}

	public ServiceProviderDefinition getServiceProviderDefinition(String key) {
		return this.getDefinition(key, this.servicesProviderMap);
	}

	public List<MapProviderDefinition> getMapProviderDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.mapProviderList;
	}

	public MapProviderDefinition getMapProviderDefinition(String key) {
		return this.getDefinition(key, this.mapProviderMap);
	}

	public List<CubeProviderDefinition> getCubeProviderDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.cubeProviderList;
	}

	public CubeProviderDefinition getCubeProviderDefinition(String key) {
		return this.getDefinition(key, this.cubeProviderMap);
	}

	public List<ThesaurusProviderDefinition> getThesaurusProviderDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.thesaurusProviderList;
	}

	public ThesaurusProviderDefinition getThesaurusProviderDefinition(String key) {
		return this.getDefinition(key, this.thesaurusProviderMap);
	}

	public TaskDefinition getTaskDefinition(String key) {
		return this.getDefinition(key, this.taskMap);
	}

	public ReferenceDefinition getReferenceDefinition(String key) {
		return this.getDefinition(key, this.referenceMap);
	}

	public Collection<ReferenceDefinition> getReferenceDefinitionList() {
		return this.referenceList;
	}

	public List<ImporterDefinition> getImporterDefinitionList() {
		return this.importersList;
	}

	public ImporterDefinition getImporterDefinition(String key) {
		return this.importersMap.get(key);
	}

	public ExporterDefinition getExporterDefinition(String key) {
		return this.exportersMap.get(key);
	}

	public List<TaskDefinition> getTaskDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.taskList;
	}

	public List<ThesaurusDefinition> getThesaurusDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.thesaurusList;
	}

	public Map<String, ThesaurusDefinition> getThesaurusDefinitionMap() {
		return this.thesaurusMap;
	}

	public List<CubeDefinition> getCubeDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.cubeList;
	}

	public CubeDefinition getCubeDefinition(String key) {
		onLoad(this, Dictionary.DICTIONARY);
		return this.cubesMap.get(key);
	}

	public MapDefinition getMapDefinition(String key) {
		return this.getDefinition(key, this.mapMap);
	}

	public List<MapDefinition> getMapDefinitionList() {
		onLoad(this, Dictionary.DICTIONARY);
		return this.mapList;
	}

	public ThesaurusDefinition getThesaurusDefinition(String key) {
		return this.getDefinition(key, this.thesaurusMap);
	}

	public RoleDefinition getRoleDefinition(String key) {
		return this.getDefinition(key, this.rolesMap);
	}

	public List<String> getRolesNames() {
		return this.rolesNames;
	}

	public Definition getDefinition(String key) {
		return this.getDefinition(key, this.definitionMap);
	}

	public boolean existsDefinition(String key) {
		onLoad(this, Dictionary.DICTIONARY);
		return this.definitionMap.containsKey(key);
	}

	public boolean reset() {
		this.removeLoadedAttribute(Dictionary.DICTIONARY);
		onLoad(this, Dictionary.DICTIONARY);
		return true;
	}

	private <T> T deserializeDefinition(Class<T> clazz, String filename) {
		Persister persister = new Persister();
		FileInputStream inputStream = null;

		String absoluteFilename = this.sourceDirectory + "/definitions/" + filename;
		File absoluteFile = new File(absoluteFilename);
		if (!absoluteFile.exists()) {
			return null;
		}
		try {
			inputStream = new FileInputStream(absoluteFilename);
			return persister.read(clazz, inputStream);
		} catch (Exception e) {
			return null;
		} finally {
			StreamHelper.close(inputStream);
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends Definition> void addDefinition(String filename, Class<T> clazz, HashMap<String, T> definitionMap, ArrayList<T> definitionList) {
		Package _package = this.deserializeDefinition(Package.class, filename);
		if (_package == null) return;
		T definition = (T) _package.getDefinition();
		definition.setFileName(filename);
		String code = definition.getCode();
		String name = definition.getName();
		definitionMap.put(code, definition);
		definitionMap.put(name, definition);

		if (definition instanceof NodeDefinition) {
			NodeDefinition nodeDefinition = (NodeDefinition) definition;
			this.nodesMap.put(code, nodeDefinition);
			this.nodesMap.put(name, nodeDefinition);
			if (nodeDefinition.isEnvironment() && !nodeDefinition.isAbstract())
				this.environments.add(nodeDefinition);
			if (nodeDefinition.isSingleton() && !nodeDefinition.isAbstract())
				this.singletonDefinitionList.add(nodeDefinition);
		}

		if (definition instanceof RoleDefinition)
			this.rolesNames.add(name);

		if (definitionList != null)
			definitionList.add(definition);

		this.definitionMap.put(code, definition);
		this.definitionMap.put(name, definition);

		this.definitionList.add(definition);
	}

	@SuppressWarnings("unchecked")
	public void deserializeFromXML(String filename) {
		SAXBuilder saxBuilder = new SAXBuilder();
		org.jdom.Document document;
		Element node, root;
		List<Element> entries;

		this.nodesMap.clear();
		this.containersMap.clear();
		this.collectionsMap.clear();
		this.desktopsMap.clear();
		this.formsMap.clear();
		this.documentMap.clear();
		this.documentList.clear();
		this.catalogsMap.clear();
		this.mapMap.clear();
		this.mapList.clear();
		this.servicesMap.clear();
		this.servicesList.clear();
		this.servicesProviderMap.clear();
		this.servicesProviderList.clear();
		this.mapProviderMap.clear();
		this.mapProviderList.clear();
		this.thesaurusProviderMap.clear();
		this.thesaurusProviderList.clear();
		this.cubeProviderMap.clear();
		this.cubeProviderList.clear();
		this.taskMap.clear();
		this.taskList.clear();
		this.referenceMap.clear();
		this.referenceList.clear();
		this.thesaurusMap.clear();
		this.thesaurusList.clear();
		this.cubesMap.clear();
		this.cubeList.clear();
		this.importersList.clear();
		this.importersMap.clear();
		this.exportersMap.clear();
		this.definitionMap.clear();
		this.definitionList.clear();
		this.environments.clear();
		this.rolesMap.clear();
		this.rolesNames.clear();

		File dictionaryFile = new File(filename);
		if (!dictionaryFile.exists())
			return;

		try {
			FileInputStream dictionaryStream = new FileInputStream(dictionaryFile);
			document = saxBuilder.build(dictionaryStream);
			root = document.getRootElement();

			node = root.getChild("definitionlist");

			if (node == null) return;

			entries = node.getChildren("definition");
			for (Element element : entries) {
				String file = element.getAttributeValue("file");
				String typeValue = element.getAttributeValue("type").replace('-', '_');
				DefinitionType type = typeValue != null ? DefinitionType.valueOf(typeValue) : null;

				if (type != null) {
					switch (type) {
						case container:
							this.addDefinition(file, ContainerDefinition.class, this.containersMap, null);
							break;
						case desktop:
							this.addDefinition(file, DesktopDefinition.class, this.desktopsMap, null);
							break;
						case collection:
							this.addDefinition(file, CollectionDefinition.class, this.collectionsMap, null);
							break;
						case form:
							this.addDefinition(file, FormDefinition.class, this.formsMap, null);
							break;
						case document:
							this.addDefinition(file, DocumentDefinition.class, this.documentMap, this.documentList);
							break;
						case catalog:
							this.addDefinition(file, CatalogDefinition.class, this.catalogsMap, null);
							break;
						case service:
							this.addDefinition(file, ServiceDefinition.class, this.servicesMap, this.servicesList);
							break;
						case service_provider:
							this.addDefinition(file, ServiceProviderDefinition.class, this.servicesProviderMap, this.servicesProviderList);
							break;
						case map:
							this.addDefinition(file, MapDefinition.class, this.mapMap, this.mapList);
							break;
						case map_provider:
							this.addDefinition(file, MapProviderDefinition.class, this.mapProviderMap, this.mapProviderList);
							break;
						case thesaurus_provider:
							this.addDefinition(file, ThesaurusProviderDefinition.class, this.thesaurusProviderMap, this.thesaurusProviderList);
							break;
						case cube_provider:
							this.addDefinition(file, CubeProviderDefinition.class, this.cubeProviderMap, this.cubeProviderList);
							break;
						case task:
							this.addDefinition(file, TaskDefinition.class, this.taskMap, this.taskList);
							break;
						case reference:
							this.addDefinition(file, ReferenceDefinition.class, this.referenceMap, this.referenceList);
							break;
						case thesaurus:
							this.addDefinition(file, ThesaurusDefinition.class, this.thesaurusMap, this.thesaurusList);
							break;
						case cube:
							this.addDefinition(file, CubeDefinition.class, this.cubesMap, this.cubeList);
							break;
						case importer:
							this.addDefinition(file, ImporterDefinition.class, this.importersMap, this.importersList);
							break;
						case exporter:
							this.addDefinition(file, ExporterDefinition.class, this.exportersMap, null);
							break;
						case role:
							this.addDefinition(file, RoleDefinition.class, this.rolesMap, null);
							break;
					}
				}
			}
		} catch (Exception exception) {
			System.out.print(exception);
		}
	}

	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {

	}

	@Override
	public void loadAttribute(EventObject oEventObject, String sAttribute) {

	}
}