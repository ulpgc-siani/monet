package org.monet.bpi;

import java.util.ArrayList;

public interface Cube<Fact> {

  public void addFact(Fact fact);
  
  public MemberList getMembers(String key, SelectionList selectionList, FilterList filterList, int start, int limit);
  
  public int getMembersCount(String key, SelectionList selectionList, FilterList filterList);

  public TupleList getTuples(String key, ArrayList<String> indicators, SelectionList selectionList, FilterList filterList, int start, int limit);

  public CubeRange getRange();
  
  public PeriodRange getPeriodRange(String key, String memberValue);

}
