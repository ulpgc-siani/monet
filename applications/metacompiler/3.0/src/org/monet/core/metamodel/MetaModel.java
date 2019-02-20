package org.monet.core.metamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.simpleframework.xml.Attribute;

public class MetaModel {
  private @Attribute
  String                                name;
  private @Attribute
  String                                version;
  private @Attribute
  String                                date;

  private ArrayList<MetaSyntagma>       metaRootList      = new ArrayList<MetaSyntagma>();
  private ArrayList<MetaClass>          metaclassList     = new ArrayList<MetaClass>();
  private HashMap<String, MetaClass>    metaclassMap      = new HashMap<String, MetaClass>();

  private ArrayList<MetaProperty>       metapropertyList  = new ArrayList<MetaProperty>();
  private HashMap<String, MetaProperty> metapropertyMap   = new HashMap<String, MetaProperty>();

  private HashSet<String>               propertyKeywords  = new HashSet<String>();
  private HashSet<String>               attributeKeywords = new HashSet<String>();
  private HashSet<String>               methodKeywords    = new HashSet<String>();

  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public String getVersionAsPackage() {
    return version.replace(".", "");
  }

  public String getDate() {
    return date;
  }

  public void addMetaProperty(MetaProperty metaproperty, boolean isRoot) {
    if (isRoot)
      this.metaRootList.add(metaproperty);
    
    this.metapropertyList.add(metaproperty);
    
    String propertyType = metaproperty.getType();
    if (propertyType != null && !propertyType.isEmpty()) {
      this.metapropertyMap.put(metaproperty.getType(), metaproperty);
    }

    if (metaproperty.getToken() != null && !metaproperty.getToken().isEmpty() && !metaproperty.getToken().equals("schema")) {
      this.propertyKeywords.add(metaproperty.getToken());
    }

    for (MetaAttribute metaattribute : metaproperty.getAttributeList()) {
      if (metaattribute.getToken() != null && !metaattribute.getToken().isEmpty() && !metaattribute.getType().equals("variable"))
        this.attributeKeywords.add(metaattribute.getToken());
    }

    for (MetaMethod metamethod : metaproperty.getMethodList()) {
      this.methodKeywords.add(metamethod.getName());
    }

    for (MetaProperty childProperty : metaproperty.getPropertyList()) {
      this.addMetaProperty(childProperty, false);
    }
  }

  public ArrayList<MetaProperty> getMetaPropertyList() {
    return metapropertyList;
  }

  public void addMetaClass(MetaClass metaclass) {
    this.metaRootList.add(metaclass);
    this.metaclassList.add(metaclass);
    this.metaclassMap.put(metaclass.getType(), metaclass);

    for (MetaAttribute metaattribute : metaclass.getAttributeList()) {
      if (metaattribute.getToken() != null && !metaattribute.getToken().isEmpty() && !metaattribute.getType().equals("variable"))
        this.attributeKeywords.add(metaattribute.getToken());
    }

    for (MetaMethod metamethod : metaclass.getMethodList()) {
      this.methodKeywords.add(metamethod.getName());
    }

    for (MetaProperty childProperty : metaclass.getPropertyList()) {
      this.addMetaProperty(childProperty, false);
    }
  }

  public MetaComposedEntity getMetaClassOrProperty(String name) {
    MetaComposedEntity classOrProperty = metaclassMap.get(name);
    if(classOrProperty == null) {
      classOrProperty = metapropertyMap.get(name);
    }
    return classOrProperty;
  }

  public ArrayList<MetaClass> getMetaClassList() {
    return metaclassList;
  }

  public ArrayList<MetaSyntagma> getMetaRootList() {
    return metaRootList;
  }

  public ArrayList<MetaClass> getMetaClassListDefinitionsNotAbstract() {
    ArrayList<MetaClass> result = new ArrayList<MetaClass>();
    for (MetaClass metaClass : this.metaclassList)
      if (metaClass.isDefinition() && !metaClass.isAbstract())
        result.add(metaClass);
    return result;
  }

  public HashSet<String> getAllMetaProperties() {
    return this.propertyKeywords;
  }

  public HashSet<String> getAllMetaMethods() {
    return this.methodKeywords;
  }

  public HashSet<String> getAllMetaAttributes() {
    return this.attributeKeywords;
  }

}
