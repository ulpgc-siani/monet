package org.monet.core.metamodel;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="include")
public class MetaInclude extends MetaSyntagma {
  public static class Option {
    private @Attribute String className;
    public String getClassName() {
      return className;
    }
  }
  private MetaClass owner;
  private @Attribute String name = "";
  private @Attribute(required = false) String tag = "";
  private @Attribute(name = "multiple" ,required = false) Boolean isMultiple = false;
  private @Attribute(name = "required", required = false) Boolean isRequired = false;
  private @Attribute(name = "class-name", required = false) String className = "IndexedDeclaration";
  private @Attribute(name = "key", required = false) String key = "";
  private @ElementList(inline = true, name="option") ArrayList<Option> optionList;
  private Boolean isAbstract = false;
  private Boolean isOverride = false;
  private ArrayList<MetaClass> linkList = new ArrayList<MetaClass>();

  public void setOwner(MetaClass owner) {
    this.owner = owner;
  }

  public void addLink(MetaClass link) {
    if (linkList.contains(link)) return;
    linkList.add(link);
  }

  public MetaClass getOwner() {
    return owner;
  }

  public String getName() {
    return name;
  }

  public String getTag() {
    return tag;
  }

  public Boolean isMultiple() {
    return isMultiple;
  }

  public Boolean isRequired() {
    return isRequired;
  }
  
  public Boolean isAbstract() {
    return isAbstract;
  }
  
  public Boolean isOverride() {
    return isOverride;
  }
  
  public String getClassName() {
    return className;
  }

  public String getKey() {
    return key;
  }

  public ArrayList<Option> getOptionList() {
    return optionList;
  }

  public ArrayList<MetaClass> getLinkList() {
    return linkList;
  }

  public ArrayList<MetaClass> getLinkTree() {
    ArrayList<MetaClass> linkTree = new ArrayList<MetaClass>();
    if (isAbstract) {
      linkTree.add(this.owner);     
    }
    else {
      for (MetaClass metaclass : linkList) {
        linkTree.add(metaclass);
        linkTree.addAll(metaclass.getChildrenTree());
      }
    }
    return linkTree;
  }

  public Boolean hasLinks() {
    return (linkList.size() > 0);
  }

  public void setAbstract(boolean value) {
    isAbstract = value;    
  }

  public void setOverride(boolean value) {
    isOverride = value;    
  }

}
