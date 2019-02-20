package org.monet.bpi;
	
public abstract class ThesaurusService {
  
  protected static ThesaurusService instance;
  
  public static Thesaurus get(String name) {
    return instance.getImpl(name);
  }
  
  protected abstract Thesaurus getImpl(String name);
  
}