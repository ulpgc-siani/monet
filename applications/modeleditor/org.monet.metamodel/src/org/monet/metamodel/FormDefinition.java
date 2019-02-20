package org.monet.metamodel;

import org.monet.metamodel.interfaces.HasMappings;
import org.monet.metamodel.interfaces.IsInitiable;

public class FormDefinition extends FormDefinitionBase implements IsInitiable, HasMappings {

  public void init() {

  }

  @Override
  public Class<?> getMappingClass(String code) {
    // TODO Auto-generated method stub
    return null;
  }

}
