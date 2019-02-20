package org.monet.metacompiler.engine.renders;

import java.util.HashMap;

import org.monet.core.metamodel.MetaClass;

public class EditorGramaticRender extends EditorRender {

  public EditorGramaticRender() {
    super();
  }
  
  @Override
  protected void init() {
    loadCanvas("editor.gramatic", true);
    
    this.initMetaClassListDefinitionsNotAbstract();
    this.initMetaProperties();
    this.initMetaMethods();
    this.initMetaAttributes();
  }

  private void initMetaClassListDefinitionsNotAbstract() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String metaClassListDefinitionsNotAbstract = "";
    int pos = 0;
    
    for (MetaClass metaClass : metamodel.getMetaClassListDefinitionsNotAbstract()) {
      map.put("or", pos==0?"":"|");
      map.put("token", metaClass.getToken());
      metaClassListDefinitionsNotAbstract += block("metaClassListDefinitionsNotAbstract", map);
      pos++;
    }
    
    addMark("metaClassListDefinitionsNotAbstract", metaClassListDefinitionsNotAbstract);
  }

  private void initMetaProperties() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    int pos = 0;
    String metaProperties = "";
    
    for (String metaProperty : metamodel.getAllMetaProperties()) {
      map.put("or", pos==0?"":"|");
      map.put("name", metaProperty);
      metaProperties += block("metaProperty", map);
      pos++;
    }
    
    addMark("metaProperties", metaProperties);
  }

  private void initMetaMethods() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    int pos = 0;
    String metaMethods = "";
    
    for (String metaMethod : metamodel.getAllMetaMethods()) {
      map.put("or", pos==0?"":"|");
      map.put("token", getSyntax().getToken(metaMethod, false, false));
      metaMethods += block("metaMethod", map);
      pos++;
    }
    
    addMark("metaMethods", metaMethods);
  }

  private void initMetaAttributes() {
    HashMap<String, Object> map = new HashMap<String, Object>();
  	int pos = 0;
  	String metaAttributes = "";
  	
    for (String metaAttribute : metamodel.getAllMetaAttributes()) {
      map.put("or", pos==0?"":"|");
      map.put("name", metaAttribute);
      metaAttributes += block("metaAttribute", map);
      pos++;
    }
    
    addMark("metaAttributes", metaAttributes);
  }
  
}
