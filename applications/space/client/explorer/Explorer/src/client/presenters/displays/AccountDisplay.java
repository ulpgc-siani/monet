package client.presenters.displays;

import client.core.model.Account;
import client.presenters.operations.LogoutOperation;
import cosmos.gwt.model.Theme;
import cosmos.presenters.Operation;

public class AccountDisplay extends Display {
	public static final Type TYPE = new Type("AccountDisplay", Display.TYPE);
	private Account account;
	private Theme theme;

	public AccountDisplay(Account account, Theme theme) {
		this.account = account;
		this.theme = theme;
	}

	@Override
	protected void onInjectServices() {
		addNotificationList();
		addBusinessUnitList();
		addLogoutCommand();
	}

	public String getFullName() {
		return account.getFullName();
	}

	public String getEmail() {
		return account.getEmail();
	}

	public String getPhoto() {

		if (account.getPhoto() != null && !account.getPhoto().isEmpty())
			return account.getPhoto();

		return (getLayoutDisplay().isReducedMode())?theme.getAddPhotoReducedPath():theme.getAddPhotoPath();
	}

	public boolean isDefaultPhoto() {
		return (account.getPhoto() == null || account.getPhoto().isEmpty());
	}

	private void addNotificationList() {
		addChild(new NotificationListDisplay());
	}

	private void addBusinessUnitList() {
		addChild(new BusinessUnitListDisplay());
	}

	private void addLogoutCommand() {
		addChild(new LogoutOperation(new Operation.NullContext()));
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public void refresh() {
		account = services.getAccountService().load();
		notifyRefresh();
	}

	private void notifyRefresh() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.update();
			}
		});
	}

	public interface Hook extends EntityDisplay.Hook {
		void update();
	}

}
