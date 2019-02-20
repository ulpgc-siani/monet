package org.monet.space.kernel.model;

import org.monet.metamodel.Definition;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class Trash extends Entity {

	@Override
	public String getLabel() {
		return "trash";
	}

	@Override
	public String getInstanceLabel() {
		return "trash";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public Definition getDefinition() {
		return new Definition() {
			@Override
			public String getCode() {
				return "trash";
			}

			@Override
			public String getName() {
				return "trash";
			}

			@Override
			public DefinitionType getType() {
				return DefinitionType.trash;
			}
		};
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}
}
