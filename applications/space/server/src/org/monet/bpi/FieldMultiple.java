package org.monet.bpi;

import org.monet.bpi.types.Term;

import java.util.ArrayList;
import java.util.List;

public interface FieldMultiple<T extends Field<?>, V> extends Iterable<V> {

	public String getCode();

	public T addNew();

	public T addNew(V newValue);

	public List<T> addNewAll(List<V> newValues);

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

	public ArrayList<Term> getAllAsTerm();
}
