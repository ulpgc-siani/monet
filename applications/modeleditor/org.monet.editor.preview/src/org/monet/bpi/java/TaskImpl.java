package org.monet.bpi.java;

import org.monet.bpi.BehaviorTask;
import org.monet.bpi.Node;
import org.monet.bpi.Task;
import org.monet.bpi.types.Link;
import org.monet.metamodel.internal.Lock;

public abstract class TaskImpl
  extends BPIObject
  implements Task, BehaviorTask {
  
  @Override
  public String getId() {
    
    return null;
  }

  @Override
  public void setLabel(String label) {
    
    
  }
  
  @Override
  public String getLabel() {
    return null;
  } 

  @Override
  public void setDescription(String description) {
    
    
  }

  protected Node genericGetTarget() {
    
    return null;
  }

  protected void genericSetTarget(Node target) {
    
    
  }
  
  protected Node getShortCut(String name) {
    return null;
  }
  
  protected void setShortCut(String name, Node node) {
    
  }
  
  protected void removeShortCut(String name) {
  }

  @Override
  public String getContextVariable(String name) {
    
    return null;
  }

  @Override
  public void setContextVariable(String name, String value) {
    
    
  }

  @Override
  public void removeContextVariable(String name) {
    
    
  }
  
  public void addLog(String title, String text) {
    
  }
  
  public void addLog(String title, String text, Link link) {
    
  }

  @Override
  public void save() {
    
    
  }

  @Override
  public void resume() {
    
    
  }
  
  protected void unlock(Lock lock) {
    
  }
  
  protected void _goto(String placeName, String historyText) {
    
  }
  
  @Override
  public void onCreate() {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void onAbort() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onInitialize() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onTerminate() {
    // TODO Auto-generated method stub
    
  }

}
