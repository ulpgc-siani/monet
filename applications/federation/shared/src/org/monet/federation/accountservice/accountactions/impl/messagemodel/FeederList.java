package org.monet.federation.accountservice.accountactions.impl.messagemodel;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "feederlist")
public class FeederList {
  private @ElementList(inline=true,required=false) ArrayList<Feeder> feeders;

  public FeederList() {
  }
  
  public ArrayList<Feeder> getAll() {
    
    if (this.feeders == null)
      return new ArrayList<Feeder>();
    
    return this.feeders;
  }
}