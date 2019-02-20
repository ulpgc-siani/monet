package org.monet.bpi;

import org.monet.bpi.types.SummationItem;

public interface FieldSummation extends FieldNumber {

	public SummationItem getItem(String itemKey);

	public SummationItem addNew(String key);

	public void delete(SummationItem item);

	public void commitChanges() throws Exception;

	public void discardChanges();

}