package org.monet.editor.dsl.metamodel;

import java.util.ArrayList;
import java.util.Collection;

public class ItemProxy extends Item {

  private Item base;
  
  public ItemProxy(Item base, boolean isRequired, boolean isMultiple) {
    super(base.getType(), base.getName(), base.getMetaModelType(), base.getToken(), isRequired, isMultiple);
    this.base = base;
  }
  
  @Override
  public void addChild(Item item) {
  }
  
  @Override
  public Item getChild(String token) {
    return base.getChild(token);
  }
  
  @Override
  public ArrayList<Item> getRequiredItems() {
    return base.getRequiredItems();
  }
  
  @Override
  public Collection<Item> getItems() {
    return base.getItems();
  }
  
  @Override
  public String getValueTypeQualifiedName() {
    return base.getValueTypeQualifiedName();
  }
  
  @Override
  public boolean hasCode() {
    return base.hasCode();
  }
  
  @Override
  public boolean hasName() {
    return base.hasName();
  }
  
  @Override
  public boolean isCodeRequired() {
    return base.isCodeRequired();
  }
  
  @Override
  public boolean isNameRequired() {
    return base.isNameRequired();
  }
  
  @Override
  public String getDescription() {
    return base.getDescription();
  }
  
  @Override
  public String getHint() {
    return base.getHint();
  }
  
  @Override
  public String getRefBaseType() {
    return base.getRefBaseType();
  }
  
}
