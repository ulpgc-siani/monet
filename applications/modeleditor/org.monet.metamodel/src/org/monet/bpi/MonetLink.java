package org.monet.bpi;


public abstract class MonetLink {
 
  public static MonetLink forNode(String nodeId, String label) {
    return null;
  }

  public static MonetLink forNode(String nodeId, String label, boolean editMode) {
    return null;
  }

  public static MonetLink forTask(String taskId, String label) {
    return null;
  }
  
  public String getView() {
	  return null;
  }

  public MonetLink withView(String view) {
	  return this;
  }
}
