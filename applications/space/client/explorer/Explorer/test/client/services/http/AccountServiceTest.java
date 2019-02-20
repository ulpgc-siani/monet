package client.services.http;

import client.core.model.BusinessUnitList;
import client.core.model.List;
import client.core.model.Notification;
import client.services.Service;
import client.services.callback.BusinessUnitListCallback;
import client.services.callback.NotificationListCallback;
import client.services.callback.VoidCallback;
import org.junit.Test;

public class AccountServiceTest extends ServiceTest {

	@Test
	public void testLoadNotifications() {
		client.services.AccountService service = createService();

		service.loadNotifications(0, 10, new NotificationListCallback() {
			@Override
			public void success(List<Notification> object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.println(error);
			}
		});
	}

	@Test
	public void testLoadBusinessUnits() {
		client.services.AccountService service = createService();

		service.loadBusinessUnits(new BusinessUnitListCallback() {
			@Override
			public void success(BusinessUnitList object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.println(error);
			}
		});
	}

	@Test
	public void testLogout() {
		client.services.AccountService service = createService();

		service.logout(new VoidCallback() {
			@Override
			public void success(Void object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.println(error);
			}
		});
	}

	@Override
	protected <T extends Service> T createService() {
		return (T)new client.services.http.AccountService(createStub(), createServices());
	}

}