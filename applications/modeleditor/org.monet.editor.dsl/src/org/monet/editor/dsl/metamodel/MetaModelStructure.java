package org.monet.editor.dsl.metamodel;

import java.util.Collection;


public interface MetaModelStructure {

  public Item getDefinition(String name);

  public Collection<String> getDefinitions();
  
  public boolean areFamily(String child, String ancestor);
  
}
