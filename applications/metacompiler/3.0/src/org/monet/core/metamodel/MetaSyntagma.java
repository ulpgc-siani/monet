package org.monet.core.metamodel;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public abstract class MetaSyntagma {
  private @Attribute(required = false) boolean extensible = false;
  private @Element(required = false) String description = "";
  private @Element(required = false) String hint = "";
  protected @ElementList(inline = true, required = false, name = "include") ArrayList<MetaInclude> includeList = new ArrayList<MetaInclude>();

  public abstract String getType();
  
  public boolean isExtensible() {
    return this.extensible;
  }
  
  public String getDescription() {
    return description;
  }

  public String getHint() {
    return hint;
  }

  public ArrayList<MetaInclude> getIncludeList() {
    return includeList;
  }

  public Boolean hasIncludes() {
    return (includeList.size() > 0);
  }

}
