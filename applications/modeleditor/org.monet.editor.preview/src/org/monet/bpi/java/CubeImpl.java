package org.monet.bpi.java;

import java.util.ArrayList;

import org.monet.bpi.Cube;
import org.monet.bpi.CubeRange;
import org.monet.bpi.FilterList;
import org.monet.bpi.MemberList;
import org.monet.bpi.PeriodRange;
import org.monet.bpi.SelectionList;
import org.monet.bpi.TupleList;

public class CubeImpl<Fact> implements Cube<Fact> {

  public void addFact(Fact fact) {
    
  }
  
  @Override
  public MemberList getMembers(String key, SelectionList bpiSelectionList, FilterList bpiFilterList, int start, int limit) {
    return null;
  }
  
  @Override
  public int getMembersCount(String key, SelectionList selectionList, FilterList filterList) {
    return 0;
  }

  @Override
  public TupleList getTuples(String key, ArrayList<String> indicators, SelectionList bpiSelectionList, FilterList bpiFilterList, int start, int limit) {
    return null;
  }

  @Override
  public CubeRange getRange() {
    return null;
  }

  @Override
  public PeriodRange getPeriodRange(String key, String memberValue) {
    return null;
  }
  
}
