package org.monet.editor.preview.renders;

import java.util.HashMap;

import org.monet.metamodel.CubeDefinition;
import org.monet.metamodel.Definition;
import org.monet.metamodel.DesktopDefinition;
import org.monet.metamodel.DesktopDefinitionBase.ViewProperty;
import org.monet.metamodel.DesktopDefinitionBase.ViewProperty.ShowProperty;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.ThesaurusDefinition;
import org.monet.metamodel.internal.Ref;

public class DesktopViewRender extends NodeViewRender {

  public DesktopViewRender(String language) {
    super(language);
  }

  private String showEntityLinks(ShowProperty showDefinition) {
    String showBlockName = "";
    String links = "";
    
    if (showDefinition.getLink() == null)
      return links;
    
    for (Ref reference : showDefinition.getLink()) {
      Definition definition = dictionary.getDefinition(reference.getValue());
      HashMap<String, Object> localMap = new HashMap<String, Object>();
      String label = language.getModelResource(definition.getLabel(), this.codeLanguage);
      String description = language.getModelResource(definition.getDescription(), this.codeLanguage);

      if (definition instanceof NodeDefinition)
        showBlockName = "link.entity";
      else if (definition instanceof CubeDefinition)
        showBlockName = "link.entity.cube";
      else if (definition instanceof ThesaurusDefinition)
        showBlockName = "link.entity.thesaurus";
      else
        continue;

      localMap.put("entityCode", definition.getCode());
      localMap.put("entityName", definition.getName());
      localMap.put("entityLabel", label);
      localMap.put("entityDescription", description);

      links += block(showBlockName, localMap);

      localMap.clear();
    }
    
    return links;
  }

  protected String showNewsLink(ShowProperty showDefinition) {
    
    if (showDefinition.getLinkNews() == null)
      return "";

    HashMap<String, Object> map = new HashMap<String, Object>();
    
    return block("link.news", map);
  }

  protected String showTasksLink(ShowProperty showDefinition) {
    
    if (showDefinition.getLinkTasks() == null)
      return "";

    HashMap<String, Object> map = new HashMap<String, Object>();
    
    return block("link.tasks", map);
  }

  protected String showTrashLink(ShowProperty showDefinition) {
    
    if (showDefinition.getLinkTrash() == null)
      return "";
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    return block("link.trash", map);
  }

  protected String showRolesLink(ShowProperty showDefinition) {
    
    if (showDefinition.getLinkRoles() == null)
      return "";
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    return block("link.roles", map);
  }

  protected String showNews(ViewProperty viewDefinition, ShowProperty showDefinition) {
    
    if (showDefinition.getNews() == null)
      return "";

    HashMap<String, Object> map = new HashMap<String, Object>();
    
    map.put("codeView", viewDefinition.getCode());

    return block("show.news", map);
  }

  protected String showTasks(ViewProperty viewDefinition, ShowProperty showDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();

    if (showDefinition.getTasks() == null)
      return "";
    
    return block("show.tasks", map);
  }

  protected String showTasksPending(ViewProperty viewDefinition, ShowProperty showDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    if (showDefinition.getTasksPending() == null)
      return "";
    
    return block("show.tasks.pending", map);
  }
  
  protected String showTasksActive(ViewProperty viewDefinition, ShowProperty showDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    if (showDefinition.getTasksActive() == null)
      return "";
    
    return block("show.tasks.active", map);
  }
  
  protected String showTrash(ViewProperty viewDefinition, ShowProperty showDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();

    if (showDefinition.getTrash() == null)
      return "";

    return block("show.trash", map);
  }

  protected String showRoles(ViewProperty viewDefinition, ShowProperty showDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();

    if (showDefinition.getRoles() == null)
      return "";
    
    return block("show.roles", map);
  }
  
  protected void initContent(HashMap<String, Object> viewMap, DesktopDefinition.ViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String shows = "", links = "";
    ShowProperty showDefinition = viewDefinition.getShow();
    
    links += this.showEntityLinks(showDefinition);
    links += this.showNewsLink(showDefinition);
    links += this.showTasksLink(showDefinition);
    links += this.showTrashLink(showDefinition);
    links += this.showRolesLink(showDefinition);

    shows += this.showNews(viewDefinition, showDefinition);
    shows += this.showTasks(viewDefinition, showDefinition);
    shows += this.showTasksPending(viewDefinition, showDefinition);
    shows += this.showTasksActive(viewDefinition, showDefinition);
    shows += this.showTrash(viewDefinition, showDefinition);
    shows += this.showRoles(viewDefinition, showDefinition);
    
    map.put("links", links);
    map.put("shows", shows);

    viewMap.put("content", block("content", map));
  }

  @Override
  protected String initView(String codeView) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    DesktopDefinition.ViewProperty viewDefinition = (DesktopDefinition.ViewProperty) this.definition.getNodeViewProperty(codeView);
    boolean isLocationView = codeView.equals("location");

    if (isLocationView) {
      this.initMapWithoutView(map, "location");
      return this.initLocationView(map);
    } else if (viewDefinition == null) {
      map.put("codeView", codeView);
      map.put("labelDefinition", this.definition.getLabelString(this.codeLanguage));
      return block("view.undefined", map);
    }

    this.initMap(map, viewDefinition);

    if (this.isSystemView(viewDefinition)) {
      return this.initSystemView(map, viewDefinition);
    }

    this.initContent(map, viewDefinition);

    return block("view", map);
  }

  @Override
  protected void init() {
    loadCanvas("node.desktop");
    super.init();
  }

}
