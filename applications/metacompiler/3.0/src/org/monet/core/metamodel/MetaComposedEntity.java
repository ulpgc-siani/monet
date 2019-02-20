package org.monet.core.metamodel;

import java.util.ArrayList;
import java.util.Collection;

public interface MetaComposedEntity {

  boolean isTerminated();
  void setParent(MetaComposedEntity parent);
  MetaComposedEntity getParent();
  String getType();
  void addChildren(MetaComposedEntity composedEntity);
  void terminate();
  boolean isAbstract();
  Collection<MetaComposedEntity> getChildrenTree();
  void addLink(MetaComposedEntity owner);
  ArrayList<MetaAttribute> getAttributeList();
  ArrayList<MetaProperty> getPropertyList();
  ArrayList<MetaInclude> getIncludeList();
  boolean hasNameAttribute();
  String getTypeOrToken();
  MetaComposedEntity getOwner();
  
}
