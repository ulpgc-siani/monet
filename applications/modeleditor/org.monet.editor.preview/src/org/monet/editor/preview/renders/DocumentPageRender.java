package org.monet.editor.preview.renders;

import java.util.HashMap;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.monet.editor.preview.model.Problem;

public class DocumentPageRender extends NodePageRender {
  
  public DocumentPageRender(String language) {
    super(language);
  }
  
  @Override
  protected String initConfiguration() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String configuration = "";
    
    super.initConfiguration();
    
    configuration += block("operation.download", map);

    return configuration;
  }
  
  @Override protected void init() {
    loadCanvas("node.document");
    super.init();
  }

  @Override
  protected String initTabs() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String tabsList = "";
    String idRevision = this.getParameterAsString("idrevision");
    
    map.put("code", this.definition.getCode());
    map.put("codeView", "default");
    map.put("label", block("previewLabel", map));
    map.put("from", this.getParameterAsString("from"));
    map.put("template", template);

    try {
      PreviewRender nodeRender = this.rendersFactory.get(this.definition, "view.html", this.codeLanguage);
      nodeRender.setParameters(this.getParameters());
      nodeRender.setParameter("view", "default");
      map.put("render(view.node)", nodeRender.getOutput());
    }
    catch (Exception exception) {
      this.problems.add(new Problem(String.format("Compiling definition %s", this.definition.getName()), exception.getClass().toString(), ExceptionUtils.getStackTrace(exception), Problem.SEVERITY_ERROR));
    }
      
    tabsList += block("tab", map);
    tabsList += this.initSignsTab();
    
    if (definition.isGeoreferenced() && idRevision.isEmpty()) {
      tabsList += this.initMapTab();
    }

    map.put("defaultTab", this.definition.getCode() + "default");
    map.put("tabsList", tabsList);
    
    return block("tabs", map);
  }
  
  protected String initSignsTab() {
    HashMap<String, Object> map = new HashMap<String, Object>();

    try {
      PreviewRender render = this.rendersFactory.get(this.definition, "view.html", this.codeLanguage);
      render.setParameters(this.getParameters());
      render.setParameter("view", "signs");
  
      map.put("code", this.definition.getCode());
      map.put("codeView", "signs");
      map.put("label", block("signaturesLabel", map));
      map.put("class", "orange");
      map.put("render(view.node)", render.getOutput());
    }
    catch (Exception exception) {
      this.problems.add(new Problem(String.format("Compiling definition %s", this.definition.getName()), exception.getClass().toString(), ExceptionUtils.getStackTrace(exception), Problem.SEVERITY_ERROR));
    }
    
    return block("tab.signs", map);
  }
  
}
