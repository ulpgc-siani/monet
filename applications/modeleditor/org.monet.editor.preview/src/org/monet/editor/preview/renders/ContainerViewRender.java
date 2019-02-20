package org.monet.editor.preview.renders;

import java.util.HashMap;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.monet.editor.preview.model.Problem;
import org.monet.metamodel.ContainerDefinition;
import org.monet.metamodel.ContainerDefinitionBase.ViewProperty.ShowProperty;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.internal.Ref;

public class ContainerViewRender extends NodeViewRender {
  
  public ContainerViewRender(String language) {
    super(language);
  }

  protected void initContent(HashMap<String, Object> viewMap, ContainerDefinition.ViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String renders = "";
    ShowProperty showDefinition = viewDefinition.getShow();
    
    if (showDefinition != null && showDefinition.getComponent() != null) {
      Ref componentDefinition = showDefinition.getComponent();
      NodeDefinition childDefinition = (NodeDefinition)dictionary.getDefinition(componentDefinition.getDefinition());
      
      try {
        PreviewRender definitionRender = this.rendersFactory.get(childDefinition, "view.html", this.codeLanguage);
        definitionRender.setParameters(this.getParameters());
        definitionRender.setParameter("view", componentDefinition.getValue());
        map.put("render(view.node)", definitionRender.getOutput());
      }
      catch (Exception exception) {
        this.problems.add(new Problem(String.format("Compiling definition %s", childDefinition.getName()), exception.getClass().toString(), ExceptionUtils.getStackTrace(exception), Problem.SEVERITY_ERROR));
      }
      
      renders += block("show", map);
      map.clear();
    }
    
    map.put("shows", renders);
    
    viewMap.put("content", block("content", map));
  }
  
  @Override
  protected String initView(String codeView) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    ContainerDefinition.ViewProperty viewDefinition = (ContainerDefinition.ViewProperty)this.definition.getNodeViewProperty(codeView);
    
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

  @Override protected void init() {
    loadCanvas("node.container");
    super.init();
  }

}
