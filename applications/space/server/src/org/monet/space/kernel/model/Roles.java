package org.monet.space.kernel.model;

import org.monet.metamodel.Definition;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class Roles extends Entity {

	@Override
	public String getLabel() {
		return "roles";
	}

	@Override
	public String getInstanceLabel() {
		return "roles";
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
				return "roles";
			}

			@Override
			public String getName() {
				return "roles";
			}

			@Override
			public DefinitionType getType() {
				return DefinitionType.roles;
			}
		};
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}
}
