package org.monet.bpi;

public interface BehaviorNode {

  public void constructor();

  public void onOpened();

  public void onClosed();

  /**
   * Event called before node is being saved.
   * All changes made in this method will be saved automatically
   * Calling "save()" will throw an exception
   */
  public void onSaving();
  
  /**
   * Event called after node have been saved
   * Any change will not be saved automatically
   */
  public void onSaved();

  /**
   * Event called after node have been removed
   * Calling "save()" will throw an exception
   */
  public void onRemoved();

  public void executeCommand(String operation);

}