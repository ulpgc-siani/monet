package client.presenters.displays;

import client.core.model.List;
import client.presenters.operations.ShowNewsOperation;
import client.services.AccountService;
import client.services.callback.NotificationListCallback;
import cosmos.presenters.Operation;
import cosmos.presenters.Presenter;

public class NotificationListDisplay extends Display {
	public static final Type TYPE = new Type("NotificationListDisplay", Display.TYPE);

	public static final int LIMIT = 10;

	public NotificationListDisplay() {
	}

	@Override
	protected void onInjectServices() {
		addShowNewsOperation();
	}

	public void loadNotifications() {
		AccountService service = this.services.getAccountService();

		service.loadNotifications(0, LIMIT, new NotificationListCallback() {
			@Override
			public void success(List<client.core.model.Notification> result) {
				notifyNotifications(result);
			}

			@Override
			public void failure(String error) {

			}
		});
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	private void addShowNewsOperation() {
		addChild(new ShowNewsOperation(new Operation.Context() {
			@Override
			public Presenter getCanvas() {
				return NotificationListDisplay.this.getRootDisplay();
			}

			@Override
			public Operation getReferral() {
				return getVisitingDisplayOperation();
			}
		}));
	}

	private void notifyNotifications(final List<client.core.model.Notification> notificationList) {
		this.updateHooks(new Notification<Hook>() {
				@Override
				public void update(Hook hook) {
					hook.notifications(notificationList);
				}
			});
	}

	public interface Hook extends Display.Hook {
		void notifications(List<client.core.model.Notification> notificationList);
	}
}
