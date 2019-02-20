package org.monet.bpi;

import org.monet.bpi.types.Event;

public interface BehaviorProject  {
  
  /**
   * Event called when dueDate is reached for a previous registered event. 
   * Events registration are made by using EventService singleton
   */
  public void onReceiveEvent(Event event);
  
}