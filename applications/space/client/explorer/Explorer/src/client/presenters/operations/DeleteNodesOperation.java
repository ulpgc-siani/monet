package client.presenters.operations;

import client.core.messages.DeletingNodesError;
import client.core.model.Node;
import client.presenters.displays.MessageDisplay;
import client.services.TranslatorService;
import client.services.callback.VoidCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

import static client.services.TranslatorService.OperationLabel.*;

public class DeleteNodesOperation extends Operation {
	private final NodeSelection selection;

	public static final Type TYPE = new Type("DeleteNodesOperation", Operation.TYPE);

	public DeleteNodesOperation(Context context, NodeSelection selection) {
		super(context);
		this.selection = selection;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Nodes to remove: " + serializeNodes());

		final MessageDisplay messageDisplay = getMessageDisplay();
		final TranslatorService translatorService = services.getTranslatorService();

		messageDisplay.confirm(translatorService.translate(CONFIRM), translatorService.translate(CONFIRM_DELETE_NODES), new MessageDisplay.ConfirmationCallback() {
			@Override
			public void accept() {
				messageDisplay.showLoading(services.getTranslatorService().getLoadingLabel());

				services.getNodeService().delete(selection.get(), new VoidCallback() {
					@Override
					public void success(Void value) {
						messageDisplay.hideLoading();
						executePerformed();
					}

					@Override
					public void failure(String details) {
						messageDisplay.hideLoading();
						executeFailed(new DeletingNodesError(details));
					}
				});
			}

			@Override
			public void cancel() {
			}
		});
	}

	private String serializeNodes() {
		String result = "";
		for (Node node : selection.get()) {
			if (!result.isEmpty())
				result += ",";
			result += node.getLabel();
		}
		return result;
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(DELETE_SELECTION);
	}

	public interface NodeSelection {
		Node[] get();
	}
}
