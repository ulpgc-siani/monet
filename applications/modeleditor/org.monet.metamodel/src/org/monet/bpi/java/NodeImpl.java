package org.monet.bpi.java;

import java.util.List;
import java.util.Map;

import org.monet.bpi.BehaviorNode;
import org.monet.bpi.IndexEntry;
import org.monet.bpi.MonetLink;
import org.monet.bpi.Node;
import org.monet.bpi.Task;
import org.monet.bpi.User;
import org.monet.bpi.types.Date;
import org.monet.bpi.types.Link;

public abstract class NodeImpl implements Node, BehaviorNode {

  @Override
  public User getAuthor() {
    return null;
  }

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
  public void addPermission(String userId) {
    // TODO Auto-generated method stub

  }

  @Override
  public void addPermission(User user) {
    // TODO Auto-generated method stub

  }

  @Override
  public void addPermission(String userId, Date beginDate) {
    // TODO Auto-generated method stub

  }

  @Override
  public void addPermission(User user, Date beginDate) {
    // TODO Auto-generated method stub

  }

  @Override
  public void addPermission(String userId, Date beginDate, Date endDate) {
    // TODO Auto-generated method stub

  }

  @Override
  public void addPermission(User user, Date beginDate, Date endDate) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean hasPermission(String userId) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean hasPermission(User user) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void deletePermission(String userId) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deletePermission(User user) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isPrototype() {
    return true;
  }

  @Override
  public Node getPrototypeNode() {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public boolean isGeoReferenced() {
    return true;
  }

  @Override
  public String getCode() {
    return null;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getLabel() {
    return null;
  }

  @Override
  public void setLabel(String label) {

  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public void setDescription(String description) {

  }

  @Override
  public String getColor() {
    return null;
  }

  @Override
  public void setColor(String color) {

  }

  protected IndexEntry getIndexEntry(String code) {
    return null;
  }

  @Override
  public Map<String, String> getFlags() {
    return null;
  }

  @Override
  public String getFlag(String name) {
    return null;
  }

  @Override
  public void setFlag(String name, String value) {
  }

  @Override
  public void removeFlag(String name) {
  }

  @Override
  public String getNote(String name) {
    return null;
  }

  @Override
  public void setNote(String name, String value) {
  }

  @Override
  public void removeNote(String name) {
  }

  @Override
  public void save() {
  }

  @Override
  public void lock() {
  }

  @Override
  public void unLock() {
  }

  @Override
  public boolean isLocked() {
    return false;
  }

  public String getPartnerContext() {
    return null;
  }

  public void setPartnerContext(String context) {
  }

  @Override
  public void setEditable(boolean value) {
  }

  @Override
  public boolean isEditable() {
    return false;
  }

  @Override
  public void setDeletable(boolean value) {
  }

  @Override
  public boolean isDeletable() {
    return false;
  }

  public Link toLink() {
    return null;
  }

  public MonetLink toMonetLink() {
    return null;
  }

  public MonetLink toMonetLink(boolean editMode) {
    return null;
  }

  public void transform() {
  }

  protected void resetFlags(String ruleCode) {
  }

  protected void setFlag(String ruleCode, org.monet.metamodel.NodeDefinitionBase.RuleNodeProperty.AddFlagEnumeration flag) {
  }

  protected void setFlag(String ruleCode, org.monet.metamodel.NodeDefinitionBase.RuleViewProperty.AddFlagEnumeration flag) {
  }

  protected void setFlag(String ruleCode, org.monet.metamodel.DesktopDefinitionBase.RuleLinkProperty.AddFlagEnumeration flag) {
  }

  protected void setFlag(String ruleCode, org.monet.metamodel.NodeDefinitionBase.RuleOperationProperty.AddFlagEnumeration flag) {
  }

  protected void setFlag(String ruleCode, org.monet.metamodel.FormDefinitionBase.RuleFormProperty.AddFlagEnumeration flag) {
  }

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
  public void onSetContext() {
  }

  @Override
  public void onSave() {
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

  @Override
  public boolean executeCommandConfirmationWhen(String operation) {
    return false;
  }

  @Override
  public void executeCommandConfirmationOnCancel(String operation) {
  }

  @Override
  public List<Node> getLinksIn() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Node> getLinksOut() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Task> getLinkedTasks() {
    // TODO Auto-generated method stub
    return null;
  }

}
