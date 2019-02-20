package org.monet.metacompiler.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import org.monet.core.metamodel.MetaClass;
import org.monet.core.metamodel.MetaComposedEntity;
import org.monet.core.metamodel.MetaInclude;
import org.monet.core.metamodel.MetaModel;
import org.monet.core.metamodel.MetaProperty;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.HyphenStyle;
import org.simpleframework.xml.stream.Style;

public class MetaModelLoader {
  private MetaModel metamodel;
  private Serializer serializer;

  public MetaModel load(String path) throws Exception {
    Style style = new HyphenStyle();
    Format format = new Format(style);   
    
    this.serializer = new Persister(format);
    loadIndex(new File(path+"/MANIFEST.xml"));
    loadFiles(new File(path));
    terminate();
    Collections.sort(metamodel.getMetaClassList(), MetaClass.NAME_ORDER);
    return this.metamodel;
  }

  private void loadIndex(File source) throws Exception {
    metamodel = serializer.read(MetaModel.class, source);
  }
  
  private void loadFiles(File source) throws Exception {
    for (File file : source.listFiles()) {
      if (file.isDirectory()) {
        loadFiles(file);
        continue;
      }
      
      String filename = file.getName();
      if (!filename.endsWith(".xml")) continue;
      if (filename.equals("MANIFEST.xml")) continue;
      System.out.println(filename);
      loadDefinition(file);
    }
  }

  private void loadDefinition(File source) throws Exception {
    String sourceContent = this.readFile(source);
    
    try {
      if(sourceContent.indexOf("<class ") > -1) {
        MetaClass metaclass = serializer.read(MetaClass.class, sourceContent);
        metamodel.addMetaClass(metaclass);
      } else {
        MetaProperty metaproperty = serializer.read(MetaProperty.class, sourceContent);
        metamodel.addMetaProperty(metaproperty, true);
      }
    } catch(Exception ex) {
      throw new Exception(source.getName() + ": " + ex.getMessage(), ex);
    }
    
  }
  
  private void terminate() {
    ArrayList<MetaClass> metaclassList = metamodel.getMetaClassList(); 
    ArrayList<MetaProperty> metapropertyList = metamodel.getMetaPropertyList(); 

    for (MetaProperty metaproperty : metapropertyList) {
      String parentName = metaproperty.getParentName();
      MetaComposedEntity parent = (parentName.isEmpty()) ? null : metamodel.getMetaClassOrProperty(parentName);
      if (!parentName.isEmpty() && parent == null) {
        System.out.printf("ERROR: %s declared in %s doesn't exist\n", parentName,metaproperty.getType());
      }
      metaproperty.setParent(parent);
      
      for (MetaInclude include : metaproperty.getIncludeList()) {
        createLink(metaproperty, include);
      }
    }
    
    for (MetaClass metaclass : metaclassList) {
      String parentName = metaclass.getParentName();
      MetaComposedEntity parent = (parentName.isEmpty()) ? null : metamodel.getMetaClassOrProperty(parentName);
      if (!parentName.isEmpty() && parent == null) {
        System.out.printf("ERROR: %s declared in %s doesn't exist\n", parentName,metaclass.getType());
      }
      metaclass.setParent(parent);
      
      for (MetaInclude include : metaclass.getIncludeList()) {
        createLink(metaclass, include);
      }
    }
    
    for (MetaProperty metaproperty : metapropertyList) {
      metaproperty.terminate();
    }
    for (MetaClass metaclass : metaclassList) {
      metaclass.terminate();
    }
  }
  
  private void createLink(MetaComposedEntity owner, MetaInclude include) {
    String className = include.getProperty();
    MetaComposedEntity metaentity = metamodel.getMetaClassOrProperty(className);
    if (metaentity == null) {
      System.out.printf("ERROR. Include option declaration %s: %s does not exist\n", owner.getType(), className);
      return;
    }
    expandLink(owner, metaentity, include);
    include.addLink(metaentity);
    metaentity.addLink(owner);
  }
  
  private void expandLink(MetaComposedEntity owner, MetaComposedEntity metaclass, MetaInclude include) {
    if(metaclass.isAbstract()) {
      for(MetaComposedEntity childMetaclass : metaclass.getChildrenTree()) {
        include.addLink(childMetaclass);
        childMetaclass.addLink(owner);
        expandLink(owner, childMetaclass, include);
      }
    }
  }
  
  public String readFile(File file) {
    StringBuffer oContent = new StringBuffer();
    InputStreamReader oInputStreamReader;
    BufferedReader oBufferedReader;
    String sLine;

    try {
      oInputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
      oBufferedReader = new BufferedReader(oInputStreamReader);
      while ((sLine = oBufferedReader.readLine()) != null) {
        oContent.append(sLine + "\r\n");
      }
      oInputStreamReader.close();
    } catch (IOException oException) {
      throw new RuntimeException("Could not read file " + file.getName(), oException);
    }

    return oContent.toString();
  }
  
}
