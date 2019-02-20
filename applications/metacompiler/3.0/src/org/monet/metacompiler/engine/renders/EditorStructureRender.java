package org.monet.metacompiler.engine.renders;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.monet.core.metamodel.MetaAttribute;
import org.monet.core.metamodel.MetaAttribute.Option;
import org.monet.core.metamodel.MetaClass;
import org.monet.core.metamodel.MetaComposedEntity;
import org.monet.core.metamodel.MetaInclude;
import org.monet.core.metamodel.MetaMethod;
import org.monet.core.metamodel.MetaMethod.Parameter;
import org.monet.core.metamodel.MetaProperty;
import org.monet.core.metamodel.MetaSyntagma;

public class EditorStructureRender extends EditorRender {

  public EditorStructureRender() {
    super();
  }

  @Override
  protected void init() {
    loadCanvas("editor.structure", true);

    this.initReferenciableProperties();
    this.initDeclarationContent();
    this.initDefinitions();
  }

  private void initDefinitions() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    StringBuilder definitionCalls = new StringBuilder();
    StringBuilder definitionMethods = new StringBuilder();
    int index = 0;

    for (MetaClass metaClass : metamodel.getMetaClassList()) {
      map.clear();

      map.put("type", metaClass.getType());
      map.put("parent", metaClass.getParentName());

      if (!metaClass.isAbstract()) {
        map.put("token", metaClass.getToken());
        map.put("description", getSyntax().escapeStringToJava(metaClass.getDescription()));
        map.put("hint", getSyntax().escapeStringToJava(metaClass.getHint()));

        String name = "";
        if (metaClass.hasNameAttribute()) {
          map.put("required", String.valueOf(metaClass.getNameAttribute().isRequired()));
          name = block("definition$concrete$name", map);
        }
        map.put("name", name);

        String code = "";
        if (metaClass.hasCodeAttribute()) {
          map.put("required", String.valueOf(metaClass.getCodeAttribute().isRequired()));
          code = block("definition$concrete$code", map);
        }
        map.put("code", code);

        map.put("attributes", this.processAttributes("definitionItem", metaClass.getAttributeList(), metaClass));
        map.put("properties", this.processProperties("definitionItem", metaClass.getPropertyList()));
        map.put("methods", this.processMethods("definitionItem", metaClass.getMethodList()));
        map.put("includes", this.processIncludes("definitionItem", metaClass));

        map.put("concrete", block("definition$concrete", map));
      } else {
        map.put("concrete", "");
      }
      
      map.put("index", index);

      definitionCalls.append(block("definitionCall", map));
      definitionMethods.append(block("definitionMethod", map));
      index++;
    }

    addMark("definitionCalls", definitionCalls.toString());
    addMark("definitionMethods", definitionMethods.toString());
  }

  private void initReferenciableProperties() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String result = "";

    for (MetaSyntagma metasyntagma : metamodel.getMetaRootList()) {
      if (!(metasyntagma instanceof MetaProperty))
        continue;

      MetaProperty metaProperty = (MetaProperty) metasyntagma;

      map.clear();

      map.put("type", metaProperty.getType());
      map.put("token", metaProperty.getToken());
      map.put("parent", metaProperty.getParentName());
      map.put("description", getSyntax().escapeStringToJava(metaProperty.getDescription()));
      map.put("hint", getSyntax().escapeStringToJava(metaProperty.getHint()));
      map.put("itemName", this.generateItemName());
      map.put("concrete", metaProperty.isAbstract() ? "" : block("referenciableProperty$concrete", map));
      result += block("referenciableProperty", map);
    }

    addMark("referenciableProperties", result);
  }

  private void initDeclarationContent() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String result = "";

    for (MetaSyntagma metasyntagma : metamodel.getMetaRootList()) {
      if (!(metasyntagma instanceof MetaProperty) || ((MetaProperty) metasyntagma).isAbstract())
        continue;

      MetaProperty metaProperty = (MetaProperty) metasyntagma;

      map.clear();

      map.put("type", metaProperty.getType());
      map.put("required", "");

      map.put("name", "");
      if (metaProperty.hasNameAttribute()) {
        map.put("required", String.valueOf(metaProperty.getNameAttribute().isRequired()));
        map.put("name", block("declarationContent$name", map));
      }
      
      map.put("code", "");
      if (metaProperty.hasCodeAttribute()) {
        map.put("required", String.valueOf(metaProperty.getCodeAttribute().isRequired()));
        map.put("code", block("declarationContent$code", map));
      }

      map.put("attributes", this.processAttributes("declarationItem", metaProperty.getAttributeList(), metaProperty));
      map.put("properties", this.processProperties("declarationItem", metaProperty.getPropertyList()));
      map.put("methods", this.processMethods("declarationItem", metaProperty.getMethodList()));
      map.put("includes", this.processIncludes("declarationItem", metaProperty));

      result += block("declarationContent", map);
    }

    addMark("declarationContents", result);
  }

  private String processAttributes(String parentItem, List<MetaAttribute> metaAttributeList, MetaSyntagma metaOwner) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String attributes = "";

    for (MetaAttribute metaAttribute : metaAttributeList) {
      if (metaAttribute.isSpecific())
        continue;

      String itemName = generateItemName();
      map.clear();

      map.put("token", metaAttribute.getToken());
      map.put("metamodeltype", metaAttribute.getType());
      map.put("required", String.valueOf(metaAttribute.isRequired()));
      map.put("multiple", String.valueOf(metaAttribute.isMultiple()));
      map.put("attributeGramaticType", getSyntax().getAttributeGramaticType(metaOwner, metaAttribute.getToken(), metaAttribute.getType(), metaAttribute.getLink()));
      map.put("description", getSyntax().escapeStringToJava(metaAttribute.getDescription()));
      map.put("hint", getSyntax().escapeStringToJava(metaAttribute.getHint()));
      map.put("itemName", itemName);

      String enumItems = "";
      for (Option option : metaAttribute.getEnumerationOptions()) {
        HashMap<String, Object> localMap = new HashMap<String, Object>();
        localMap.put("token", option.getToken());
        localMap.put("description", getSyntax().escapeStringToJava(option.getDescription()));
        localMap.put("hint", getSyntax().escapeStringToJava(option.getHint()));
        localMap.put("parentItem", itemName);
        localMap.put("enumItem", generateItemName());
        enumItems += block("attribute$enumItem", localMap);
      }

      map.put("enumItems", enumItems);
      map.put("parentItem", parentItem);

      attributes += block("attribute", map);
    }

    return attributes;
  }

  private String processProperties(String parentItem, List<MetaProperty> metaPropertyList) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String properties = "";

    for (MetaProperty metaProperty : metaPropertyList) {
      if(metaProperty.isSpecific())
        continue;
      
      String itemName = generateItemName();
      map.clear();

      map.put("itemName", itemName);
      map.put("typeOrToken", metaProperty.getTypeOrToken());
      map.put("token", metaProperty.getToken());
      map.put("required", String.valueOf(metaProperty.isRequired()));
      map.put("multiple", String.valueOf(metaProperty.isMultiple()));
      map.put("description", getSyntax().escapeStringToJava(metaProperty.getDescription()));
      map.put("hint", getSyntax().escapeStringToJava(metaProperty.getHint()));
      map.put("attributes", this.processAttributes(itemName, metaProperty.getAttributeList(), metaProperty));
      map.put("properties", this.processProperties(itemName, metaProperty.getPropertyList()));
      map.put("methods", this.processMethods(itemName, metaProperty.getMethodList()));
      map.put("includes", this.processIncludes(itemName, metaProperty));
      
      String baseType = metaProperty.getTypeOrParent();
      if(baseType == null || baseType.isEmpty()) {
        map.put("baseType", "");
      } else {
        map.put("baseType", baseType);
        map.put("baseType", block("property$baseType", map));
      }
      
      if(metaProperty.getType() != null && metaProperty.getParent() != null) {
        map.put("type", metaProperty.getType());
        map.put("parent", metaProperty.getParentName());
        map.put("clazzHierarchy", block("property$clazzHierarchy", map));
      } else {
        map.put("clazzHierarchy", "");
      }

      map.put("content", "");
      if (metaProperty.getContent() != null) {
        map.put("contentItemName", generateItemName());
        map.put("content", block("property$content", map));
      }
      
      map.put("name", "");
      if (metaProperty.hasNameAttribute()) {
        map.put("nameRequired", String.valueOf(metaProperty.getNameAttribute().isRequired()));
        map.put("name", block("property$name", map));
      }
      
      map.put("code", "");
      if (metaProperty.hasCodeAttribute()) {
        map.put("codeRequired", String.valueOf(metaProperty.getCodeAttribute().isRequired()));
        map.put("code", block("property$code", map));
      }

      map.put("parentItem", parentItem);

      properties += block("property", map);
    }

    return properties;
  }

  private String processMethods(String parentItem, List<MetaMethod> metaMethodList) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String methods = "";

    for (MetaMethod metaMethod : metaMethodList) {
      String itemName = generateItemName();
      map.clear();

      map.put("itemName", itemName);
      map.put("name", getSyntax().getToken(metaMethod.getName(), false, false));
      map.put("required", String.valueOf(metaMethod.isRequired()));
      map.put("description", getSyntax().escapeStringToJava(metaMethod.getDescription()));
      map.put("hint", getSyntax().escapeStringToJava(metaMethod.getHint()));
      map.put("parentItem", parentItem);

      String parameters = "";
      for (Parameter parameter : metaMethod.getParameterList()) {
        HashMap<String, Object> localMap = new HashMap<String, Object>();
        localMap.put("name", parameter.getName());
        localMap.put("type", parameter.getType());
        localMap.put("description", getSyntax().escapeStringToJava(parameter.getDescription()));
        localMap.put("hint", getSyntax().escapeStringToJava(parameter.getHint()));
        localMap.put("parentItem", itemName);
        localMap.put("itemName", generateItemName());
        parameters += block("method$parameter", localMap);
      }
      map.put("parameters", parameters);

      methods += block("method", map);
    }

    return methods;
  }

  private String processIncludes(String parentItem, MetaComposedEntity metaComposedEntity) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String includes = "";

    for (MetaInclude metaInclude : metaComposedEntity.getIncludeList()) {
      map.clear();

      String includeItems = "";
      for (MetaComposedEntity linkedMetaClass : metaInclude.getExpandedLinkTree()) {
        HashMap<String, Object> localMap = new HashMap<String, Object>();
        localMap.put("name", linkedMetaClass.getType());
        localMap.put("required", String.valueOf(metaInclude.isRequired()));
        localMap.put("multiple", String.valueOf(metaInclude.isMultiple()));
        localMap.put("itemName", generateItemName());
        localMap.put("parentItem", parentItem);
        includeItems += block("include$item", localMap);
      }
      map.put("items", includeItems);

      includes += block("include", map);
    }

    return includes;
  }

  protected String generateItemName() {
    return "_" + UUID.randomUUID().toString().replaceAll("-", "");
  }
}
