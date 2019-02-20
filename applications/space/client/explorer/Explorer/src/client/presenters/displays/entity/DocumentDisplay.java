package client.presenters.displays.entity;

import client.core.model.Document;
import client.core.model.DocumentView;
import client.presenters.displays.OperationListDisplay;
import client.presenters.operations.DownloadNodeOperation;
import cosmos.presenters.Presenter;

public class DocumentDisplay extends NodeDisplay<Document, DocumentView> implements EnvironmentDisplay {

	public static final Type TYPE = new Type("DocumentDisplay", EnvironmentDisplay.TYPE);

	public DocumentDisplay(Document document, DocumentView view) {
super(document, view);
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public boolean isRoot() {
		return false;
	}

	@Override
	protected void addOperations() {
		super.addOperations();

		OperationListDisplay display = getChild(OperationListDisplay.TYPE);
		display.addChild(createDownloadOperation());
	}

	private Presenter createDownloadOperation() {
		DownloadNodeOperation operation = new DownloadNodeOperation(getOperationContext(), getEntity());
		operation.inject(services);
		return operation;
	}
}
