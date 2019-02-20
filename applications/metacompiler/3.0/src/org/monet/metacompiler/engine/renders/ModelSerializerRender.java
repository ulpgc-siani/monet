package org.monet.metacompiler.engine.renders;

import java.util.HashMap;
import java.util.HashSet;

import org.monet.core.metamodel.MetaAttribute;
import org.monet.core.metamodel.MetaComposedEntity;
import org.monet.core.metamodel.MetaInclude;
import org.monet.core.metamodel.MetaProperty;
import org.monet.core.metamodel.MetaProperty.Content;
import org.monet.core.metamodel.MetaSyntagma;

public class ModelSerializerRender extends MetaModelRender {

  protected MetaSyntagma metasyntagma;

  public ModelSerializerRender() {
    super();
  }
  
  public void setMetaSyntagma(MetaSyntagma metasyntagma) {
    this.metasyntagma = metasyntagma;
  }
  
  @Override
  protected void init() {
    loadCanvas("model.serializer", true);
    
    addMark("typeComment", this.metasyntagma.getType());
    addMark("descriptionComment", this.metasyntagma.getDescription());
    addMark("hintComment", this.metasyntagma.getHint());
    
    addMark("className", this.metasyntagma.isExtensible() ? this.metasyntagma.getType() + "Base" : this.metasyntagma.getType());
    if(this.metasyntagma instanceof MetaComposedEntity && ((MetaComposedEntity) this.metasyntagma).getParent() != null) {
      MetaComposedEntity entity = (MetaComposedEntity) this.metasyntagma;
      
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("parentType", entity.getParent().getType());
      
      addMark("extends", block("extends", map));
    } else {
      addMark("extends", "");
    }
    
    MetaComposedEntity entity = (MetaComposedEntity) this.metasyntagma;
    addMark("abstract", entity.isAbstract() ? block("abstract", new HashMap<String, Object>()) : "");
    
    addMark("attributes", initAttributes(entity));
    addMark("properties", createProperties(entity));
    addMark("includes", createIncludes(entity));
    
    addMark("copyAttributes", createCopyAttributes(entity));
    addMark("copyProperties", createCopyProperties(entity));
    addMark("copyIncludes", createCopyIncludes(entity));

    createMerge(entity);
    addMark("mergeAttributes", createMergeAttributes(entity));
    addMark("mergeProperties", createMergeProperties(entity));
    addMark("mergeIncludes", createMergeIncludes(entity));
  }
  
  public String initAttributes(MetaComposedEntity entity) {
    StringBuilder blockBuilder = new StringBuilder();
    
    for(MetaAttribute attribute : entity.getAttributeList()) {
      if(attribute.getOwner() != entity || attribute.getType().startsWith("expression:"))
        continue;
      
      HashMap<String, Object> map = new HashMap<String, Object>();
      boolean isEnumeration = attribute.getType().equals("enumeration");
      String token = this.syntax.getToken(attribute.getToken(), true, false) + "Enumeration"; 
      
      //createAttributeClass
      if(isEnumeration) {
        map.put("token", token);
        map.put("values", this.syntax.getEnumeration(attribute.getValues(",")));
        blockBuilder.append(block("attribute$class", map));
      }
      
      //createAttribute
      map.clear();
      String attributeType = this.syntax.getAttributeType(attribute.getType());
      if(attributeType == null)
        continue;
      map.put("token", isEnumeration ? token : attributeType);
      map.put("name", this.syntax.getToken(attribute.getToken(), false, true));
      if(attribute.isMultiple()) {
        blockBuilder.append(block("attribute$member.multiple", map));
      } else {
        blockBuilder.append(block("attribute$member.simple", map));
      }
      
      //createAttributeMethod
      map.clear();
      map.put("name", this.syntax.getToken(attribute.getToken(), false, false));
      map.put("methodName", this.syntax.getToken(attribute.getToken(), true, false));
      map.put("memberName", this.syntax.getToken(attribute.getToken(), false, true));
      map.put("type", this.syntax.getAttributeType(attribute.getType()));
      
      String methodBlock;
      if(attribute.getType().equals("enumeration")) {
        if(attribute.isMultiple())
          methodBlock = "attribute$method.enumerationMultiple";
        else
          methodBlock = "attribute$method.enumeration";
      } else if(attribute.isMultiple()) {
        methodBlock = "attribute$method.multiple";
      } else {
        if(attribute.getType().equals("boolean"))
          methodBlock = "attribute$method.boolean";
        else
          methodBlock = "attribute$method.simple";
      }
      blockBuilder.append(block(methodBlock, map));
    }
    
    return blockBuilder.toString();
  }
  
  public String createProperties(MetaComposedEntity entity) {
    StringBuilder blockBuilder = new StringBuilder();
    
    for(MetaProperty property : entity.getPropertyList()) {
      if(property.getOwner() != entity || property.getToken().equals("schema"))
        continue;
      
      HashMap<String, Object> map = new HashMap<String, Object>();
      
      //createPropertyClass
      blockBuilder.append(this.createPropertyClass(property));
      
      //createProperty
      map.clear();
      String propertyClassName;
      if(property.isExtensible()) {
        propertyClassName = this.getFullClassName(property);
      } else {
        propertyClassName = this.syntax.getToken(property.getTypeOrToken(), true, false);
      }
      map.put("name", propertyClassName);
      map.put("memberName", this.syntax.getToken(property.getTypeOrToken(), false, true));
      
      String memberBlock;
      if(property.isMultiple()) {
        if(property.hasNameAttribute())
          memberBlock = "property$member.map";
        else
          memberBlock = "property$member.multiple";
      } else
        memberBlock = "property$member.simple";
      
      blockBuilder.append(block(memberBlock, map));
      
      //createPropertyMethod
      blockBuilder.append(this.createPropertyMethod(property));
    }
    
    return blockBuilder.toString();
  }
  
  private String createPropertyClass(MetaProperty property) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    Content content = property.getContent();
    StringBuilder includesBuilder = new StringBuilder();
    
    String propertyClassName = syntax.getToken(property.getTypeOrToken(), true, false);
    if(property.isExtensible())
      propertyClassName += "Base";
    map.put("propertyClassName", propertyClassName);
    map.put("parentName", property.getParent()!=null?property.getParentName():"");
    map.put("extends", property.getParent()!=null?block("property$class$extends", map):"");
    map.put("attributes", this.initAttributes(property));
    map.put("content", content != null?block("property$class$content", map):"");
    
    for (MetaInclude include : property.getIncludeList()) {
      if (include.getOwner() == property)
        includesBuilder.append(this.createInclude(include));
    }
    map.put("children", this.createProperties(property));
    map.put("includes", this.createIncludes(property));

    map.put("copyAttributes", this.createCopyAttributes(property));
    map.put("copyProperties", this.createCopyProperties(property));
    map.put("copyIncludes", this.createCopyIncludes(property));

    map.put("mergeAttributes", this.createMergeAttributes(property));
    map.put("mergeProperties", this.createMergeProperties(property));
    map.put("mergeIncludes", this.createMergeIncludes(property));
    
    return block("property$class", map);
  }
  
  private String createPropertyMethod(MetaProperty property) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String name = property.getTypeOrToken();
    String methodName = property.getToken();
    String pluralName = methodName + "s";
    boolean isMultiple = property.isMultiple();
    boolean isBoolean = property.isBoolean();
    boolean isAllowable = property.isAllowable();
    String method = "";
    
    String typeMap = name + "Map";
    String typeList = name + "List";
    String typeMethodMap = methodName + "Map";
    String typeMethodList = methodName + "List";

    String propertyClassName;
    if(property.isExtensible()) {
      propertyClassName = this.getFullClassName(property);
    } else {
      propertyClassName = this.syntax.getToken(name, true, false);
    }
    String parameters = propertyClassName + " value";
    
    map.put("key", property.getKey());
    map.put("tokenVariable", syntax.getToken(name,false,true));
    map.put("tokenClass", propertyClassName);
    map.put("tokenMethod", syntax.getTokenMethod(methodName, ""));
    map.put("tokenAddMethod", syntax.getTokenAddMethod(methodName, propertyClassName + " value"));
    map.put("tokenMethodMap", syntax.getTokenMethod(typeMethodMap,""));
    map.put("tokenMethodList", syntax.getTokenMethod(typeMethodList,""));
    map.put("tokenMethodKey", syntax.getTokenMethod(methodName,"String " + property.getKey()));
    map.put("tokenMethodPluralName", syntax.getTokenMethod(pluralName,""));
    map.put("tokenBooleanMethod", syntax.getToken(methodName,false,false));
    map.put("tokenSetMethod", syntax.getTokenSetMethod(methodName,"boolean value"));
    map.put("tokenSetMethodWithParameters", syntax.getTokenSetMethod(methodName,parameters));
    map.put("tokenTypeMap", syntax.getToken(typeMap,false,true));
    map.put("tokenTypeList", syntax.getToken(typeList,false,true));

    if (isBoolean)
      method = block("property$method.boolean", map);
    else if (isAllowable)
      method = block("property$method.allowable", map);
    else {
      if (isMultiple && property.hasNameAttribute() && property.hasCodeAttribute())
        method = block("property$method.multipleWithNameAndCode", map);
      else if (isMultiple && property.hasNameAttribute())
        method = block("property$method.multipleWithName", map);
      else if (isMultiple)
        if (property.getKey().isEmpty())
          method = block("property$method.multiple", map);
        else 
          method = block("property$method.multiple.key", map);
      else 
        method = block("property$method.other", map);
    }
    
    map.clear();
    map.put("method", method);
    
    return block("property$method", map);
  }
  
  public String createIncludes(MetaComposedEntity entity) {
    StringBuilder blockBuilder = new StringBuilder();
    
    for (MetaInclude include : entity.getIncludeList()) {
      if (include.getOwner() == entity) {
        blockBuilder.append(this.createInclude(include));
        blockBuilder.append(this.createIncludeMethod(include));
        blockBuilder.append(this.createIncludeListHelper(include));
        blockBuilder.append(this.createIncludeMapHelper(include));
      }
    }
    
    return blockBuilder.toString();
  }
  
  private String createInclude(MetaInclude include) {
    boolean isMultiple = include.isMultiple();
    HashSet<MetaComposedEntity> classList = include.getLinkTree();
    StringBuilder classListBuilder = new StringBuilder();
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    for (MetaComposedEntity clazz : classList) {
      if (clazz.isAbstract()) continue;
      
      String type = clazz.getType();
      String typeMap = type + "Map";
      String typeList = type + "List";
      
      map.put("tokenType", syntax.getToken(type,true,false));
      map.put("tokenTypeVariable", syntax.getToken(type,false,true));
      map.put("tokenTypeMap", syntax.getToken(typeMap,false,true));
      map.put("tokenTypeList", syntax.getToken(typeList,false,true));
      
      if (isMultiple && clazz.hasNameAttribute())
        classListBuilder.append(block("include$class.multiple.map", map));
      else if (isMultiple)
        classListBuilder.append(block("include$class.multiple.list", map));
      else
        classListBuilder.append(block("include$class.simple", map));
    }
    
    map.clear();
    map.put("classList", classListBuilder.toString());
    
    return block("include", map);
  }
  
  private String createIncludeMethod(MetaInclude include) {
    boolean isMultiple = include.isMultiple();
    HashSet<MetaComposedEntity> classList = include.getLinkTree();
    StringBuilder methodListBuilder = new StringBuilder();
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    for (MetaComposedEntity clazz : classList) {
      if (clazz.isAbstract()) continue;
      
      String type = clazz.getType();
      String typeMap = type + "Map";
      String typeList = type + "List";
      
      map.put("type", syntax.getToken(type,true,false));
      map.put("tokenType", syntax.getToken(type,true,false));
      map.put("tokenTypeVariable", syntax.getToken(type,false,true));
      map.put("tokenTypeMap", syntax.getToken(typeMap,false,true));
      map.put("tokenTypeList", syntax.getToken(typeList,false,true));
      map.put("tokenMethod", syntax.getTokenMethod(type,""));
      map.put("tokenMethodMap", syntax.getTokenMethod(typeMap,""));
      map.put("tokenMethodList", syntax.getTokenMethod(typeList,""));

      String parameters = block("includeMethod$class$parameters", map);
      map.put("tokenSetMethod", syntax.getTokenSetMethod(type,parameters));
      
      if (isMultiple && clazz.hasNameAttribute())
        methodListBuilder.append(block("includeMethod$class.multiple.map", map));
      else if (isMultiple)
        methodListBuilder.append(block("includeMethod$class.multiple.list", map));
      else
        methodListBuilder.append(block("includeMethod$class.simple", map));
    }
    
    map.clear();
    map.put("methodList", methodListBuilder.toString());
    
    return block("includeMethod", map);
  }

  private String createIncludeListHelper(MetaInclude include) {
    boolean isMultiple = include.isMultiple();
    HashSet<MetaComposedEntity> classList = include.getLinkTree();
    StringBuilder listHelperBuilder = new StringBuilder();

    if (classList.size() <= 1 || !isMultiple)
      return "";
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    String type = include.getProperty();

    map.put("helperName", syntax.getToken("All" + type + "List",false,true));
    map.put("helperClass", syntax.getToken("All" + type + "List",true,false));
    map.put("helperType", "ArrayList<" + type + ">");
    
    for (MetaComposedEntity item : classList) {
      if (item.isAbstract()) continue;
      HashMap<String, Object> localMap = new HashMap<String, Object>();
      localMap.put("helperName", map.get("helperName"));
      localMap.put("itemName", syntax.getToken(item.getType(),false,true));
      listHelperBuilder.append(block("includeListHelper$item", localMap));
    }
    map.put("listHelper", listHelperBuilder.toString());
    
    return block("includeListHelper", map);
  }
  
  private String createIncludeMapHelper(MetaInclude include) {
    boolean isMultiple = include.isMultiple();
    HashSet<MetaComposedEntity> classList = include.getLinkTree();

    if (classList.size() <= 1 || !isMultiple)
      return "";
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    String type = include.getProperty();

    map.put("type", type);
    map.put("helperName", syntax.getToken("All" + type + "Map",false,true));
    map.put("helperClass", syntax.getToken("All" + type + "Map",true,false));
    map.put("helperItem", syntax.getToken(type,true,false).replaceAll("Property$", ""));
    map.put("helperType", "LinkedHashMap<String," + type + ">");
    map.put("listName", syntax.getToken("All" + type + "List",false,true));
    map.put("listClass", syntax.getToken("All" + type + "List",true,false));
    
    return block("includeMapHelper", map);
  }

  public void createMerge(MetaComposedEntity entity) {
    if(entity.getParent() != null)
      addMark("mergeSuper", block("mergeSuper", new HashMap<String, Object>()));
    else
      addMark("mergeSuper", "");
  }

  public String createMergeAttributes(MetaComposedEntity entity) {
    StringBuilder blockBuilder = new StringBuilder();
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    for(MetaAttribute attribute : entity.getAttributeList()) {
      if (attribute.getOwner() != entity || attribute.getType().startsWith("expression:")) continue;
      
      String attributeType = this.syntax.getAttributeType(attribute.getType());
      if(attributeType == null)
        continue;
      
      map.put("name", this.getSyntax().getToken(attribute.getToken(), false, true));
      
      String asignation = block(attribute.isMultiple() ? "mergeAttributeMultipleAsignation" : "mergeAttributeSimpleAsignation", map);
      map.put("asignation", asignation);
      
      blockBuilder.append(block("mergeAttribute", map));
      blockBuilder.append("\n");
    }
    
    return blockBuilder.toString();
  }
  
  public String createMergeProperties(MetaComposedEntity entity) {
    StringBuilder blockBuilder = new StringBuilder();
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    for(MetaProperty property : entity.getPropertyList()) {
      if (property.getOwner() != entity || property.getToken().equals("schema")) continue;

      map.put("name", this.getSyntax().getToken(property.getTypeOrToken(), false, true));
      map.put("nameMethod", this.getSyntax().getToken(property.getToken(), true, false));
      map.put("nameType", property.isExtensible() ? this.getFullClassName(property) : syntax.getToken(property.getTypeOrToken(), true, false));
      
      String blockName;
      if(property.isMultiple() && property.hasNameAttribute()) {
        blockName = "mergePropertyMultipleWithNames";
      } else if(property.isMultiple()) {
        blockName = "mergePropertyMultiple";
      } else {
        blockName = "mergePropertySimple";
      }
      
      blockBuilder.append(block(blockName, map));
      blockBuilder.append("\n");
    }
    
    return blockBuilder.toString();
  }
  
  public String createMergeIncludes(MetaComposedEntity entity) {
    StringBuilder blockBuilder = new StringBuilder();
    
    for(MetaInclude include : entity.getIncludeList()) {
      if (include.getOwner() != entity) continue;

      HashSet<MetaComposedEntity> classList = include.getLinkTree();
      for(MetaComposedEntity clazz : classList) {
        if(!clazz.isAbstract()) {
          HashMap<String, Object> map = new HashMap<String, Object>();
          map.put("type", clazz.getType());
          map.put("typeMember", syntax.getToken(clazz.getType(), false, true));
          
          if(include.isMultiple() && clazz.hasNameAttribute()) {
            blockBuilder.append(block("mergeIncludeMultipleWithNames", map));
          } else if(include.isMultiple()) {
            blockBuilder.append(block("mergeIncludeMultiple", map));
          } else {
            blockBuilder.append(block("mergeIncludeSimple", map));
          }
        }
      }
    }
    
    return blockBuilder.toString();
  }
  
  public String createCopyAttributes(MetaComposedEntity entity) {
    StringBuilder blockBuilder = new StringBuilder();
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    for(MetaAttribute attribute : entity.getAttributeList()) {
      if (attribute.getType().startsWith("expression:")) continue;
      
      String attributeType = this.syntax.getAttributeType(attribute.getType());
      if(attributeType == null)
        continue;
      
      map.put("name", this.getSyntax().getToken(attribute.getToken(), false, true));
      
      String asignation = block(attribute.isMultiple() ? "copyAttributeMultipleAsignation" : "copyAttributeSimpleAsignation", map);
      map.put("asignation", asignation);
      
      blockBuilder.append(block("copyAttribute", map));
      blockBuilder.append("\n");
    }
    
    return blockBuilder.toString();
  }
  
  public String createCopyProperties(MetaComposedEntity entity) {
    StringBuilder blockBuilder = new StringBuilder();
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    for(MetaProperty property : entity.getPropertyList()) {
      if (property.getToken().equals("schema")) continue;

      map.put("name", this.getSyntax().getToken(property.getTypeOrToken(), false, true));
      map.put("nameMethod", this.getSyntax().getToken(property.getToken(), true, false));
      map.put("nameType", property.isExtensible() ? this.getFullClassName(property) : syntax.getToken(property.getTypeOrToken(), true, false));
      
      String blockName;
      if(property.isMultiple() && property.hasNameAttribute()) {
        blockName = "copyPropertyMultipleWithNames";
      } else if(property.isMultiple()) {
        blockName = "copyPropertyMultiple";
      } else {
        blockName = "copyPropertySimple";
      }
      
      blockBuilder.append(block(blockName, map));
      blockBuilder.append("\n");
    }
    
    return blockBuilder.toString();
  }
  
  public String createCopyIncludes(MetaComposedEntity entity) {
    StringBuilder blockBuilder = new StringBuilder();
    
    for(MetaInclude include : entity.getIncludeList()) {

      HashSet<MetaComposedEntity> classList = include.getLinkTree();
      for(MetaComposedEntity clazz : classList) {
        if(!clazz.isAbstract()) {
          HashMap<String, Object> map = new HashMap<String, Object>();
          map.put("type", clazz.getType());
          map.put("typeMember", syntax.getToken(clazz.getType(), false, true));
          
          if(include.isMultiple() && clazz.hasNameAttribute()) {
            blockBuilder.append(block("copyIncludeMultipleWithNames", map));
          } else if(include.isMultiple()) {
            blockBuilder.append(block("copyIncludeMultiple", map));
          } else {
            blockBuilder.append(block("copyIncludeSimple", map));
          }
        }
      }
    }
    
    return blockBuilder.toString();
  }
  
  private String getFullClassName(MetaComposedEntity entity) {
    StringBuilder builder = new StringBuilder();
    while(entity != null) {
      builder.insert(0, ".");
      builder.insert(0, this.syntax.getToken(entity.getTypeOrToken(), true, false));
      entity = entity.getOwner();
    }
    if(builder.length() > 0)
      builder.deleteCharAt(builder.length()-1);
    return builder.toString();
  }
  
}
