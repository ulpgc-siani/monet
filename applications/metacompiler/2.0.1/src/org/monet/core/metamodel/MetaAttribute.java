package org.monet.core.metamodel;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="attribute")
public class MetaAttribute extends MetaSyntagma {
  
  public static class Enumeration {
    private @ElementList(inline = true, entry="option")
    ArrayList<String> optionList = new ArrayList<String>();

    public String getValues(String separator) {
      String result = "";
      for (String option : optionList) {
        result += separator + option;
      }
      if (result.isEmpty()) return result;
      return result.substring(1);
    }
  }
  
  private MetaClass owner;

  private @Attribute String name;
  private @Attribute String type;
  private @Attribute(required=false) String link;
  private @Attribute(required=false) String restriction;
  private @Element(required=false) Enumeration enumeration;
  private @Attribute(name = "required",required=false) Boolean isRequired;
  private @Attribute(name = "default",required=false) String defaultValue;

  public void setOwner(MetaClass owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getLink() {
    return link;
  }

  public String getRestriction() {
    return restriction;
  }

  public String getValues(String separator) {
    if (this.enumeration == null) return "";
    return this.enumeration.getValues(separator);
  }

  public boolean hasDefaultValue() {
    return defaultValue != null;
  }
  
  public String getDefaultValue() {
    return type.equalsIgnoreCase("string") ? "\"" + defaultValue + "\"" : defaultValue;
  }

  public MetaClass getOwner() {
    return owner;
  }

  public String getOwnerClassName() {
    if (owner == null) return "";
    return owner.getName();
  }

  public Boolean isRequired() {
    return isRequired;
  }

}
