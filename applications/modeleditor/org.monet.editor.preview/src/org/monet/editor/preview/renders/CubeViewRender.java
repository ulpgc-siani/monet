package org.monet.editor.preview.renders;

import java.util.HashMap;

import org.monet.metamodel.CubeDefinition;

public class CubeViewRender extends ViewRender {
  protected CubeDefinition definition;
  
  public CubeViewRender(String language) {
    super(language);
  }

  @Override public void setTarget(Object target) {
   	this.definition = (CubeDefinition)target;
  }
  
  @Override protected void init() {
    loadCanvas("cube");
    super.init();
  }
  
  @Override
  protected String initView(String codeView) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    CubeDefinition.ViewProperty viewDefinition = (CubeDefinition.ViewProperty)this.definition.getView(codeView);
    
    if (viewDefinition == null) {
      map.put("codeView", codeView);
      map.put("labelDefinition", language.getModelResource(this.definition.getLabel(), this.codeLanguage));
      return block("view.undefined", map);
    }
    
    this.initControlInfo(map, viewDefinition);
    this.initContent(map, viewDefinition);
    
    return block("view", map);
  }

  protected void initControlInfo(HashMap<String, Object> map, CubeDefinition.ViewProperty viewDefinition) {
    map.put("code", this.definition.getCode());
    map.put("name", this.definition.getName());
    map.put("from", this.getParameterAsString("from"));
    map.put("codeView", viewDefinition.getCode());
  }

  protected void initContent(HashMap<String, Object> map, CubeDefinition.ViewProperty viewDefinition) {
    CubeDefinition.ViewProperty.ModeEnumeration mode = viewDefinition.getMode();
    String result = "";
    
    if (mode.equals(CubeDefinition.ViewProperty.ModeEnumeration.DYNAMIC))
      result = this.initDynamicContent(viewDefinition);
    else
      result = this.initStaticContent(viewDefinition);
    
    map.put("content", result);
  }

  private String initDynamicContent(CubeDefinition.ViewProperty viewDefinition) {
    HashMap<String, Object> map;

    map = this.getDynamicContentReportMap(viewDefinition);
    
    return block("view.dynamic$content", map);
  }
  
  protected HashMap<String, Object> getDynamicContentReportMap(CubeDefinition.ViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String toolbar = "", body = "";
    
    toolbar = block("view.dynamic$content$toolbar.report", map);
    body = block("view.dynamic$content$body.report", map);
    
    map.clear();
    map.put("toolbar", toolbar);
    map.put("body", body);
    
    return map;
  }
  
  private String initStaticContent(CubeDefinition.ViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    map.put("codeView", viewDefinition.getCode());
    map.put("title", language.getModelResource(this.definition.getLabel(), this.codeLanguage));
    
    return block("view.static$content", map);
  }
  
}