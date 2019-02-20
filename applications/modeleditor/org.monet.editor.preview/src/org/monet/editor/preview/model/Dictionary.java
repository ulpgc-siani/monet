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

package org.monet.editor.preview.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.monet.editor.preview.utils.PackageReader;
import org.monet.metamodel.CatalogDefinition;
import org.monet.metamodel.CollectionDefinition;
import org.monet.metamodel.ContainerDefinition;
import org.monet.metamodel.CubeDefinition;
import org.monet.metamodel.Definition;
import org.monet.metamodel.DesktopDefinition;
import org.monet.metamodel.DocumentDefinition;
import org.monet.metamodel.ExporterDefinition;
import org.monet.metamodel.FactTransformerDefinition;
import org.monet.metamodel.FormDefinition;
import org.monet.metamodel.ImporterDefinition;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.RoleDefinition;
import org.monet.metamodel.ServiceDefinition;
import org.monet.metamodel.ServiceProviderDefinition;
import org.monet.metamodel.TaskDefinition;
import org.monet.metamodel.ThesaurusDefinition;
import org.monet.metamodel.ThesaurusProviderDefinition;
import org.monet.metamodel.interfaces.IsInitiable;
import org.monet.metamodel.internal.DescriptorDefinition;

public class Dictionary extends org.monet.metamodel.Dictionary {
  private HashMap<String, Definition> definitionMap;
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
  private HashMap<String, CubeDefinition> cubesMap;
  private ArrayList<CubeDefinition> cubesList;
  private HashMap<String, ServiceDefinition> servicesMap;
  private ArrayList<ServiceDefinition> servicesList;
  private ArrayList<ServiceProviderDefinition> servicesProviderList;
  private ArrayList<ThesaurusProviderDefinition> thesaurusProviderList;
  private HashMap<String, ThesaurusProviderDefinition> thesaurusProviderMap;
  private HashMap<String, ServiceProviderDefinition> servicesProviderMap;
  private ArrayList<TaskDefinition> taskList;
  private HashMap<String, TaskDefinition> taskMap;
  private HashMap<String, IndexDefinition> indexMap;
  private ArrayList<IndexDefinition> indexList;
  private ArrayList<ThesaurusDefinition> thesaurusList;
  private HashMap<String, ThesaurusDefinition> thesaurusMap;
  private ArrayList<ImporterDefinition> importersList;
  private HashMap<String, ImporterDefinition> importersMap;
  private HashMap<String, ExporterDefinition> exportersMap;
  private ArrayList<RoleDefinition> rolesList;
  private HashMap<String, RoleDefinition> rolesMap;
  private HashMap<String, FactTransformerDefinition> transformersMap;
  private ArrayList<FactTransformerDefinition> transformersList;
  private ArrayList<Problem> problems;

  private ArrayList<NodeDefinition> singletonsList;
  private ArrayList<NodeDefinition> environmentsList;
  private HashMap<String, String> preDependencyMap = new HashMap<String, String>();
  private HashMap<String, ArrayList<String>> dependencyMap = new HashMap<String, ArrayList<String>>();

  private org.monet.metamodel.interfaces.Language defaultLanguage;
  private HashMap<String, org.monet.metamodel.interfaces.Language> languages = new HashMap<String, org.monet.metamodel.interfaces.Language>();

  private Dictionary() {
    super();
    this.definitionMap = new HashMap<String, Definition>();
    this.nodesMap = new HashMap<String, NodeDefinition>();
    this.containersMap = new HashMap<String, ContainerDefinition>();
    this.containersList = new ArrayList<ContainerDefinition>();
    this.desktopsMap = new HashMap<String, DesktopDefinition>();
    this.desktopsList = new ArrayList<DesktopDefinition>();
    this.collectionsMap = new HashMap<String, CollectionDefinition>();
    this.collectionsList = new ArrayList<CollectionDefinition>();
    this.formsMap = new HashMap<String, FormDefinition>();
    this.formsList = new ArrayList<FormDefinition>();
    this.documentsMap = new HashMap<String, DocumentDefinition>();
    this.documentsList = new ArrayList<DocumentDefinition>();
    this.catalogsMap = new HashMap<String, CatalogDefinition>();
    this.catalogsList = new ArrayList<CatalogDefinition>();
    this.cubesMap = new HashMap<String, CubeDefinition>();
    this.cubesList = new ArrayList<CubeDefinition>();
    this.servicesMap = new HashMap<String, ServiceDefinition>();
    this.servicesList = new ArrayList<ServiceDefinition>();
    this.servicesProviderList = new ArrayList<ServiceProviderDefinition>();
    this.thesaurusProviderMap = new HashMap<String, ThesaurusProviderDefinition>();
    this.servicesProviderMap = new HashMap<String, ServiceProviderDefinition>();
    this.taskMap = new HashMap<String, TaskDefinition>();
    this.taskList = new ArrayList<TaskDefinition>();
    this.indexMap = new HashMap<String, IndexDefinition>();
    this.indexList = new ArrayList<IndexDefinition>();
    this.thesaurusMap = new HashMap<String, ThesaurusDefinition>();
    this.thesaurusList = new ArrayList<ThesaurusDefinition>();
    this.importersMap = new HashMap<String, ImporterDefinition>();
    this.importersList = new ArrayList<ImporterDefinition>();
    this.exportersMap = new HashMap<String, ExporterDefinition>();
    this.rolesMap = new HashMap<String, RoleDefinition>();
    this.rolesList = new ArrayList<RoleDefinition>();
    this.transformersMap = new HashMap<String, FactTransformerDefinition>();
    this.transformersList = new ArrayList<FactTransformerDefinition>();
    this.setProblems(new ArrayList<Problem>());

    this.singletonsList = new ArrayList<NodeDefinition>();
    this.environmentsList = new ArrayList<NodeDefinition>();
  }

  public static Dictionary getInstance() {
    if (instance == null)
      instance = new Dictionary();
    return (Dictionary) instance;
  }

  @SuppressWarnings("unchecked")
  private <T extends Definition> void addDefinition(Definition definition, HashMap<String, T> definitionMap, ArrayList<T> definitionList) {
    String code = definition.getCode();
    String name = definition.getName();
    
    if (definitionMap.containsKey(definition.getName()))
      return;
    
    definitionMap.put(code, (T) definition);
    definitionMap.put(name, (T) definition);

    if (definition instanceof NodeDefinition) {
      NodeDefinition nodeDefinition = (NodeDefinition) definition;

      boolean isEnvironment = (nodeDefinition.isDesktop() || (nodeDefinition.isContainer() && ((ContainerDefinition) nodeDefinition).getEnvironment() != null));
      if (isEnvironment && !nodeDefinition.isAbstract())
        this.environmentsList.add(nodeDefinition);
      
      if (nodeDefinition.isSingleton() && !nodeDefinition.isAbstract())
        this.singletonsList.add(nodeDefinition);

      this.nodesMap.put(code, nodeDefinition);
      this.nodesMap.put(name, nodeDefinition);
    }

    if (definitionList != null)
      definitionList.add((T) definition);

    this.definitionMap.put(code, definition);
    this.definitionMap.put(name, definition);
  }

  private void buildDependencyMap() {
    for (String definitionName : this.preDependencyMap.keySet()) {
      ArrayList<String> implementers = new ArrayList<String>();
      this.dependencyMap.put(definitionName, implementers);
      if (!this.definitionMap.get(definitionName).isAbstract())
        implementers.add(definitionName);
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

  public void register(Class<? extends Definition> clazz, String name, String parentName) {
    Definition definition;

    try {
      definition = clazz.newInstance();
      if (ContainerDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.containersMap, this.containersList);
      else if (DesktopDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.desktopsMap, this.desktopsList);
      else if (CollectionDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.collectionsMap, this.collectionsList);
      else if (FormDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.formsMap, this.formsList);
      else if (DocumentDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.documentsMap, this.documentsList);
      else if (CatalogDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.catalogsMap, this.catalogsList);
      else if (ServiceDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.servicesMap, this.servicesList);
      else if (ServiceProviderDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.servicesProviderMap, this.servicesProviderList);
      else if (ThesaurusProviderDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.thesaurusProviderMap, this.thesaurusProviderList);
      //else if (TaskDefinition.class.isAssignableFrom(clazz)) // TODO. DECOMENTAR CUANDO ESTÉ EL NUEVO METAMODELO
        //this.addDefinition(definition, this.taskMap, this.taskList);
      else if (IndexDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.indexMap, this.indexList);
      else if (ThesaurusDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.thesaurusMap, this.thesaurusList);
      else if (CubeDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.cubesMap, this.cubesList);
      else if (ImporterDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.importersMap, this.importersList);
      else if (ExporterDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.exportersMap, null);
      else if (RoleDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.rolesMap, this.rolesList);
      else if (FactTransformerDefinition.class.isAssignableFrom(clazz))
        this.addDefinition(definition, this.transformersMap, this.transformersList);

      this.addDefinition(definition, this.definitionMap, null);

      if (definition instanceof IsInitiable)
        ((IsInitiable) definition).init();

      this.preDependencyMap.put(name, parentName);

    } catch (Exception e) {
      this.getProblems().add(new Problem("Error loading definition class", e.getClass().toString(), ExceptionUtils.getStackTrace(e), Problem.SEVERITY_ERROR));
    }
  }

  public void initialize(String modelDir) throws IOException, ClassNotFoundException {
    String classesDir = modelDir + "/classes";
    DictionaryClassLoader classLoader = DictionaryClassLoader.getInstance();
    PackageReader reader = new PackageReader(classesDir);

    classLoader.setPathBase(classesDir);

    this.getProblems().clear();
    this.clean();
    for (String classname : reader.read()) {
      Class.forName(classname, true, classLoader);
      // We only need to load the class
    }
    
    buildDependencyMap();
    
    for (Entry<String, ArrayList<String>> entry : this.dependencyMap.entrySet()) {
      if (entry.getValue().size() == 0) 
        this.problems.add(new Problem(String.format("Abstract definition '%s' has no implementers", entry.getKey()), null, null, Problem.SEVERITY_ERROR));
    }
  }

  public void clean() {
    this.definitionMap.clear();
    this.nodesMap.clear();
    this.containersMap.clear();
    this.containersList.clear();
    this.desktopsMap.clear();
    this.desktopsList.clear();
    this.collectionsMap.clear();
    this.collectionsList.clear();
    this.formsMap.clear();
    this.formsList.clear();
    this.documentsMap.clear();
    this.documentsList.clear();
    this.catalogsMap.clear();
    this.catalogsList.clear();
    this.cubesMap.clear();
    this.cubesList.clear();
    this.servicesMap.clear();
    this.servicesList.clear();
    this.servicesProviderList.clear();
    this.thesaurusProviderMap.clear();
    this.servicesProviderMap.clear();
    this.taskMap.clear();
    this.taskList.clear();
    this.indexMap.clear();
    this.indexList.clear();
    this.thesaurusMap.clear();
    this.thesaurusList.clear();
    this.importersMap.clear();
    this.importersList.clear();
    this.exportersMap.clear();
    this.rolesMap.clear();
    this.rolesList.clear();
    this.transformersMap.clear();
    this.transformersList.clear();

    this.singletonsList.clear();
    this.environmentsList.clear();
  }

  public void registerLanguage(Class<? extends org.monet.metamodel.interfaces.Language> clazz, String localeCode) {
    org.monet.metamodel.interfaces.Language language;
    try {
      language = clazz.newInstance();
      this.addLanguage(localeCode, language);
    } catch (Exception e) {
      this.getProblems().add(new Problem("Error loading language class", e.getClass().toString(), ExceptionUtils.getStackTrace(e), Problem.SEVERITY_ERROR));
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

  public String getModelResource(Object res, String codeLanguage) {
    if (res == null)
      return null;
    if (res instanceof String)
      return (String) res;
    Integer resId = (Integer) res;

    String[] parts = codeLanguage.split("-");
    String langCode = parts[0];
    String countryCode = parts.length > 1 ? parts[1] : null;
    Locale locale = countryCode != null ? new Locale(langCode, countryCode) : new Locale(langCode);

    int iResId = resId.intValue();
    String resource = null;
    org.monet.metamodel.interfaces.Language language = this.getLanguage(codeLanguage);
    if (language != null) // Language exists
      resource = language.get(iResId);
    if (!locale.getLanguage().equals(codeLanguage) && (language == null || resource == null)) {
      // Check only with locale language
      language = this.getLanguage(locale.getLanguage());

      if (language != null)
        resource = language.get(iResId);
    }

    if (resource == null) {
      // From default language
      language = this.getDefaultLanguage();
      resource = language.get(iResId);
    }

    return resource;
  }

  public LinkedList<Definition> getAncestors(Definition definition) {
    return new LinkedList<Definition>();
  }

  private <T> ArrayList<T> getDefinition(String key, HashMap<String, T> definitionMap) {
    ArrayList<T> definitions = new ArrayList<T>();

    Definition mainDefinition = this.definitionMap.get(key);
    String mainDefinitionName = null;
    if (mainDefinition != null)
      mainDefinitionName = mainDefinition.getName();
    ArrayList<String> implementers = this.dependencyMap.get(mainDefinitionName);
    if (implementers == null) {
      System.out.println("no implementers on Dictionary.getDefinition");
    }
    if (implementers.size() == 0) {
      System.out.println("no implementers on Dictionary.getDefinition");
    }

    for (String implementerKey : implementers) {
      T definition = definitionMap.get(implementerKey);
      definitions.add(definition);
    }

    return definitions;
  }

  public ArrayList<NodeDefinition> getAllImplementersOfNodeDefinition(String key) {
    return this.getDefinition(key, this.nodesMap);
  }

  public Definition getDefinition(String key) {
    return this.getDefinition(key, this.definitionMap).get(0);
  }

  public NodeDefinition getNodeDefinition(String key) {
    return (NodeDefinition) this.definitionMap.get(key);
  }

  public ArrayList<DesktopDefinition> getDesktopDefinitionList() {
    return this.desktopsList;
  }

  public ArrayList<ContainerDefinition> getContainerDefinitionList() {
    return this.containersList;
  }

  public ArrayList<CollectionDefinition> getCollectionDefinitionList() {
    return this.collectionsList;
  }

  public ArrayList<CatalogDefinition> getCatalogDefinitionList() {
    return this.catalogsList;
  }
  
  public ArrayList<FormDefinition> getFormDefinitionList() {
    return this.formsList;
  }
  
  public ArrayList<DocumentDefinition> getDocumentDefinitionList() {
    return this.documentsList;
  }
  
  public ArrayList<CubeDefinition> getCubeDefinitionList() {
    return this.cubesList;
  }

  public ArrayList<RoleDefinition> getRoleDefinitionList() {
    return this.rolesList;
  }

  public RoleDefinition getRoleDefinition(String key) {
    return this.rolesMap.get(key);
  }

  public ThesaurusDefinition getThesaurusDefinition(String key) {
    return this.thesaurusMap.get(key);
  }

  public IndexDefinition getIndexDefinition(String key) {
    if (key.equals(DescriptorDefinition.CODE)) 
      return new DescriptorDefinition();
    return this.getDefinition(key, this.indexMap).get(0);
  }

  public List<NodeDefinition> getEnvironmentDefinitionList() {
    return this.environmentsList;
  }

  public ArrayList<Problem> getProblems() {
    return problems;
  }

  public void setProblems(ArrayList<Problem> problems) {
    this.problems = problems;
  }
}