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
    ArrayList<Option> optionList = new ArrayList<Option>();

    public String getValues(String separator) {
      String result = "";
      for (Option option : optionList) {
        result += separator + option.getToken();
      }
      if (result.isEmpty()) return result;
      return result.substring(1);
    }
    
    public ArrayList<Option> getOptions() {
      return this.optionList;
    }
  }
  
  public static class Option extends MetaSyntagma {
    @Attribute(name = "token", required = true)
    private String token;
    
    public String getToken() {
      return this.token;
    }
    
    public String getType() { return null; }
  }
  
  private MetaComposedEntity owner;

  private @Attribute String token;
  private @Attribute(required=false) String type;
  private @Attribute(required=false) String link;
  private @Attribute(required=false) String restriction;
  private @Element(required=false) Enumeration enumeration;
  private @Attribute(name = "required",required=false) Boolean isRequired = false;
  private @Attribute(name = "default",required=false) String defaultValue;
  private @Attribute(name = "multiple", required = false) Boolean isMultiple = false;
  private @Attribute(name = "specific", required = false) Boolean specific = false;

  public void setOwner(MetaComposedEntity owner) {
    this.owner = owner;
  }

  public String getToken() {
    return token;
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

  public ArrayList<Option> getEnumerationOptions() {
    return this.enumeration != null ? this.enumeration.getOptions() : new ArrayList<MetaAttribute.Option>();
  }
  
  public String getValues(String separator) {
    if (this.enumeration == null) return "";
    return this.enumeration.getValues(separator);
  }

  public boolean hasDefaultValue() {
    return defaultValue != null;
  }
  
  public String getDefaultValue() {
    if (!this.hasDefaultValue()) return "";
    return type.equalsIgnoreCase("string") ? "\"" + defaultValue + "\"" : defaultValue;
  }

  public MetaComposedEntity getOwner() {
    return owner;
  }

  public String getOwnerClassName() {
    if (owner == null) return "";
    return owner.getType();
  }

  public boolean isRequired() {
    return isRequired;
  }

  public Boolean isMultiple() {
    return isMultiple;
  }

  public void setIsMultiple(Boolean isMultiple) {
    this.isMultiple = isMultiple;
  }

  public boolean isSpecific() {
    return specific;
  }

}
