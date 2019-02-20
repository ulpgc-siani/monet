package org.monet.editor.preview.renders;

import java.util.ArrayList;
import java.util.HashMap;

import org.monet.metamodel.CollectionDefinition;
import org.monet.metamodel.Definition;
import org.monet.metamodel.SetDefinition.SetViewProperty;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.SelectProperty;
import org.monet.metamodel.internal.Ref;


public class CollectionDataRender extends SetDataRender {
  
  public CollectionDataRender(String language) {
    super(language);
  }

  @Override protected void init() {
    loadCanvas("data");

    SetViewProperty viewDefinition = (SetViewProperty)this.getParameter("view");
    StringBuilder data = new StringBuilder();
    
    data.append(this.initRows(this.getNodesDefinitionsMap(viewDefinition), viewDefinition));
    
    addMark("data", data.toString());
  }

  @Override
  protected HashMap<String, HashMap<String, String>> getNodesDefinitionsMap(SetViewProperty viewDefinition) {
    SelectProperty selectDefinition = viewDefinition.getSelect();
    ArrayList<Ref> addList = ((CollectionDefinition)this.definition).getAdd().getNode();
    HashMap<String, HashMap<String, String>> result = new HashMap<String, HashMap<String, String>>();

    if (selectDefinition != null && selectDefinition.getNode().size() > 0) {
      ArrayList<Ref> selectList = selectDefinition.getNode();
      for (Ref select : selectList) {
        Definition definition = dictionary.getDefinition(select.getValue());
        HashMap<String, String> nodeMap = new HashMap<String, String>();
        nodeMap.put("code", definition.getCode());
        nodeMap.put("name", definition.getName());
        nodeMap.put("label", definition.getLabelString(this.codeLanguage));
        nodeMap.put("description", language.getModelResource(definition.getDescription(), this.codeLanguage));
        result.put(definition.getCode(), nodeMap);
      }
    }
    else {
      for (Ref add : addList) {
        for(Definition definition : dictionary.getAllImplementersOfNodeDefinition(add.getValue())) {
          HashMap<String, String> nodeMap = new HashMap<String, String>();
          nodeMap.put("code", definition.getCode());
          nodeMap.put("name", definition.getName());
          nodeMap.put("label", definition.getLabelString(this.codeLanguage));
          nodeMap.put("description", definition.getDescriptionString(this.codeLanguage));
          result.put(definition.getCode(), nodeMap);
        }
      }
    }
    
    return result;
  }
  
}
