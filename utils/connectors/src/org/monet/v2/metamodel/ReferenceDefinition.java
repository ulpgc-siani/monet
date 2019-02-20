package org.monet.v2.metamodel;


import java.util.HashMap;

import org.simpleframework.xml.core.Commit;

public class ReferenceDefinition extends ReferenceDefinitionBase {

  protected ReferenceViewDeclaration                  defaultView;
  protected HashMap<String, AttributeDeclaration>     attributeMap = new HashMap<String, AttributeDeclaration>();
  protected HashMap<String, ReferenceViewDeclaration> viewMap      = new HashMap<String, ReferenceViewDeclaration>();

  @Commit
  public void commit() {
    for (AttributeDeclaration attribute : this._attributeDeclarationList) {
      this.attributeMap.put(attribute.getCode(), attribute);
      this.attributeMap.put(attribute.getName(), attribute);
    }
    for (ReferenceViewDeclaration view : this._referenceViewDeclarationList) {
      this.viewMap.put(view.getCode(), view);
      this.viewMap.put(view.getName(), view);
      if (view.isDefault())
        this.defaultView = view;
    }
  }

  public AttributeDeclaration getAttribute(String key) {
    return this.attributeMap.get(key);
  }

  public ReferenceViewDeclaration getReferenceViewDeclaration(String key) {
    return this.viewMap.get(key);
  }

  public ReferenceViewDeclaration getDefaultView() {
    return this.defaultView;
  }

}
