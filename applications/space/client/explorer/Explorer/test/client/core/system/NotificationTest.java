package client.core.system;

import client.ApplicationTestCase;
import client.core.model.List;
import client.services.http.HttpList;
import client.services.http.builders.NotificationBuilder;
import com.google.gwt.core.client.JsonUtils;
import cosmos.utils.date.DateFormatter;
import org.junit.Test;

public class NotificationTest extends ApplicationTestCase {

	private static final String NOTIFICATIONS = "{\"totalCount\":2,\"items\":[{\"id\":\"2\",\"userId\":\"7\",\"publicationId\":\"2\",\"label\":\"Los android fallan como una escopeta de feria\",\"target\":\"1\",\"createDate\":1421157353000},{\"id\":\"1\",\"userId\":\"7\",\"publicationId\":\"1\",\"label\":\"Hay que ir a la tienda apple y comprar el iWatch\",\"target\":\"1\",\"createDate\":1421157348000}]}";

	@Test
	public void testLoadNotifications() {
		NotificationBuilder builder = new NotificationBuilder();
		List<client.core.model.Notification> notifications = builder.buildList((HttpList) JsonUtils.safeEval(NOTIFICATIONS).cast());

		assertEquals(2, notifications.size());
		assertEquals("2", notifications.get(0).getId());
		assertEquals("7", notifications.get(0).getUserId());
		assertEquals("2", notifications.get(0).getPublicationId());
		assertEquals("Los android fallan como una escopeta de feria", notifications.get(0).getLabel());
		assertEquals("1", notifications.get(0).getTarget());
		assertEquals("2015/01/13 13:55:53", DateFormatter.format(notifications.get(0).getCreateDate(), DateFormatter.Format.SECONDS));
	}

}