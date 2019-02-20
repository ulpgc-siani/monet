package client.presenters.operations;

import client.core.model.Account;
import client.core.model.User;
import client.presenters.displays.AccountDisplay;
import client.presenters.displays.ApplicationDisplay;
import client.services.TranslatorService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RefreshAccountOperation extends Operation {
	private final User user;

	public static final Type TYPE = new Type("RefreshAccountPhotoOperation", Operation.TYPE);

	public RefreshAccountOperation(Context context, User user) {
		super(context);
		this.user = user;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. User: " + user.toString());
		refreshCanvas(services.getAccountService().load());
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.REFRESH_ACCOUNT);
	}

	protected void refreshCanvas(Account account) {
		account.setUser(user);

		ApplicationDisplay applicationDisplay = context.getCanvas();
		AccountDisplay accountDisplay = applicationDisplay.getChild(AccountDisplay.TYPE);
		accountDisplay.refresh();

		this.executePerformed();
	}

}
