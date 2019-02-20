package org.monet.bpi;
	
public abstract class BPIThesaurusService {
  
  protected static BPIThesaurusService instance;
  
  public static BPIThesaurusService getInstance() {
    return instance;
  }
  
  public abstract BPIThesaurus get(String name);
  
}