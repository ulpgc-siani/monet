package org.monet.editor.dsl.metamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.monet.editor.dsl.helper.JavaHelper;

public class Item {

  public static final int               ATTRIBUTE  = 0;
  public static final int               PROPERTY   = 1;
  public static final int               METHOD     = 2;
  public static final int               PARAMETER  = 3;
  public static final int               DEFINITION = 4;
  public static final int               ENUMVALUE  = 5;

  protected LinkedHashMap<String, Item> childs     = new LinkedHashMap<String, Item>();

  private int                           type;
  private String                        name;
  private String                        token;
  private String                        valueTypeQualifiedName;
  private String                        refBaseType;
  private String                        description;
  private String                        hint;
  private boolean                       isRequired = false;
  private boolean                       isMultiple = false;
  private boolean                       hasName    = false;
  private boolean                       hasCode    = false;
  private boolean                       isNameRequired;
  private boolean                       isCodeRequired;
  private boolean                       isExportable;
  private Item                          parent;
  private String                        metaModelType;

  public Item(int type, String name, String metaModelType, String token, boolean isRequired, boolean isMultiple) {
    this(type, name, metaModelType, token, isRequired);
    this.isMultiple = isMultiple;
  }

  public Item(int type, String name, String metaModelType, String token, boolean isRequired) {
    this(type, name, metaModelType, token);
    this.isRequired = isRequired;
  }

  public Item(int type, String name, String metaModelType, String token) {
    this.type = type;
    this.name = name;
    this.token = token;
    this.metaModelType = metaModelType;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFullyQualifiedName() {
    StringBuilder builder = new StringBuilder();
    Item parentItem = this;

    while (parentItem != null) {
      builder.insert(0, ".");
      builder.insert(0, JavaHelper.toJavaIdentifier(parentItem.name));
      parentItem = parentItem.parent;
    }
    builder.delete(builder.length() - 1, builder.length());
    builder.insert(0, "org.monet.metamodel.");

    return builder.toString();
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public boolean isRequired() {
    return isRequired;
  }

  public void setRequired(boolean isRequired) {
    this.isRequired = isRequired;
  }

  public boolean isMultiple() {
    return isMultiple;
  }

  public void setMultiple(boolean isMultiple) {
    this.isMultiple = isMultiple;
  }

  public boolean hasName() {
    return hasName;
  }

  public boolean isNameRequired() {
    return this.isNameRequired;
  }

  public void setHasName(boolean hasName, boolean isRequired) {
    this.hasName = hasName;
    this.isNameRequired = isRequired;
  }

  public boolean hasCode() {
    return hasCode;
  }

  public boolean isCodeRequired() {
    return this.isCodeRequired;
  }

  public void setHasCode(boolean hasCode, boolean isRequired) {
    this.hasCode = hasCode;
    this.isCodeRequired = isRequired;
  }

  public void addChild(Item item) {
    this.childs.put(item.getToken(), item);
    item.setParent(this);
  }

  protected void setParent(Item parent) {
    this.parent = parent;
  }

  public Item getParent() {
    return this.parent;
  }

  public Item getChild(String tag) {
    return this.childs.get(tag);
  }

  public ArrayList<Item> getRequiredItems() {
    ArrayList<Item> requires = new ArrayList<Item>();
    for (Item item : this.childs.values())
      if (item.isRequired())
        requires.add(item);
    return requires;
  }

  public String getValueTypeQualifiedName() {
    return this.valueTypeQualifiedName;
  }

  public void setValueTypeQualifiedName(String valueTypeQualifiedName) {
    this.valueTypeQualifiedName = valueTypeQualifiedName;
  }

  public Collection<Item> getItems() {
    return this.childs.values();
  }

  public boolean isNodeDefinition() {
    return this.token.equals("catalog") || this.token.equals("collection") || this.token.equals("container") || this.token.equals("desktop") || this.token.equals("document") || this.token.equals("form");
  }

  public String getParentBehaviourClass() {
    if (this.token.equals("catalog") || this.token.equals("collection") || this.token.equals("container") || this.token.equals("desktop") || this.token.equals("document") || this.token.equals("form")) {
      return String.format("org.monet.bpi.java.Node%sImpl", JavaHelper.toJavaIdentifier(this.token));
    } else if (this.token.equals("sensor") || this.token.equals("service") || this.token.equals("activity") || this.token.equals("importer") || this.token.equals("exporter") || this.token.equals("dashboard") || this.token.equals("datastore") || this.token.equals("datastore-builder")) {
      return String.format("org.monet.bpi.java.%sImpl", JavaHelper.toJavaIdentifier(this.token));
    } else if (this.token.equals("thesaurus") || this.token.equals("glossary")) {
      return "org.monet.bpi.java.SourceImpl";
    } else if (this.token.equals("index")) {
      return "org.monet.bpi.java.IndexEntryImpl";
    }

    return null;
  }

  public String getParentDefinitionClass() {
    return String.format("org.monet.metamodel.%sDefinition", JavaHelper.toJavaIdentifier(this.token));
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String value) {
    this.description = value;
  }

  public String getHint() {
    return hint;
  }

  public void setHint(String hint) {
    this.hint = hint;
  }

  public boolean isExportable() {
    return isExportable;
  }

  public void setExportable(boolean isExportable) {
    this.isExportable = isExportable;
  }

  public String getRefBaseType() {
    return refBaseType;
  }

  public void setRefBaseType(String baseRefType) {
    this.refBaseType = baseRefType;
  }

  public String getMetaModelType() {
    return metaModelType;
  }

  public void setMetaModelType(String metaModelType) {
    this.metaModelType = metaModelType;
  }

}
