package org.monet.bpi;

import java.util.ArrayList;

public interface BPIFieldMultiple<T extends BPIField<?>,V> extends Iterable<V> {

	public T add();
  public T add(V newValue);
  public T insert(int index);
  public T insert(int index, V newValue);
  public void remove(int index);
  public void remove(V newValue);
  public void removeAll();
  public V get(int index);
  public T getAsField(int index);
  public int getCount();
  public ArrayList<V> getAll();
  public ArrayList<T> getAllFields();
  
}
