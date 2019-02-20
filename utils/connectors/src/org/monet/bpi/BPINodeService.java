package org.monet.bpi;

	
public abstract class BPINodeService {
  
  protected static BPINodeService instance;
  
  public static BPINodeService getInstance() {
    return instance;
  }
  
  public abstract <T extends BPIBaseNode<?>> T locate(String name);
  
  public abstract <T extends BPIBaseNode<?>> T get(String nodeId);

	public abstract <T extends BPIBaseNode<?>> T create(Class<T> nodeClass, BPINode<?, ?> parent);
	
	public abstract <T extends BPIBaseNode<?>> T create(String name, BPINode<?, ?> parent);

}