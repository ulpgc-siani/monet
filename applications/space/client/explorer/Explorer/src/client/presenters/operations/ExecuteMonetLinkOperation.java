package client.presenters.operations;

import client.core.model.MonetLink;
import client.core.model.Node;
import client.core.model.Task;
import client.services.TranslatorService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecuteMonetLinkOperation extends Operation {
	private final MonetLink link;

	public static final Type TYPE = new Type("ExecuteMonetLinkOperation", Operation.TYPE);

	public ExecuteMonetLinkOperation(Context context, MonetLink link) {
		super(context);
		this.link = link;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Link: " + link.toString());
		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());

		if (link.getType() == MonetLink.Type.Node)
			showNode();
		else if (link.getType() == MonetLink.Type.News)
			showNews();
		else if (link.getType() == MonetLink.Type.Task)
			showTask();

		getMessageDisplay().hideLoading();
		executePerformed();
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.ABORT_TASK);
	}

	private void showNode() {
		Node node = services.getSpaceService().getEntityFactory().createNode(link.getId());
		executeOperation(new ShowNodeOperation(context, node, null));
	}

	private void showNews() {
		executeOperation(new ShowNewsOperation(context));
	}

	private void showTask() {
		Task task = services.getSpaceService().getEntityFactory().createTask(link.getId());
		executeOperation(new ShowTaskOperation(context, task, null));
	}

	private void executeOperation(Operation operation) {
		operation.inject(services);
		operation.execute();
	}

}
