package org.monet.core.metamodel;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "method")
public class MetaMethod extends MetaSyntagma {
  
  public static class Parameter extends MetaSyntagma {
    private @Attribute String name;
    private @Attribute String type;
    
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public String getType() {
      return type;
    }
    public void setType(String type) {
      this.type = type;
    }
  }
  
  private MetaComposedEntity owner;
  private @Attribute String name;
  private @Attribute(name = "required", required = false) Boolean isRequired = false;
  private @ElementList(inline = true, required = false, name = "parameter") ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
  
  public void setOwner(MetaComposedEntity owner) {
    this.owner = owner;
  }

  public MetaComposedEntity getOwner() {
    return owner;
  }

  public String getName() {
    return name;
  }

  public Boolean isRequired() {
    return isRequired;
  }
  
  public ArrayList<Parameter> getParameterList() {
    return parameterList;
  }
  
  public void setParameterList(ArrayList<Parameter> parameterList) {
    this.parameterList = parameterList;
  }

  @Override
  public String getType() {
    return null; //TODO: Return type?
  }

}
