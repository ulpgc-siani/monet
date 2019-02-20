package client.services.http.builders;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.Notification;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class NotificationBuilder implements Builder<client.core.model.Notification, List<client.core.model.Notification>> {
	@Override
	public client.core.model.Notification build(HttpInstance instance) {
		if (instance == null)
			return null;

		Notification notification = new Notification();
		initialize(notification, instance);
		return notification;
	}

	@Override
	public void initialize(client.core.model.Notification object, HttpInstance instance) {
		Notification notification = (Notification)object;
		notification.setId(instance.getString("id"));
		notification.setUserId(instance.getString("userId"));
		notification.setPublicationId(instance.getString("publicationId"));
		notification.setLabel(instance.getString("label"));
		notification.setIcon(instance.getString("icon"));
		notification.setTarget(instance.getString("target"));
		notification.setCreateDate(instance.getDate("createDate"));
		notification.setRead(instance.getBoolean("read"));
	}

	@Override
	public List<client.core.model.Notification> buildList(HttpList instance) {
		List<client.core.model.Notification> result = new MonetList<>();

		for (int i = 0; i < instance.getItems().length(); i++)
			result.add(build(instance.getItems().get(i)));

		return result;
	}
}
