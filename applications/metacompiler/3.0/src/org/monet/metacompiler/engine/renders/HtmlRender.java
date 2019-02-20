package org.monet.metacompiler.engine.renders;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.monet.core.metamodel.MetaAttribute;
import org.monet.core.metamodel.MetaClass;
import org.monet.core.metamodel.MetaComposedEntity;
import org.monet.core.metamodel.MetaInclude;
import org.monet.core.metamodel.MetaProperty;
import org.monet.core.metamodel.MetaProperty.Content;
import org.monet.core.metamodel.MetaSyntagma;

public class HtmlRender extends MetaModelRender {

  protected MetaSyntagma metasyntagma;

  public HtmlRender() {
    super();
  }
  
  public void setMetaSyntagma(MetaSyntagma metasyntagma) {
    this.metasyntagma = metasyntagma;
  }
  
  @Override
  protected void init() {
    loadCanvas("html");
    
    addMark("name", metamodel.getName());
    addMark("version", metamodel.getVersion());
    addMark("date", metamodel.getDate());
    addMark("definitionMetaClass", this.processMetaClass((MetaClass)metamodel.getMetaClassOrProperty("Definition")));
    
    String metaClasses = "";
    for (MetaClass metaClass : metamodel.getMetaClassList()) {
      if (metaClass.getType() != "Definition")
        metaClasses += this.processMetaClass(metaClass);
    }
    addMark("metaClasses", metaClasses);
  }

  private String processMetaClass(MetaClass metaClass) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    map.put("name", metaClass.getType());
    map.put("stereotype", this.calculateStereotype(metaClass.isAbstract(), "abstract", "&lt;" + metaClass.getToken() + "&gt;"));
    map.put("label", this.showLabel(metaClass));
    map.put("description", metaClass.getDescription());
    map.put("hint", metaClass.getHint());
    map.put("attributes", this.showAttributes(metaClass));
    map.put("properties", this.showProperties(metaClass));
    map.put("includes", this.showIncludes(metaClass));
    map.put("parents", this.showParents(metaClass));
    map.put("children", this.showChildren(metaClass));
    map.put("linkClasses", this.showLinkClasses(metaClass));
    map.put("relatedClasses", this.showRelatedClasses(metaClass));
    
    return block("metaClass", map);
  }

  private String calculateStereotype(boolean value, String mainStereotype, String altStereotype) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String content = "";
    
    map.put("main", mainStereotype);
    map.put("alt", altStereotype);
    
    if (value)
      content = block("stereotype.main", map);
    else
      if (altStereotype != null)
        content = block("stereotype.alt", map);
    
    map.clear();
    map.put("content", content);
        
    return block("stereotype", map);
  }

  private String showLabel(MetaComposedEntity metaEntity) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String label = "";
    
    map.put("label", metaEntity.getType());
    
    if (metaEntity.isAbstract())
      label = block("label.abstract", map);
    else
      label = block("label", map);
    
    return label;
  }

  private String showAttributes(MetaComposedEntity metaEntity) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String items = "";
    List<MetaAttribute> metaAttributeList = metaEntity.getAttributeList();
    
    if (metaAttributeList.size() <= 0)
      return "";
    
    for (MetaAttribute metaAttribute : metaAttributeList)
      items += this.showAttribute(metaEntity, metaAttribute);
    
    map.put("items", items);
    
    return block("attributes", map);
  }
  
  private String showAttribute(MetaComposedEntity metaEntity, MetaAttribute metaAttribute) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String type = metaAttribute.getType();
    String values = metaAttribute.getValues(",");
    String link = metaAttribute.getLink();
    String restriction = metaAttribute.getRestriction();
    String relation = "";
    
    if (!values.isEmpty())
      type = "(" + values + ")";
    
    map.put("type", type);
    map.put("name", metaAttribute.getToken());
    map.put("value", metaAttribute.getDefaultValue());
    
    if (link != null && !link.isEmpty()) {
      HashMap<String, Object> localMap = new HashMap<String, Object>();
      localMap.put("link", link);
      localMap.put("restriction", restriction);
      relation = block("attribute$relation", localMap);
    }
    map.put("relation", relation);
    
    map.put("stereotype", this.calculateStereotype(metaAttribute.isRequired(), "required", "optional"));
    map.put("description", metaAttribute.getDescription());
    map.put("hint", metaAttribute.getHint());
    
    MetaComposedEntity metaOwner = metaAttribute.getOwner();
    String owner = "";
    if (metaOwner != null && metaOwner != metaEntity)
      owner = metaOwner.getType();
    map.put("owner", owner);
    
    return block("attribute", map);
  }

  private String showProperties(MetaComposedEntity metaEntity) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String items = "";
    List<MetaProperty> metaPropertyList = metaEntity.getPropertyList();
    
    if (metaPropertyList.size() <= 0)
      return "";
    
    for (MetaProperty metaProperty : metaPropertyList)
      items += this.showProperty(metaEntity, metaProperty);
    
    map.put("items", items);
    
    return block("properties", map);
  }
  
  private String showProperty(MetaComposedEntity metaEntity, MetaProperty metaProperty) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    map.put("type", metaProperty.getType());
    map.put("multipleStereotype", this.calculateStereotype(metaProperty.isMultiple(), "multiple", null));
    map.put("requiredStereotype", this.calculateStereotype(metaProperty.isRequired(), "required", "optional"));
    map.put("description", metaProperty.getDescription());
    map.put("hint", metaProperty.getHint());
    
    String attributes = "";
    for (MetaAttribute metaAttribute : metaProperty.getAttributeList())
      attributes += this.showAttribute(metaProperty, metaAttribute);
    map.put("attributes", attributes);
    
    Content content = metaProperty.getContent();
    map.put("contentType", (content!=null)?content.getType().name():"");
    
    MetaComposedEntity metaOwner = metaProperty.getOwner();
    String owner = "";
    if (metaOwner != null && metaOwner != metaEntity)
      owner = metaOwner.getType();
    map.put("owner", owner);

    return block("property", map);
  }

  private String showIncludes(MetaComposedEntity metaEntity) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String items = "";
    List<MetaInclude> metaIncludeList = metaEntity.getIncludeList();
    
    if (metaIncludeList.size() <= 0)
      return "";
    
    for (MetaInclude metaInclude : metaIncludeList)
      items += this.showInclude(metaEntity, metaInclude);
    
    map.put("items", items);
    
    return block("includes", map);
  }

  private String showInclude(MetaComposedEntity metaEntity, MetaInclude metaInclude) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    map.put("multipleStereotype", this.calculateStereotype(metaInclude.isMultiple(), "multiple", null));
    map.put("requiredStereotype", this.calculateStereotype(false/*metaInclude.isRequired()*/, "required", "optional"));
    map.put("classTree", this.showClassTree(metaInclude.getLinkList()));
    
    MetaComposedEntity metaOwner = metaInclude.getOwner();
    String owner = "";
    if (metaOwner != null && metaOwner != metaEntity)
      owner = metaOwner.getType();
    map.put("owner", owner);

    return block("include", map);
  }

  private String showParents(MetaClass metaClass) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    List<MetaClass> metaParentList = metaClass.getParentList();
    
    if (metaParentList.size() <= 0)
      return "";
    
    map.put("classList", this.showClassList(metaParentList));
    
    return block("parents", map);
  }

  private String showChildren(MetaClass metaClass) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    List<MetaComposedEntity> metaChildrenList = metaClass.getChildrenList();
    
    if (metaChildrenList.size() <= 0)
      return "";
    
    map.put("classTree", this.showClassTree(metaChildrenList));
    
    return block("children", map);
  }

  private String showLinkClasses(MetaClass metaClass) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    List<MetaComposedEntity> metaLinkList = metaClass.getLinkList();
    
    if (metaLinkList.size() <= 0)
      return "";
    
    map.put("classTree", this.showClassTree(metaLinkList));
    
    return block("linkClasses", map);
  }

  private String showRelatedClasses(MetaClass metaClass) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    List<MetaClass> metaRelatedList = metaClass.getRelatedList();
    
    if (metaRelatedList.size() <= 0)
      return "";
    
    map.put("classList", this.showClassList(metaRelatedList));
    
    return block("relatedClasses", map);
  }

  private String showClassTree(Collection<MetaComposedEntity> metaLinkCollection) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String links = "";
    
    if (metaLinkCollection.size() <= 0)
      return "";
    
    for (MetaComposedEntity metaLink : metaLinkCollection) {
      HashMap<String, Object> localMap = new HashMap<String, Object>();
      
      localMap.put("type", metaLink.getType());
      localMap.put("label", this.showLabel(metaLink));
      localMap.put("children", this.showClassTree(metaLink.getChildrenTree()));
      
      links += block("classTree.link", localMap);
    }
   
    map.put("links", links);
    
    return block("classTree", map);
  }

  private String showClassList(List<MetaClass> metaClassList) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String items = "";
    
    if (metaClassList.size() <= 0)
      return "";
    
    for (MetaClass metaClass : metaClassList) {
      HashMap<String, Object> localMap = new HashMap<String, Object>();
      
      localMap.put("type", metaClass.getType());
      localMap.put("label", this.showLabel(metaClass));
      
      items += block("classList.item", localMap);
    }
   
    map.put("items", items);
    
    return block("classList", map);
  }

}
