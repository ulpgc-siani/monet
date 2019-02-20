package org.monet.bpi;

import java.util.List;

public interface FieldComposite extends Field<List<Field<?>>> {

	boolean isEnabled();

	void setEnabled(boolean value);

}