package client.presenters.operations;

import client.core.messages.LogoutError;
import client.services.TranslatorService;
import client.services.callback.VoidCallback;
import com.google.gwt.user.client.Window;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogoutOperation extends Operation {

	public static final Type TYPE = new Type("LogoutOperation", Operation.TYPE);

	public LogoutOperation(Context context) {
		super(context);
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.LOGOUT);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called.");

		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());

		services.getAccountService().logout(new VoidCallback() {
			@Override
			public void success(Void value) {
				Window.Location.reload();
			}

			@Override
			public void failure(String error) {
				getMessageDisplay().hideLoading();
				LogoutOperation.this.executeFailed(new LogoutError(error));
			}
		});


	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

}
