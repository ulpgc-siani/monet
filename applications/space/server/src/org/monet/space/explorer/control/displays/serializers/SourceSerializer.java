package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.JsonSerializer;
import org.monet.metamodel.SourceDefinition;
import org.monet.space.kernel.model.Source;

public class SourceSerializer extends EntitySerializer<Source, SourceDefinition> implements JsonSerializer<Source> {

	public SourceSerializer(Helper helper) {
		super(helper);
	}

}
