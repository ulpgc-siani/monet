package org.monet.space.explorer.control.dialogs;

import com.google.inject.Inject;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.SetDefinition;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.model.Node;

public class LoadIndexDialog extends HttpDialog {
	protected LayerProvider layerProvider;

	@Inject
	public void inject(LayerProvider layerProvider) {
		this.layerProvider = layerProvider;
	}

	public <T extends Scope> T getScope() {
		String scope = getString("scope");

		if (scope.equals(Scope.NODE))
			return (T) new NodeScope() {
				@Override
				public Node getSet() {
					return LoadIndexDialog.this.getSet();
				}

				@Override
				public SetDefinition.SetViewProperty getSetView() {
					return LoadIndexDialog.this.getSetView();
				}
			};

		if (scope.equals(Scope.FIELD))
			return (T) new FieldScope() {
				@Override
				public String getSingleton() {
					return LoadIndexDialog.this.getSingleton();
				}

				@Override
				public String getField() {
					return null;
				}
			};

		return null;
	}

	public IndexDefinition getIndexDefinition() {
		return dictionary.getIndexDefinition(getEntityId());
	}

	public IndexDefinition.IndexViewProperty getIndexView() {
		IndexDefinition definition = getIndexDefinition();
		return definition.getView(getString(Parameter.INDEX_VIEW));
	}

	private Node getSet() {
		return layerProvider.getNodeLayer().loadNode(getString(Parameter.SET));
	}

	private SetDefinition.SetViewProperty getSetView() {
		NodeDefinition nodeDefinition = getSet().getDefinition();

		if (nodeDefinition.isCollection() || nodeDefinition.isCatalog())
			return (SetDefinition.SetViewProperty) nodeDefinition.getNodeView(getString(Parameter.SET_VIEW));

		return null;
	}

	private String getSingleton() {
		return getString(Parameter.SINGLETON);
	}

	public interface Scope {
		String NODE = "node";
		String FIELD = "field";
	}

	public interface NodeScope extends Scope {
		Node getSet();
		SetDefinition.SetViewProperty getSetView();
	}

	public interface FieldScope extends Scope {
		String getSingleton();
		String getField();
	}

}
