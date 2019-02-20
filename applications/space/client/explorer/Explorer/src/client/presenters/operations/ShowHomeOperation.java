package client.presenters.operations;

import client.core.model.Account;
import client.core.model.NodeView;
import client.services.TranslatorService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowHomeOperation extends Operation {

	public static final Type TYPE = new Type("ShowHomeOperation", Operation.TYPE);

	public ShowHomeOperation(Context context) {
		super(context);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called.");

		Account account = services.getAccountService().load();

		ShowEnvironmentOperation operation = new ShowEnvironmentOperation(context, account.getRootNode(), (NodeView)account.getRootNode().getViews().getDefaultView());
		operation.inject(services);
		operation.execute();
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.SHOW_HOME);
	}

}
