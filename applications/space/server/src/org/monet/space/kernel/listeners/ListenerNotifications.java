package org.monet.space.kernel.listeners;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NewsLayer;
import org.monet.space.kernel.components.layers.NotificationLayer;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.MonetLink;
import org.monet.space.kernel.model.Notification;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.news.Post;

public class ListenerNotifications extends Listener {

	private NotificationLayer notificationLayer;
	private NewsLayer newsLayer;

	@Override
	public void taskNewMessagesReceived(MonetEvent event) {
		Task task = (Task) event.getSender();
		String taskOrderId = event.getParameter(MonetEvent.PARAMETER_ORDER_ID);
		String title = event.getParameter(MonetEvent.PARAMETER_TITLE);
		String body = event.getParameter(MonetEvent.PARAMETER_BODY);
		String publicationId = generatePublicationId(task.getId(), taskOrderId, "new_messages");
		MonetLink target = new MonetLink(MonetLink.Type.Task, task.getId()).withView("chat." + taskOrderId);

		sendPost(title, body, target, publicationId);
		sendNotification(title, target, publicationId, task);
	}

	private void sendPost(String title, String body, MonetLink target, String publicationId) {

		if (this.newsLayer == null)
			this.newsLayer = ComponentPersistence.getInstance().getNewsLayer();

		Post post = new Post();
		post.setTitle(title);
		post.setBody(body);
		post.setTarget(target);
		post.setWallUserId(null);

		this.newsLayer.publish(post, false);
	}

	private void sendNotification(String title, MonetLink target, String publicationId, Task task) {

		if (this.notificationLayer == null)
			this.notificationLayer = ComponentPersistence.getInstance().getNotificationLayer();

		Notification notification = new Notification();
		notification.setLabel(title);
		notification.setIcon(null);
		notification.setTarget(target.toString());
		notification.setPublicationId(publicationId);

		this.notificationLayer.notifyToTask(notification, task);
	}

	private String generatePublicationId(String taskId, String taskOrderId, String suffix) {
		return taskId + "_" + taskOrderId + "_" + suffix;
	}

}
