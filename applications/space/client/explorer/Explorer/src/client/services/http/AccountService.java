package client.services.http;

import client.core.model.Account;
import client.core.model.BusinessUnit;
import client.core.model.Notification;
import client.core.model.User;
import client.services.Services;
import client.services.callback.AccountCallback;
import client.services.callback.BusinessUnitListCallback;
import client.services.callback.NotificationListCallback;
import client.services.callback.VoidCallback;
import client.services.http.dialogs.account.LoadBusinessUnitsDialog;
import client.services.http.dialogs.account.LoadNotificationsDialog;
import client.services.http.dialogs.account.LogoutDialog;
import client.services.http.dialogs.account.ProfilePhotoDialog;

public class AccountService extends HttpService implements client.services.AccountService {

	public AccountService(Stub stub, Services services) {
		super(stub, services);
	}

    @Override
	public Account load() {
	    return services.getSpaceService().load().getAccount();
	}

    @Override
    public boolean userIsLogged(User user) {
        return load().getUser().equals(user);
    }

    @Override
	public void load(AccountCallback callback) {
		callback.success(load());
	}

	@Override
	public void loadNotifications(int start, int limit, NotificationListCallback callback) {
		stub.request(new LoadNotificationsDialog(start, limit), Notification.CLASS_NAME, callback);
	}

	@Override
	public void loadBusinessUnits(BusinessUnitListCallback callback) {
		stub.request(new LoadBusinessUnitsDialog(), BusinessUnit.CLASS_NAME, callback);
	}

	@Override
	public void logout(VoidCallback callback) {
		stub.request(new LogoutDialog(services.getSpaceService().load().getInstanceId()), null, callback);
	}

	@Override
	public void saveProfilePhoto(String photoUrl) {
		stub.request(new ProfilePhotoDialog(load().getUser().getId(), photoUrl), null, new VoidCallback() {
			@Override
			public void success(Void object) {
			}

			@Override
			public void failure(String error) {
			}
		});
	}
}
