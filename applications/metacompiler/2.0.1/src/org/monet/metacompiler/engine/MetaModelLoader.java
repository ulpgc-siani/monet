package org.monet.metacompiler.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.monet.core.metamodel.MetaAttribute;
import org.monet.core.metamodel.MetaClass;
import org.monet.core.metamodel.MetaInclude;
import org.monet.core.metamodel.MetaInclude.Option;
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
    MetaClass metaclass = serializer.read(MetaClass.class, source);
    metamodel.addMetaClass(metaclass);
  }
  
  private void terminate() {
    ArrayList<MetaClass> metaclassList = metamodel.getMetaClassList(); 

    for (MetaClass metaclass : metaclassList) {
      String parentName = metaclass.getParentName();
      MetaClass parent = (parentName.isEmpty()) ? null : metamodel.getMetaClass(parentName);
      if ((parentName.isEmpty() == false) && (parent == null)) {
        System.out.printf("ERROR: %s declared in %s doesn't exist", parentName,metaclass.getName());
      }
      metaclass.setParent(parent);
      
      for (MetaAttribute attribute : metaclass.getAttributeList()) {
        if (attribute.getOwner() != metaclass) continue;
        createRelation(metaclass, attribute.getLink());
        createRelation(metaclass, attribute.getRestriction());
      }
      for (MetaProperty property : metaclass.getPropertyList()) {
        if (property.getOwner() != metaclass) continue;
        for (MetaAttribute attribute : property.getAttributeList()) {
          createRelation(metaclass, attribute.getLink());
          createRelation(metaclass, attribute.getRestriction());
        }
      }      
      for (MetaInclude include : metaclass.getIncludeList()) {
        if (include.getOwner() != metaclass) continue;
        createLink(metaclass, include);
      }
    }
    
    for (MetaClass metaclass : metaclassList) {
      metaclass.terminate();
    }
  }
  
  private void createLink(MetaClass owner, MetaInclude include) {
    for (Option option : include.getOptionList()) {
      String className = option.getClassName();
      MetaClass metaclass = metamodel.getMetaClass(className);
      if (metaclass == null) {
        System.out.printf("ERROR. Include option declaration %s: %s does not exist\n",owner.getName(), className);
        return;
      }
     include.addLink(metaclass);
     metaclass.addLink(owner);
    }
  }
  
  private void createRelation(MetaClass owner, String className) {
    if (className == null) return;
    if (className.isEmpty()) return;
    
    MetaClass metaclass = metamodel.getMetaClass(className);
    
    if (metaclass == null) {
      System.out.printf("ERROR. Attribute type declaration %s: %s does not exist\n",owner.getName(), className);
      return;
    }
    
    metaclass.addRelation(owner);    
  }
  

}
