package org.monet.bpi.java;

import java.util.ArrayList;

import org.monet.bpi.DimensionComponent;
import org.monet.bpi.types.Number;
import org.monet.bpi.types.Term;

public class DimensionComponentImpl implements DimensionComponent {
  
  public Object getFeatureValue(String key) {
    return null;
  }
  
  public ArrayList<Object> getFeatureValues(String key) {
    return null;
  }

  public void addFeature(String key, Number value) {
  }
  
  public void addFeature(String key, Boolean value) {
  }
  
  public void addFeature(String key, String value) {
  }
  
  public void addFeature(String key, Term value, ArrayList<Term> ancestors) {
  }

  public void save() {
  }
  
}
