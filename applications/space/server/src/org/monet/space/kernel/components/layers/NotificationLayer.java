package org.monet.space.kernel.components.layers;

import org.monet.space.kernel.model.Notification;
import org.monet.space.kernel.model.NotificationList;
import org.monet.space.kernel.model.Task;

public interface NotificationLayer extends Layer {

	public NotificationList loadNotificationList(String userId, int startpos, int limit);

	public void notify(Notification notification);

	public void notifyToUser(Notification notification, String userId);

	public void notifyToRole(Notification notification, String role);

	public void notifyToTask(Notification notification, Task task);

	public void markAsRead(String ids);

	public void markAllAsRead();

}
