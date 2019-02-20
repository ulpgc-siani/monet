package org.monet.metamodel;

import org.monet.metamodel.interfaces.HasMappings;
import org.monet.metamodel.interfaces.IsInitiable;

// DocumentDefinition
// Declaración que se utiliza para modelar un documento

public class DocumentDefinition extends DocumentDefinitionBase implements IsInitiable, HasMappings {

  public void init() {

  }

  @Override
  public Class<?> getMappingClass(String code) {
    // TODO Auto-generated method stub
    return null;
  }

}
