package org.monet.bpi.java;

import java.util.Map;

import org.monet.bpi.BehaviorTask;
import org.monet.bpi.MonetLink;
import org.monet.bpi.Node;
import org.monet.bpi.Task;
import org.monet.bpi.User;
import org.monet.bpi.types.Location;
import org.monet.metamodel.internal.Lock;

public abstract class TaskImpl extends BPIObject implements Task, BehaviorTask {
  
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

  @Override
  public void setUrgent(boolean value) {
  }

  @Override
  public boolean isUrgent() {
    return false;
  }

  @Override
  public void setComments(String comments) {
  }
  
  @Override
  public String getComments() {
    return null;
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
  public Map<String, String> getFlags() {
    return null;
  }
  
  @Override
  public String getFlag(String name) {
    return null;
  }

  @Override
  public boolean isFlagActive(String name) {
    return false;
  }

  @Override
  public void setFlag(String name, String value) {
  }

  @Override
  public void setFlag(String name, boolean value) {
  }

  @Override
  public void removeFlag(String name) {
  }
  
  /**
   *  Añade una entrada al historial
   * 
   *  @param title Titulo del evento
   *  @param text Segunda linea de contenido 
   */
  public void addLog(String title, String text) {
    
  }
  
  public void addLog(String title, String text, Iterable<MonetLink> links) {
    
  }

  @Override
  public void save() {
    
    
  }

  /**
   * Comienza o reanuda la ejecución de la tarea
   */
  @Override
  public void resume() {
    
    
  }
  
  protected void lock(Lock lock) {
    
  }
  
  protected void unlock(Lock lock) {
    
  }
  
  protected void _goto(String placeName, String historyText) {
    
  }
  
  public void free() {
    
  }
  
  public void abort() {
    
  }
  
  public void assignTo(User user, String reason) {
    
  }
  
  public Location getLocation() {
    return null;
  }
  
  public void setLocation(Location bpilocation) {
  }
  
  public boolean isFinished() {
    return false;
  }

  public boolean isAborted() {
    return false;
  }

  public MonetLink toMonetLink() {
    return null;
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
  public void doGoto(String place, String historyText) {    
  }

  @Override
  public String currentPlace() {
    return null;
  }
  
  @Override
  public void onAssign(User user) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onUnAssign() {
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
