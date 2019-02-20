package org.monet.core.metamodel;

import java.util.ArrayList;
import java.util.HashMap;

import org.simpleframework.xml.Attribute;

public class MetaModel {
  private @Attribute String name;
  private @Attribute String version;
  private @Attribute String date;
  
  private ArrayList<MetaClass> metaclassList = new ArrayList<MetaClass>();
  private HashMap<String, MetaClass> metaclassMap = new HashMap<String, MetaClass>();

  public String getName() {
    return name;
  }
  public String getVersion() {
    return version;
  }
  public String getDate() {
    return date;
  }

  public void addMetaClass(MetaClass metaclass) {
    metaclassList.add(metaclass);
    metaclassMap.put(metaclass.getName(), metaclass);
  }

  public MetaClass getMetaClass(String name) {
    return metaclassMap.get(name);
  }

  public ArrayList<MetaClass> getMetaClassList() {
    return metaclassList;
  }

}
