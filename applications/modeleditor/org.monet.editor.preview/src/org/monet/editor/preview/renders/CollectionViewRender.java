package org.monet.editor.preview.renders;

import java.util.ArrayList;
import java.util.HashMap;

import org.monet.editor.preview.utils.StringHelper;
import org.monet.metamodel.CollectionDefinition;
import org.monet.metamodel.Definition;
import org.monet.metamodel.NodeViewProperty;
import org.monet.metamodel.SetDefinition.SetViewProperty;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.SelectProperty;
import org.monet.metamodel.internal.Ref;

public class CollectionViewRender extends SetViewRender {
  
  public CollectionViewRender(String language) {
    super(language);
  }

  @Override
  protected ArrayList<String> getViewSelects(SetViewProperty view) {
  	ArrayList<Ref> selectList = view.getSelect() != null ? view.getSelect().getNode() : new ArrayList<Ref>();
  	ArrayList<String> result = new ArrayList<String>();
  	
  	if (selectList.size() <= 0) {
  	  ArrayList<Ref> addList = ((CollectionDefinition)this.definition).getAdd().getNode();
  	  
  	  for (Ref add : addList) {
  	    for(Definition addDefinition : dictionary.getAllImplementersOfNodeDefinition(add.getValue()))
  	      result.add(addDefinition.getCode());
  	  }
  	  
  	  return result;
  	}
  	
		for (Ref select : selectList) {
			result.add(dictionary.getDefinition(select.getValue()).getCode());
		}
		
    return result;
  }

  @Override
  protected void fillNodesMap(SetViewProperty view) {
    SelectProperty selectDefinition = view.getSelect();
    ArrayList<Ref> addList = ((CollectionDefinition)this.definition).getAdd().getNode();
  	
    this.nodes = new HashMap<String, HashMap<String, String>>();

    if (selectDefinition != null && selectDefinition.getNode().size() > 0) {
      ArrayList<Ref> selectList = selectDefinition.getNode();
  		for (Ref select : selectList) {
  			Definition definition = dictionary.getDefinition(select.getValue());
  			HashMap<String, String> nodeMap = new HashMap<String, String>();
  			nodeMap.put("code", definition.getCode());
  			nodeMap.put("label", definition.getLabelString(this.codeLanguage));
  			nodeMap.put("description", language.getModelResource(definition.getDescription(), this.codeLanguage));
  			this.nodes.put(definition.getCode(), nodeMap);
  		}
  	}
  	else {
  		for (Ref add : addList) {
  			for(Definition definition : dictionary.getAllImplementersOfNodeDefinition(add.getValue())) {
    			HashMap<String, String> nodeMap = new HashMap<String, String>();
    			nodeMap.put("code", definition.getCode());
    			nodeMap.put("label", definition.getLabelString(this.codeLanguage));
    			nodeMap.put("description", definition.getDescriptionString(this.codeLanguage));
    			this.nodes.put(definition.getCode(), nodeMap);
  			}
  		}
  	}
  }
  
  @Override
  protected String initToolbarOperations(NodeViewProperty viewDefinition) {
    ArrayList<HashMap<String, Object>> operations = new ArrayList<HashMap<String, Object>>();
    String operationsValue = super.initToolbarOperations(viewDefinition);
    
    for (HashMap<String, String> nodeMap : this.nodes.values()) {
      HashMap<String, Object> localMap = new HashMap<String, Object>();
      Object description = nodeMap.get("description");
      
      localMap.put("id", this.definition.getCode() + viewDefinition.getCode());
      localMap.put("code", nodeMap.get("code"));
      localMap.put("label", nodeMap.get("label").toString());
      localMap.put("description", description!=null?description.toString():"");
      localMap.put("command", "javascript:void(null)");
      
      operations.add(localMap);
    }
    
    for (HashMap<String, Object> add : operations) 
      operationsValue += block("toolbar$operation.add", add);
    
    return operationsValue;
  }
  
  @Override
  protected String initAddList(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
  	String addList = "";

		for (HashMap<String, String> nodeMap : this.nodes.values()) {
			map.put("code", nodeMap.get("code"));
			map.put("label", StringHelper.cleanSpecialChars(nodeMap.get("label")));
			map.put("description", StringHelper.cleanSpecialChars(nodeMap.get("description")));
			map.put("command", "javascript:void(null)");
			addList += block("addList$item", map);
			map.clear();
		}
		
  	return addList;
  }

  @Override
	protected String initMagnets(HashMap<String, Object> viewMap, SetViewProperty view) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String magnets = "";
  	
  	for (String codeNode : this.nodes.keySet()) {
    	map.put("code", codeNode);
    	magnets += block("magnet", map);
    }
  	
  	return magnets;
	}
	
  @Override
  protected String initView(String codeView) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    SetViewProperty view = (SetViewProperty)this.definition.getNodeViewProperty(codeView);

    boolean isLocationView = codeView.equals("location"); 
    
    if (isLocationView) {
      this.initMapWithoutView(map, "location");
      return this.initLocationView(map);
    } else if (view == null) {
    	map.put("codeView", codeView);
    	map.put("labelDefinition", this.definition.getLabelString(this.codeLanguage));
    	return block("view.undefined", map);
    }
    
    this.fillNodesMap(view);
    this.initMap(map, view);
    map.put("clec", "clec");

    if (this.isSystemView(view)) {
      return this.initSystemView(map, view);
    }
    
    this.initContent(map, view);
    
    return block("view", map);
  }

  @Override protected void init() {
    loadCanvas("node.collection");
    super.init();
  }

}
