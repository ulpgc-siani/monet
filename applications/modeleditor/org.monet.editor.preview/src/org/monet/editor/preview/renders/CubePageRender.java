package org.monet.editor.preview.renders;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.monet.editor.preview.model.Problem;
import org.monet.metamodel.CubeDefinition;

public class CubePageRender extends PageRender {
  protected CubeDefinition definition;
  
  public CubePageRender(String language) {
    super(language);
  }

  @Override public void setTarget(Object target) {
   	this.definition = (CubeDefinition)target;
  }
  
  @Override protected void init() {
  	loadCanvas("cube");
  	super.init();
  }
  
  @Override protected String initPage() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String view = this.getParameterAsString("view");

    map.put("header", this.initHeader(view));
    map.put("tabs", this.initTabs());
    
    return block("page", map);
  }
  
  protected String initHeader(String view) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    map.put("path", this.getParameterAsString("path"));
    map.put("label", language.getModelResource(this.definition.getLabel(), this.codeLanguage));
    map.put("description", language.getModelResource(this.definition.getDescription(), this.codeLanguage));
    
    return block("header", map);
  }

  protected String initTabs() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    List<CubeDefinition.ViewProperty> tabViewDefinitionList = this.definition.getTabViewPropertyList();
    String tabsList = "";
    
    for (CubeDefinition.ViewProperty viewDefinition : tabViewDefinitionList) {
      tabsList += this.initTab(viewDefinition);
    }
    
    map.put("code", definition.getCode());
    map.put("defaultTab", definition.getCode() + definition.getDefaultTabView().getCode());
    map.put("tabsList", tabsList);
    
    return block("tabs", map);
  }
    
  protected String initTab(CubeDefinition.ViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String viewToRender = this.getParameterAsString("view");

    map.put("code", definition.getCode());
    map.put("label", language.getModelResource(viewDefinition.getLabel(), this.codeLanguage));
    map.put("from", this.getParameterAsString("from"));
    map.put("codeView", viewDefinition.getCode());

    if ((viewDefinition.isDefault() && viewToRender.isEmpty()) || (!viewToRender.isEmpty())) {

      try {
        PreviewRender render = this.rendersFactory.get(this.definition, "view.html", this.codeLanguage);
        render.setParameters(this.getParameters());
        render.setParameter("view", viewDefinition.getCode());
        map.put("render(view.cube)", render.getOutput());
      }
      catch (Exception exception) {
        this.problems.add(new Problem(String.format("Compiling definition %s", this.definition.getName()), exception.getClass().toString(), ExceptionUtils.getStackTrace(exception), Problem.SEVERITY_ERROR));
      }
      
      return block("tab", map);
    }
    else return block("tab.loading", map);
  }

}