package org.monet.editor.preview.renders;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.monet.editor.preview.model.Problem;
import org.monet.editor.preview.utils.StringHelper;
import org.monet.metamodel.ContainerDefinition;
import org.monet.metamodel.Definition;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.NodeDefinitionBase.OperationProperty;
import org.monet.metamodel.NodeViewProperty;

public class NodePageRender extends PageRender {
  protected NodeDefinition definition;
  
  public NodePageRender(String language) {
    super(language);
  }

  @Override public void setTarget(Object target) {
   	this.definition = (NodeDefinition)target;
  }
  
  @Override protected void init() {
  	loadCanvas("node");
  	super.init();
  }
  
  @Override protected String initPage() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    map.put("type", this.getDefinitionType(this.definition));
    map.put("configuration", this.initConfiguration());
    map.put("header", this.initHeader());
    map.put("tabs", this.initTabs());
    
    return block("page", map);
  }

  protected String initHeader() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    boolean isEnvironment = this.definition.isEnvironment();
    String label = language.getModelResource(this.definition.getLabel(), codeLanguage);
    String description = language.getModelResource(this.definition.getDescription(), codeLanguage);

    map.put("label", label);
    map.put("shortLabel", StringHelper.shortValue(label));
    map.put("shortBreadcrumbsLabel", StringHelper.shortValue(label, 30));
    map.put("description", description);
    map.put("shortDescription", StringHelper.shortValue(description));
    map.put("flags", (this.definition.isReadonly())?block("flag.locked", null):"");
    map.put("home", this.getParameterAsString("home"));
        
    this.initBreadCrumbs(map);
    
    if (isEnvironment)
      return block("header.desktop", map);
    else
      return block("header.other", map);
  }
  
  protected void initBreadCrumbs(HashMap<String, Object> headerMap) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String breadCrumbs = "";
    String label = language.getModelResource(this.definition.getLabel(), codeLanguage);

    map.put("label", label);
    map.put("shortLabel", StringHelper.shortValue(label, 30));
    
    for (Definition definition : dictionary.getAncestors(this.definition)) {
      if (! (definition instanceof NodeDefinition)) continue;
      NodeDefinition nodeDefinition = (NodeDefinition)definition;
      if (nodeDefinition.isComponent()) continue;
      map.put("code", definition.getCode());
      map.put("name", definition.getName());
      map.put("label", definition.getLabelString(codeLanguage));
      map.put("shortLabel", StringHelper.shortValue(definition.getLabelString(codeLanguage), 30));
      breadCrumbs += block("breadcrumb", map);
      map.clear();
    }
    
    headerMap.put("breadcrumbs", breadCrumbs);
  }
  
  protected String initConfiguration() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String configuration = "";
    
    for (OperationProperty operationDefinition : this.definition.getOperationList()) {
      map.put("name", operationDefinition.getName());
      map.put("label", language.getModelResource(operationDefinition.getLabel(), this.codeLanguage));
      configuration += block("operation", map);
      map.clear();
    }
    
    if (!this.definition.isDesktop() && !this.definition.isCatalog()) {
      map.put("from", this.getParameterAsString("from"));
      configuration += block("operation.editable", map);
      map.clear();
    }
    
    if (this.definition.isDuplicable()) {
      map.put("from", this.getParameterAsString("from"));
      configuration += block("operation.copy", map);
      map.clear();
    }

    return configuration;
  }

  protected String initTab(NodeViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();

    if (viewDefinition instanceof ContainerDefinition.ViewProperty) {
      ContainerDefinition.ViewProperty.ShowProperty showDefinition = ((ContainerDefinition.ViewProperty) viewDefinition).getShow();
      if (showDefinition.getNotes() != null || showDefinition.getTasks() != null) return "";
      if (showDefinition.getNotes() != null) return "";
      if (showDefinition.getTasks() != null) return "";
    }
    
    if (viewDefinition instanceof FormViewProperty) {
      FormViewProperty.ShowProperty showDefinition = ((FormViewProperty) viewDefinition).getShow();
      if (showDefinition.getNotes() != null || showDefinition.getTasks() != null) return "";
      if (showDefinition.getNotes() != null) return "";
      if (showDefinition.getTasks() != null) return "";
    }

    String template = (this.definition.isReadonly())?block("tab$template.readonly", map):block("tab$template", map);
    String label = language.getModelResource(viewDefinition.getLabel(), this.codeLanguage);

    if (label == null || label.isEmpty()) 
      label = block("tab$emptyLabel", new HashMap<String, Object>());
    
    map.put("code", definition.getCode());
    map.put("label", label);
    map.put("from", this.getParameterAsString("from"));
    map.put("codeView", viewDefinition.getCode());
    map.put("template", template);

    try {
      PreviewRender render = this.rendersFactory.get(this.definition, "view.html", this.codeLanguage);
      render.setParameters(this.getParameters());
      render.setParameter("view", viewDefinition.getCode());
      map.put("render(view.node)", render.getOutput());
    }
    catch (Exception exception) {
      this.problems.add(new Problem(String.format("Compiling definition %s", this.definition.getName()), exception.getClass().toString(), ExceptionUtils.getStackTrace(exception), Problem.SEVERITY_ERROR));
    }
    
    return block("tab", map);
  }
  
  protected String initMapTab() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    map.put("code", "location");
    map.put("label", block("label.location", map));
    map.put("codeView", "location");

    return block("tab.loading", map);
  }

  protected String initTabs() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    List<NodeViewProperty> tabViewDefinitionList = definition.getTabViewList();
    String tabsList = "";
    String idRevision = this.getParameterAsString("idrevision");
    
    if (this.definition.isDesktop() && tabViewDefinitionList.size() == 1) {
      NodeViewProperty viewDefinition = tabViewDefinitionList.get(0);
      
      try {
        PreviewRender render = this.rendersFactory.get(this.definition, "view.html", this.codeLanguage);
        render.setParameters(this.getParameters());
        render.setParameter("view", viewDefinition.getCode());
        map.put("render(view.node)", render.getOutput());
      }
      catch (Exception exception) {
        this.problems.add(new Problem(String.format("Compiling definition %s", this.definition.getName()), exception.getClass().toString(), ExceptionUtils.getStackTrace(exception), Problem.SEVERITY_ERROR));
      }
    
      addMark("tabs", block("tabs.empty", map));
    }
    
    map.put("hide", (tabViewDefinitionList.size() == 1)?"hide":"");
    
    for (NodeViewProperty viewDefinition : tabViewDefinitionList) {
      tabsList += this.initTab(viewDefinition);
    }
    
    if (definition.isGeoreferenced() && idRevision.isEmpty()) {
      tabsList += this.initMapTab();
    }
    
    String defaultTabCode = (definition.getDefaultTabView() != null)?definition.getDefaultTabView().getCode():null;
    if (defaultTabCode == null) 
      defaultTabCode = (definition.getTabViewList().size() > 0)?definition.getTabViewList().get(0).getCode():"";
    
    map.put("defaultTab", definition.getCode() + defaultTabCode);
    map.put("code", this.definition.getCode());
    map.put("tabsList", tabsList);
    
    return block("tabs", map);
  }

}
