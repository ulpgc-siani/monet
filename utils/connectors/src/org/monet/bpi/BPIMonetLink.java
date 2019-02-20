package org.monet.bpi;

import org.monet.bpi.java.BPIMonetLinkImpl;

public abstract class BPIMonetLink {
 
  public static enum Type {
    Node,
    Task
  }
    
  public static BPIMonetLink forNode(String nodeId) {
    return new BPIMonetLinkImpl(Type.Node, nodeId);
  }
  public static BPIMonetLink forTask(String taskId) {
    return new BPIMonetLinkImpl(Type.Task, taskId);
  }
  
}
