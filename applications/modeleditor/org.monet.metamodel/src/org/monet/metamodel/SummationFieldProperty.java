package org.monet.metamodel;

import java.util.HashMap;

import org.monet.metamodel.interfaces.IsInitiable;

// SelectFieldDeclaration
// Declaración que se utiliza para modelar un	campo de selección

public class SummationFieldProperty extends SummationFieldPropertyBase implements IsInitiable {

  @Override
  public void init() {

  }

  private HashMap<String, SummationItemProperty> map = new HashMap<String, SummationItemProperty>();

  public SummationItemProperty getChild(String key) {
    return this.map.get(key);
  }

}
