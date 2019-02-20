package client.presenters.displays;

public class MessageDisplay extends Display {

	public static final Type TYPE = new Type("MessageDisplay", Display.TYPE);

	public void alert(String title, String message, final AlertCallback callback) {
		notifyAlert(title, message, callback);
	}

	public void confirm(String title, String message, ConfirmationCallback callback) {
		notifyConfirm(title, message, callback);
	}

	public void alertWithTimeout(String title, String description) {
		notifyAlertWithTimeout(title, description);
	}

	public void showLoading() {
		showLoading(services.getTranslatorService().getLoadingLabel());
	}

	public void showLoading(String message) {
		notifyShowLoading(message);
	}

	public void hideLoading() {
		notifyHideLoading();
	}

	public void showMessage(String title, String message) {
		notifyShowMessage(title, message);
	}

	public void hideMessage() {
		notifyHideMessage();
	}

	private void notifyAlert(final String title, final String message, final AlertCallback callback) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.alert(title, message, callback);
			}
		});
	}

	private void notifyConfirm(final String title, final String message, final ConfirmationCallback callback) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.confirm(title, message, callback);
			}
		});
	}

	private void notifyAlertWithTimeout(final String title, final String message) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.alertWithTimeout(title, message);
			}
		});
	}

	private void notifyShowLoading(final String message) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.showLoading(message);
			}
		});
	}

	private void notifyHideLoading() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.hideLoading();
			}
		});
	}

	private void notifyShowMessage(final String title, final String message) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.showMessage(title, message);
			}
		});
	}

	private void notifyHideMessage() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.hideMessage();
			}
		});
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public interface Hook extends Display.Hook {
		void alert(String title, String message, AlertCallback callback);
		void confirm(String title, String message, ConfirmationCallback callback);
		void alertWithTimeout(String title, String message);
		void showLoading(String message);
		void hideLoading();
		void showMessage(String message, String shortMessage);
		void hideMessage();
	}

	public interface AlertCallback {
		void close();
	}

	public interface ConfirmationCallback {
		void accept();
		void cancel();
	}
}
