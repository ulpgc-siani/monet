package org.monet.metamodel;

import java.util.HashMap;

import org.monet.metamodel.interfaces.IsInitiable;

public class IndexDefinition extends IndexDefinitionBase implements IsInitiable {

  protected IndexViewProperty                  defaultView;
  protected HashMap<String, AttributeProperty> attributeMap = new HashMap<String, AttributeProperty>();
  protected HashMap<String, IndexViewProperty> viewMap      = new HashMap<String, IndexViewProperty>();

  public void init() {
    if (_referenceProperty != null) {
      for (AttributeProperty attribute : this._referenceProperty._attributePropertyMap.values()) {
        this.attributeMap.put(attribute.getCode(), attribute);
        this.attributeMap.put(attribute.getName(), attribute);
      }
      for (IndexViewProperty view : this._indexViewPropertyMap.values()) {
        this.viewMap.put(view.getCode(), view);
        this.viewMap.put(view.getName(), view);
        if (view.isDefault())
          this.defaultView = view;
      }
    }
  }

  public AttributeProperty getAttribute(String key) {
    return this.attributeMap.get(key);
  }

  public IndexViewProperty getIndexViewProperty(String key) {
    return this.viewMap.get(key);
  }

  public IndexViewProperty getDefaultView() {
    return this.defaultView;
  }

}
