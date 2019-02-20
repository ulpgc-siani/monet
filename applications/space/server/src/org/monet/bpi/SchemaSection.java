package org.monet.bpi;

import java.util.HashMap;

public abstract class SchemaSection {

  public SchemaSection() {
  }

  public abstract HashMap<String, Object> getAll();

  public abstract void set(String key, Object value);

}
