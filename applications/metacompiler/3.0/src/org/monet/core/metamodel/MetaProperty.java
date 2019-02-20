package org.monet.core.metamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "property")
public class MetaProperty extends MetaSyntagma implements MetaComposedEntity {
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
  private MetaComposedEntity owner;
  private @Attribute(name = "parent",required=false) String parentName = "";
  private @Attribute(required=false) String token;
  private @Attribute(required=false) String type;
  private @Attribute(name = "multiple", required = false) Boolean isMultiple = false;
  private @Attribute(name = "required", required = false) Boolean isRequired = false;
  private @Attribute(name = "key", required = false) String key = "";
  private @Attribute(name = "value", required = false) String value = "";
  private @Attribute(name = "specific", required = false) Boolean specific = false;
  private @Element(name = "content", required = false)  Content content;
  private @ElementList(name = "attribute", required = false, inline = true) ArrayList<MetaAttribute> attributeList = new ArrayList<MetaAttribute>();
  private @ElementList(name = "property", required = false, inline = true) ArrayList<MetaProperty> propertyList = new ArrayList<MetaProperty>();
  private @ElementList(name = "method", required = false, inline = true) ArrayList<MetaMethod> methodList = new ArrayList<MetaMethod>();
  private ArrayList<MetaComposedEntity> childrenList = new ArrayList<MetaComposedEntity>();
  private ArrayList<MetaComposedEntity> parentList = new ArrayList<MetaComposedEntity>();
  private boolean isTerminated = false;
  private MetaProperty parent;

  public void setOwner(MetaComposedEntity owner) {
    this.owner = owner;
    Iterator<MetaProperty> propertyIterator = propertyList.iterator();
    while (propertyIterator.hasNext()) {
      MetaProperty property = propertyIterator.next();
      property.setOwner(this);
    }
  }

  public MetaComposedEntity getOwner() {
    return owner;
  }

  public String getToken() {
    return token;
  }
  
  public String getType() {
    return type;
  }
  
  public String getTypeOrToken() {
    return this.type != null ? this.type : this.token.startsWith("is-") ? this.token : this.token + "Property";
  }

  public void setType(String type) {
    this.type = type;
  }
  
  public String getParentName() {
    return parentName;
  }
  
  public void setParentName(String parentName) {
    this.parentName = parentName;
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

  public Boolean isBoolean() {
    return (token.indexOf("is-") >= 0) && this.attributeList.size() == 0;
  }
  
  public Boolean isAllowable() {
    return (token.indexOf("allow-") >= 0);
  }

  public Boolean isText() {
    return (content != null);
  }
  
  public boolean isAbstract() {
    return this.token == null || this.token.isEmpty();
  }

  public String getKey() {
    return key;
  }
  
  public String getValue() {
    return value;
  }

  public ArrayList<MetaProperty> getPropertyList() {
    return this.propertyList;
  }
  
  public ArrayList<MetaAttribute> getAttributeList() {
    return attributeList;
  }

  public Boolean hasAttributes() {
    return (attributeList.size() > 0);
  }
  
  public ArrayList<MetaMethod> getMethodList() {
    return methodList;
  }
  
  public Boolean hasMethods() {
    return (methodList.size() > 0);
  }
  
  public MetaAttribute getNameAttribute() {
    for(MetaAttribute attribute : this.attributeList)
      if(attribute.getToken().equals("name"))
        return attribute;
    return null;
  }
  
  public MetaAttribute getCodeAttribute() {
    for(MetaAttribute attribute : this.attributeList)
      if(attribute.getToken().equals("code"))
        return attribute;
    return null;
  }
  
  public boolean hasNameAttribute() {
    for(MetaAttribute attribute : this.attributeList)
      if(attribute.getToken().equals("name"))
        return true;
    return false;
  }
  
  public boolean hasCodeAttribute() {
    for(MetaAttribute attribute : this.attributeList)
      if(attribute.getToken().equals("code") && attribute.isSpecific())
        return true;
    return false;
  }

  private void init() {
    Iterator<MetaAttribute> attributeIterator = attributeList.iterator();
    while (attributeIterator.hasNext()) {
      MetaAttribute attribute = attributeIterator.next();
      attribute.setOwner(this);
    }
    Iterator<MetaProperty> propertyIterator = propertyList.iterator();
    while (propertyIterator.hasNext()) {
      MetaProperty property = propertyIterator.next();
      property.setOwner(this);
    }
    Iterator<MetaMethod> methodIterator = methodList.iterator();
    while (methodIterator.hasNext()) {
      MetaMethod method = methodIterator.next();
      method.setOwner(this);
    }
    Iterator<MetaInclude> includeIterator = includeList.iterator();
    while (includeIterator.hasNext()) {
      MetaInclude include = includeIterator.next();
      include.setOwner(this);
    }
  }
  
  @Override
  public void setParent(MetaComposedEntity parent) {
    this.parent = (MetaProperty)parent;
    init();
    
    if (parent == null) return;
    
    parent.addChildren(this);
    parentList.add(parent);
  }

  @Override
  public MetaComposedEntity getParent() {
    return this.parent;
  }
  
  @Override
  public boolean isTerminated() {
    return this.isTerminated;
  }

  @Override
  public void terminate() {
    if (isTerminated) return;
    isTerminated = true;
    
    if (parent == null) return;
    if (!parent.isTerminated()) parent.terminate();
     
    parentList.addAll(parent.parentList);
    attributeList.addAll(parent.attributeList);
    propertyList.addAll(parent.propertyList);
    methodList.addAll(parent.methodList);
    includeList.addAll(parent.includeList);
  }
  
  @Override
  public void addChildren(MetaComposedEntity composedEntity) {
    this.childrenList.add(composedEntity);
  }

  @Override
  public Collection<MetaComposedEntity> getChildrenTree() {
    if (childrenList.size() == 0) return childrenList;

    ArrayList<MetaComposedEntity> childrenTree = new ArrayList<MetaComposedEntity>();
    for (MetaComposedEntity metaclass : childrenList) {
      childrenTree.add(metaclass);
      childrenTree.addAll(metaclass.getChildrenTree());
    }
    return childrenTree;
  }

  @Override
  public void addLink(MetaComposedEntity link) {
  }
  
  public ArrayList<MetaComposedEntity> getParentList() {
    return parentList;
  }

  public String getTypeOrParent() {
    return (this.type == null || this.type.isEmpty()) ? this.parentName : this.type;
  }

  public boolean isSpecific() {
    return this.specific;
  }

}
