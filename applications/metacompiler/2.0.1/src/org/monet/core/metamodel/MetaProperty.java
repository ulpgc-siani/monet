package org.monet.core.metamodel;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "property")
public class MetaProperty extends MetaSyntagma {
  public static enum ContentType { xml, text };
  public static class Content {
    private @Attribute ContentType type;
    public ContentType getType() {
      return type;
    }
  }
  public static class Data {
    private @Attribute ContentType type;
    public ContentType getType() {
      return type;
    }
  }
  private MetaClass owner;
  private @Attribute String name;
  private @Attribute(name = "multiple", required = false) Boolean isMultiple = false;
  private @Attribute(name = "required", required = false) Boolean isRequired = false;
  private @Attribute(name = "editable", required = false) Boolean isEditable = true;
  private @Attribute(name = "key", required = false) String key = "";
  private @Attribute(name = "value", required = false) String value = "";
  private @Element(name = "content", required = false)  Content content;
  private @ElementList(name = "attribute", required = false, inline = true) ArrayList<MetaAttribute> attributeList = new ArrayList<MetaAttribute>();

  public void setOwner(MetaClass owner) {
    this.owner = owner;
  }

  public MetaClass getOwner() {
    return owner;
  }

  public String getName() {
    return name;
  }

  public Content getContent() {
    return content;
  }
  
  public Boolean isXmlContentType() {
    return content != null && content.getType() == ContentType.xml;
  }

  public Boolean isMultiple() {
    return isMultiple;
  }

  public Boolean isRequired() {
    return isRequired;
  }

  public Boolean isEditable() {
    return isEditable;
  }

  public Boolean isBoolean() {
    return (name.indexOf("is-") >= 0);
  }
  
  public Boolean isAllowable() {
    return (name.indexOf("allow-") >= 0);
  }

  public Boolean isText() {
    return (content != null);
  }

  public String getKey() {
    return key;
  }
  
  public String getValue() {
    return value;
  }

  public ArrayList<MetaAttribute> getAttributeList() {
    return attributeList;
  }

  public Boolean hasAttributes() {
    return (attributeList.size() > 0);
  }

}
