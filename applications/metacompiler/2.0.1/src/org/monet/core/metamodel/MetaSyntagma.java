package org.monet.core.metamodel;

import org.simpleframework.xml.Element;

public abstract class MetaSyntagma {
  private @Element(required = false) String description = "";
  private @Element(required = false) String hint = "";

  public String getDescription() {
    return description;
  }

  public String getHint() {
    return hint;
  }

}
