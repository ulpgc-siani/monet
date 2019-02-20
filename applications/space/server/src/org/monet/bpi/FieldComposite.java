package org.monet.bpi;

import java.util.List;

public interface FieldComposite extends Field<List<Field<?>>> {
	boolean isEnabled();
	void setEnabled(boolean value);

	boolean isExtended();
	void setExtended(boolean value);

	<T, F extends Field<V>, V> T getField(String key);

}