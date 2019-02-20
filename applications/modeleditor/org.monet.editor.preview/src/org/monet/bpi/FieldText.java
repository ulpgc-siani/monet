package org.monet.bpi;

public interface FieldText extends Field<String> {

  public String getGroup(String name);
  public String getGroup(int index);

}