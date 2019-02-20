package org.monet.space.explorer.control.dialogs;

import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.control.dialogs.deserializers.FilterDeserializer;
import org.monet.space.explorer.model.Filter;

public class LoadTaskListIndexFilterOptionsDialog extends HttpDialog {

	public Filter getFilter() {
		return new FilterDeserializer(createDeserializerHelper()).deserialize(getString(Parameter.FILTER));
	}

}
