package org.monet.core.metamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "class")
public class MetaClass extends MetaSyntagma implements MetaComposedEntity {
  public static final Comparator<MetaClass> NAME_ORDER = new Comparator<MetaClass>() {
      public int compare(MetaClass o1, MetaClass o2) {
        return (o1.getType().compareTo(o2.getType()));
      }
  };
  
  public static final Comparator<MetaComposedEntity> LINK_NAME_ORDER = new Comparator<MetaComposedEntity>() {
    public int compare(MetaComposedEntity o1, MetaComposedEntity o2) {
      return o1.getType().compareTo(o2.getType());
    }
  };
 
  private @Attribute(name = "parent",required=false) String parentName = "";
  private @Attribute(required = false) String token = "";
  private @Attribute String type;
  private @ElementList(inline = true, required = false, name = "attribute") ArrayList<MetaAttribute> attributeList = new ArrayList<MetaAttribute>();
  private @ElementList(inline = true, required = false, name = "property") ArrayList<MetaProperty> propertyList = new ArrayList<MetaProperty>();
  private @ElementList(inline = true, required = false, name = "method") ArrayList<MetaMethod> methodList = new ArrayList<MetaMethod>();
  private @Element(required=false) String example;

  private MetaClass parent = null;
  private ArrayList<MetaClass> parentList = new ArrayList<MetaClass>();
  private ArrayList<MetaComposedEntity> childrenList = new ArrayList<MetaComposedEntity>();
  private ArrayList<MetaComposedEntity> linkList = new ArrayList<MetaComposedEntity>();
  private ArrayList<MetaClass> relationList = new ArrayList<MetaClass>();
  private Boolean terminated = false;

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
  
  public void setParent(MetaComposedEntity parent) {
    this.parent = (MetaClass)parent;
    init();
    
    if (parent == null) return;
    
    parent.addChildren(this);
    parentList.add((MetaClass)parent);
  } 
  
  public void terminate() {
    if (terminated) return;
    terminated = true;
    
    Collections.sort(linkList, LINK_NAME_ORDER);
    if (parent == null) return;
    if (!parent.isTerminated()) parent.terminate();
     
    parentList.addAll(parent.parentList);
    attributeList.addAll(parent.attributeList);
    propertyList.addAll(parent.propertyList);
    methodList.addAll(parent.methodList);
    includeList.addAll(parent.includeList);
    
//    HashMap<String, MetaInclude> includeMap = new HashMap<String,MetaInclude>(); 
//    for (MetaInclude include : includeList)
//      includeMap.put(include.getToken(), include);
//    
//    for (MetaInclude parentInclude : parent.includeList) {
//      String name = parentInclude.getToken();
//      MetaInclude include = includeMap.get(name);
//      if (include != null) {
//        parentInclude.setAbstract(true);
//        include.setOverride(true);
//      }
//      else {
//        includeList.add(parentInclude);
//      }
//    }
  }
  
  public void addLink(MetaComposedEntity link) {
    if (linkList.contains(link)) return;
    linkList.add(link);
  }
 
  public void addRelation(MetaClass relation) {
    if (relationList.contains(relation)) return;
    relationList.add(relation);
  }

  public String getParentName() {
    return parentName;
  }

  public MetaClass getParent() {
    return parent;
  }

  public String getType() {
    return type;
  }
  
  public String getToken() {
    return token;
  }
  
  public boolean isAbstract() {
    return token.isEmpty();
  }

  public ArrayList<MetaAttribute> getAttributeList() {
    return attributeList;
  }

  public ArrayList<MetaProperty> getPropertyList() {
    return propertyList;
  }
  
  public ArrayList<MetaMethod> getMethodList() {
    return methodList;
  }
  
  public ArrayList<MetaClass> getParentList() {
    return parentList;
  }

  public ArrayList<MetaComposedEntity> getChildrenList() {
    return childrenList;
  }

  public String getExample() {
    return example;
  }

  public ArrayList<MetaComposedEntity> getChildrenTree() {
    if (childrenList.size() == 0) return childrenList;

    ArrayList<MetaComposedEntity> childrenTree = new ArrayList<MetaComposedEntity>();
    for (MetaComposedEntity metaclass : childrenList) {
      childrenTree.add(metaclass);
      childrenTree.addAll(metaclass.getChildrenTree());
    }
    return childrenTree;
  }

  public ArrayList<MetaComposedEntity> getLinkList() {
    return linkList;
  }

  public ArrayList<MetaClass> getRelatedList() {
    return relationList;
  }

  public Boolean hasAttributes() {
    return (attributeList.size() > 0);
  }

  public Boolean hasProperties() {
    return (propertyList.size() > 0);
  }
  
  public Boolean hasMethods() {
    return (methodList.size() > 0);
  }

  public Boolean hasChildren() {
    return (childrenList.size() > 0);
  }

  public Boolean hasLinks() {
    return (linkList.size() > 0);
  }

  public Boolean hasRelatedClasses() {
    return (relationList.size() > 0);
  }
  
  public boolean isDefinition() {
    return this.type.indexOf("Definition") >= 0;
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
      if(attribute.getToken().equals("code"))
        return true;
    return false;
  }

  @Override
  public boolean isTerminated() {
    return this.terminated;
  }

  @Override
  public void addChildren(MetaComposedEntity metaClass) {
    this.childrenList.add((MetaClass)metaClass);
  }

  @Override
  public String getTypeOrToken() {
    return this.type;
  }

  @Override
  public MetaComposedEntity getOwner() {
    return null;
  }
  
}
