package client.presenters.displays.view;

import client.core.model.Document;
import client.core.model.DocumentView;
import client.core.model.Node;

public class DocumentViewDisplay extends NodeViewDisplay<DocumentView> {

	public static final Type TYPE = new Type("DocumentViewDisplay", NodeViewDisplay.TYPE);

	public DocumentViewDisplay(Node node, DocumentView nodeView) {
        super(node, nodeView);
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public String getBaseUrl() {
		return services.getNodeService().getNodePreviewBaseUrl(getDocumentId());
	}

	public String getDocumentId() {
		return getEntity().getId();
	}

	public static class Builder extends ViewDisplay.Builder<Node, DocumentView> {

		protected static void register() {
			registerBuilder(Document.CLASS_NAME.toString() + DocumentView.CLASS_NAME.toString(), new Builder());
		}

		@Override
		public ViewDisplay build(Node entity, DocumentView view) {
			return new DocumentViewDisplay(entity, view);
		}
	}

	public interface Hook extends NodeViewDisplay.Hook {
	}

}
