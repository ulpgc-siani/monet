package org.monet.space.kernel.listeners;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.Notification;
import org.monet.space.kernel.model.news.Post;

public class ListenerNewsPost extends Listener {

	@Override
	public void postCreated(MonetEvent event) {
		Post post = (Post) event.getSender();
		String target = null;

		if (post.getTarget() != null)
			target = post.getTarget().toString();

		boolean addNotification = event.getParameter(MonetEvent.PARAMETER_ADD_NOTIFICATION);
		if (!addNotification)
			return;

		String wallUserId = post.getWallUserId();
		Notification notification = new Notification();

		if (wallUserId != null) notification.setUserId(wallUserId);
		notification.setLabel(post.getTitle());
		notification.setIcon(null);
		notification.setTarget(target);

		if (wallUserId == null || wallUserId.equals("-1"))
			ComponentPersistence.getInstance().getNotificationLayer().notify(notification);
		else
			ComponentPersistence.getInstance().getNotificationLayer().notifyToUser(notification, wallUserId);
	}

	@Override
	public void postCommentCreated(MonetEvent event) {

	}

}
