package org.monet.core.metamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "class")
public class MetaClass extends MetaSyntagma {
  public static final Comparator<MetaClass> NAME_ORDER = new Comparator<MetaClass>() {
      public int compare(MetaClass o1, MetaClass o2) {
        return (o1.getName().compareTo(o2.getName()));
      }
  };
 
  private @Attribute String name;
  private @Attribute(name = "parent",required=false) String parentName = "";
  private @Attribute(required = false) String tag = "";
  private @Attribute(required = false) boolean extensible = false;
  private @ElementList(inline = true, required = false, name = "attribute") ArrayList<MetaAttribute> attributeList = new ArrayList<MetaAttribute>();
  private @ElementList(inline = true, required = false, name = "property") ArrayList<MetaProperty> propertyList = new ArrayList<MetaProperty>();
  private @ElementList(inline = true, required = false, name = "include") ArrayList<MetaInclude> includeList = new ArrayList<MetaInclude>();
  private @Element(required=false) String example;

  private MetaClass parent = null;
  private ArrayList<MetaClass> parentList = new ArrayList<MetaClass>();
  private ArrayList<MetaClass> childrenList = new ArrayList<MetaClass>();
  private ArrayList<MetaClass> linkList = new ArrayList<MetaClass>();
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
    Iterator<MetaInclude> includeIterator = includeList.iterator();
    while (includeIterator.hasNext()) {
      MetaInclude include = includeIterator.next();
      include.setOwner(this);
    }
  }
  
  public void setParent(MetaClass parent) {
    this.parent = parent;
    init();
    
    if (parent == null) return;
    
    parent.childrenList.add(this);
    parentList.add(parent);
  } 
  
  public void terminate() {
    if (terminated) return;
    terminated = true;
    
    Collections.sort(linkList, NAME_ORDER);
    if (parent == null) return;
    if (!parent.terminated) parent.terminate();
     
    parentList.addAll(parent.parentList);
    attributeList.addAll(parent.attributeList);
    propertyList.addAll(parent.propertyList);
    
    HashMap<String, MetaInclude> includeMap = new HashMap<String,MetaInclude>(); 
    for (MetaInclude include : includeList)
      includeMap.put(include.getName(), include);
    
    for (MetaInclude parentInclude : parent.includeList) {
      String name = parentInclude.getName();
      MetaInclude include = includeMap.get(name);
      if (include != null) {
        parentInclude.setAbstract(true);
        include.setOverride(true);
      }
      else {
        includeList.add(parentInclude);
      }
    }
  }
  
  public void addLink(MetaClass link) {
    if (linkList.contains(link)) return;
    linkList.add(link);
  }
 
  public void addRelation(MetaClass relation) {
    if (relationList.contains(relation)) return;
    relationList.add(relation);
  }
 

  public String getName() {
    return name;
  }

  public String getParentName() {
    return parentName;
  }

  public MetaClass getParent() {
    return parent;
  }

  public String getTag() {
    return tag;
  }

  public boolean isExtensible() {
    return this.extensible;
  }
  
  public Boolean isAbstract() {
    return tag.isEmpty();
  }

  public ArrayList<MetaAttribute> getAttributeList() {
    return attributeList;
  }

  public ArrayList<MetaProperty> getPropertyList() {
    return propertyList;
  }

  public ArrayList<MetaInclude> getIncludeList() {
    return includeList;
  }

  public ArrayList<MetaClass> getParentList() {
    return parentList;
  }

  public ArrayList<MetaClass> getChildrenList() {
    return childrenList;
  }

  public String getExample() {
    return example;
  }

  public ArrayList<MetaClass> getChildrenTree() {
    if (childrenList.size() == 0) return childrenList;

    ArrayList<MetaClass> childrenTree = new ArrayList<MetaClass>();
    for (MetaClass metaclass : childrenList) {
      childrenTree.add(metaclass);
      childrenTree.addAll(metaclass.getChildrenList());
    }
    return childrenTree;
  }

  public ArrayList<MetaClass> getLinkList() {
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

  public Boolean hasIncludes() {
    return (includeList.size() > 0);
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
    return this.name.indexOf("Definition") >= 0;
  }
  
}
