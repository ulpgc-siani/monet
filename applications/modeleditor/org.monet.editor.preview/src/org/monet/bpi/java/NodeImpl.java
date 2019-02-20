package org.monet.bpi.java;

import org.monet.bpi.BehaviorNode;
import org.monet.bpi.IndexEntry;
import org.monet.bpi.Node;
import org.monet.bpi.types.Link;

public abstract class NodeImpl 
  implements Node, BehaviorNode {
  
  @Override
  public String getOwnerId() {
    return null;
  }

  @Override
  public Node getOwner() {
    return null;
  }

  @Override
  public void setOwner(Node parent) {
    
  }

  @Override
  public boolean isPrototype() {
    return true;
  }
  
  @Override
  public String getLabel() {
    return null;
  }
  
  @Override
  public void setLabel(String label) {
    
  }

  protected IndexEntry getIndexEntry(String code) {
    return null;
  }

  @Override
  public String getNote(String code) {
    return null;
  }

  @Override
  public void addNote(String code, String value) {
  }

  @Override
  public void deleteNote(String code) {
  }

  @Override
  public void save() {
  }

  @Override
  public void saveNotes() {
  }

  @Override
  public void lock() {
  }

  @Override
  public void unLock() {
  }
  
  public Link toLink() {
    return null;
  }
  
  public void transform() {
  }
  
  protected void resetFlags(String ruleCode) {
  }
  
  protected void setFlag(String ruleCode, org.monet.metamodel.NodeDefinitionBase.RuleNodeProperty.AddFlagEnumeration flag) { }
  protected void setFlag(String ruleCode, org.monet.metamodel.NodeDefinitionBase.RuleViewProperty.AddFlagEnumeration flag) { }
  protected void setFlag(String ruleCode, org.monet.metamodel.NodeDefinitionBase.RuleOperationProperty.AddFlagEnumeration flag) { }
  protected void setFlag(String ruleCode, org.monet.metamodel.FormDefinitionBase.RuleFormProperty.AddFlagEnumeration flag) { }
  
  public void evaluateRules() {
  }
  
  public Node clone(Node parent) {
    return null;
  }

  public void merge(Node source) {
    
  }

  @Override
  public void constructor() {
  }

  @Override
  public void onOpened() {
  }

  @Override
  public void onClosed() {
  }

  @Override
  public void onSaving() {
  }

  @Override
  public void onSaved() {
  }

  @Override
  public void onRemoved() {
  }

  @Override
  public void executeCommand(String operation) {
  }

}
