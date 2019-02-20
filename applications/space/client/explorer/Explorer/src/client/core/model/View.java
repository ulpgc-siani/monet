package client.core.model;

import client.core.model.definition.views.ViewDefinition;

public interface View<Definition extends ViewDefinition> extends Entity<Definition> {
	boolean isDefault();
	boolean equals(Object object);
}
