package client.presenters.operations;

import client.core.messages.DeletingNodeError;
import client.core.model.Node;
import client.presenters.displays.MessageDisplay;
import client.services.TranslatorService;
import client.services.callback.VoidCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

import static client.services.TranslatorService.OperationLabel.*;

public class DeleteNodeOperation extends Operation {
	private final Node node;

	public static final Type TYPE = new Type("DeleteNodeOperation", Operation.TYPE);

	public DeleteNodeOperation(Context context, Node node) {
		super(context);
		this.node = node;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Node to remove: " + node.getLabel());

		final MessageDisplay messageDisplay = getMessageDisplay();
		final TranslatorService translatorService = services.getTranslatorService();

		messageDisplay.confirm(translatorService.translate(CONFIRM), translatorService.translate(CONFIRM_DELETE_NODE), new MessageDisplay.ConfirmationCallback() {
			@Override
			public void accept() {
				messageDisplay.showLoading(translatorService.getLoadingLabel());

				services.getNodeService().delete(node, new VoidCallback() {
					@Override
					public void success(Void value) {
						messageDisplay.hideLoading();
						executePerformed();
					}

					@Override
					public void failure(String details) {
						messageDisplay.hideLoading();
						executeFailed(new DeletingNodeError(details));
					}
				});
			}

			@Override
			public void cancel() {
			}
		});
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(DELETE_SELECTION);
	}

}
