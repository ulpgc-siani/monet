package client.services;

import client.core.model.Account;
import client.core.model.User;
import client.services.callback.AccountCallback;
import client.services.callback.BusinessUnitListCallback;
import client.services.callback.NotificationListCallback;
import client.services.callback.VoidCallback;

public interface AccountService extends Service {
	Account load();
    boolean userIsLogged(User user);
	void load(final AccountCallback callback);
	void loadNotifications(int start, int limit, NotificationListCallback callback);
	void loadBusinessUnits(BusinessUnitListCallback callback);
	void logout(VoidCallback callback);
	void saveProfilePhoto(String photoUrl);
}
