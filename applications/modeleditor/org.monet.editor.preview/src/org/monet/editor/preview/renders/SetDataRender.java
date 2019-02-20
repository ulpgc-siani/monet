package org.monet.editor.preview.renders;

import java.util.Collection;
import java.util.HashMap;

import org.monet.metamodel.AttributeProperty;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.IndexDefinitionBase.ReferenceProperty;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.SetDefinition;
import org.monet.metamodel.SetDefinition.SetViewProperty;

public abstract class SetDataRender extends DataRender {
  protected NodeDefinition definition;
  
  public SetDataRender(String language) {
    super(language);
  }
  
  @Override public void setTarget(Object target) {
    this.definition = (NodeDefinition)target;    
  }
  
  protected void addViewDefinitionAttributes(HashMap<String, Object> map, SetViewProperty viewDefinition) {
    SetDefinition definition = (SetDefinition)this.definition;
    IndexDefinition indexDefinition = dictionary.getIndexDefinition(definition.getIndex().getValue());
    ReferenceProperty referenceProperty = indexDefinition.getReference();
    StringBuilder attributes = new StringBuilder();
    
    if (referenceProperty != null) {
      Collection<AttributeProperty> attributeDefinitionList = referenceProperty.getAttributePropertyList();
      for (AttributeProperty attributeDefinition : attributeDefinitionList) {
        HashMap<String, Object> localMap = new HashMap<String, Object>();
        
        localMap.put("code", attributeDefinition.getCode());
        localMap.put("value", block("attributeValue", localMap));
        attributes.append(block("row$attribute", localMap));
      }
    }
    
    map.put("attributes", attributes.toString());
  }

  protected String initRows(HashMap<String, HashMap<String, String>> nodesDefinitions, SetViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    StringBuilder nodesDefinitionsValue = new StringBuilder();
    int pos = 0;
    
    for (HashMap<String, String> nodeDefinitionMap : nodesDefinitions.values()) {
      HashMap<String, Object> localMap = new HashMap<String, Object>();
      
      localMap.put("id", nodeDefinitionMap.get("name"));
      localMap.put("name", nodeDefinitionMap.get("name"));
      localMap.put("code", nodeDefinitionMap.get("code"));
      localMap.put("label", nodeDefinitionMap.get("label"));
      localMap.put("description", nodeDefinitionMap.get("description"));
      localMap.put("comma", (pos == nodesDefinitions.size()-1)?"":"comma");
      
      this.addViewDefinitionAttributes(localMap, viewDefinition);

      nodesDefinitionsValue.append(block("row", localMap));
    }
    
    map.put("nrows", String.valueOf(nodesDefinitions.size()));
    map.put("rows", nodesDefinitionsValue.toString());
    
    return block("data", map);
  }

  protected abstract HashMap<String, HashMap<String, String>> getNodesDefinitionsMap(SetViewProperty view);
  
}
