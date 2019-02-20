package org.monet.core.metamodel;

import java.util.ArrayList;
import java.util.HashSet;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="include")
public class MetaInclude {
  private @Attribute(name = "property", required = true) String property;
  private @Attribute(name = "multiple", required = false) boolean isMultiple = false;
  private @Attribute(name = "required", required = false) boolean isRequired = false;
  private ArrayList<MetaComposedEntity> linkList = new ArrayList<MetaComposedEntity>();
  private MetaComposedEntity owner;

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public void addLink(MetaComposedEntity metasyntagma) {
    if (linkList.contains(metasyntagma)) return;
    linkList.add(metasyntagma);
  }
  
  public ArrayList<MetaComposedEntity> getLinkList() {
    return linkList;
  }
  
  public MetaComposedEntity getOwner() {
    return this.owner;
  }
  
  public void setOwner(MetaComposedEntity owner) {
    this.owner = owner;
  }
  
  public boolean isMultiple() {
    return this.isMultiple;
  }
  
  public boolean isRequired() {
    return this.isRequired;
  }
  
  public ArrayList<MetaComposedEntity> getLinkTreeNotAbstract() {
    ArrayList<MetaComposedEntity> linkTree = new ArrayList<MetaComposedEntity>();
    for (MetaComposedEntity metaclass : getLinkTree())
      if(!metaclass.isAbstract())
        linkTree.add(metaclass);
    return linkTree;
  }
  
  public HashSet<MetaComposedEntity> getLinkTree() {
    HashSet<MetaComposedEntity> linkTree = new HashSet<MetaComposedEntity>();
    for (MetaComposedEntity metaclass : linkList) {
      linkTree.add(metaclass);
      linkTree.addAll(metaclass.getChildrenTree());
    }
    return linkTree;
  }
  
  public Iterable<MetaComposedEntity> getExpandedLinkTree() {
    HashSet<MetaComposedEntity> expandedLinkTree = new HashSet<MetaComposedEntity>();
    getLinkTreeRecursive(expandedLinkTree, this.getLinkTree());
    return expandedLinkTree;
  }

  protected void getLinkTreeRecursive(HashSet<MetaComposedEntity> expandedLinkTree, Iterable<MetaComposedEntity> childs) {
    for(MetaComposedEntity child : childs) {
      if(child.isAbstract()) {
        this.getLinkTreeRecursive(expandedLinkTree, child.getChildrenTree());
      } else {
        expandedLinkTree.add(child);
      }
    }
  }

  public Boolean hasLinks() {
    return (linkList.size() > 0);
  }
  
}
