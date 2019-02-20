package client.core.system;

import client.core.model.definition.entity.DocumentDefinition;
import client.core.model.definition.views.DocumentViewDefinition;
import client.core.model.definition.views.NodeViewDefinition;

public class Document<Definition extends DocumentDefinition> extends Node<Definition> implements client.core.model.Document<Definition> {

	public Document() {
	}

	@Override
	protected NodeView loadView(NodeViewDefinition viewDefinition) {
		DocumentView view = new DocumentView(new Key(viewDefinition.getCode(), viewDefinition.getName()), viewDefinition.getLabel(), viewDefinition.isDefault(), this);
		view.setDefinition((DocumentViewDefinition)viewDefinition);
		return view;
	}

	public Document(String id, String label) {
		super(id, label, false, Type.DOCUMENT);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.Document.CLASS_NAME;
	}

	@Override
	public boolean isEnvironment() {
		return false;
	}

	@Override
	public boolean isCatalog() {
		return super.isCatalog();
	}

	@Override
	protected void loadViews() {
		client.core.model.ViewList<client.core.model.NodeView> views = new client.core.system.ViewList<>();

		views.add(new DocumentView(NodeView.PREVIEW, "", true, this));

		setViews(views);
	}

}
