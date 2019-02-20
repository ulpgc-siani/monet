package org.monet.metamodel;

import java.util.HashMap;

public abstract class AbstractManifest extends AbstractManifestBase {
  
  protected HashMap<String, String> defineMap = new HashMap<String, String>();
  
  public HashMap<String, String> getDefineMap() {
    return this.defineMap;
  }
  
}