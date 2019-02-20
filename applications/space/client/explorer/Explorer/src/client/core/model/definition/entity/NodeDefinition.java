package client.core.model.definition.entity;

import client.core.model.List;
import client.core.model.definition.views.NodeViewDefinition;

public interface NodeDefinition<V extends NodeViewDefinition> extends EntityDefinition {

	List<OperationDefinition> getOperations();
	List<V> getViews();
	V getView(String key);
	boolean isReadonly();

	interface OperationDefinition {
		String getCode();
		String getName();
		String getLabel();
		String getDescription();
		boolean isEnabled();
		boolean isVisible();
	}
}
