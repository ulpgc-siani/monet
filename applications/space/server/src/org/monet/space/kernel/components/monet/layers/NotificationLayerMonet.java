package org.monet.space.kernel.components.monet.layers;

import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NotificationLayer;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Notification;
import org.monet.space.kernel.model.NotificationList;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.producers.ProducerFederationList;
import org.monet.space.kernel.producers.ProducerNotification;
import org.monet.space.kernel.producers.ProducerNotificationList;

import java.util.List;

public class NotificationLayerMonet extends PersistenceLayerMonet implements NotificationLayer {

	public NotificationLayerMonet(ComponentPersistence componentPersistence) {
		super(componentPersistence);
	}

	@Override
	public NotificationList loadNotificationList(String userId, int startpos, int limit) {
		ProducerNotificationList producerNotificationList;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerNotificationList = (ProducerNotificationList) this.producersFactory.get(Producers.NOTIFICATIONLIST);

		return producerNotificationList.load(userId, startpos, limit);
	}

	@Override
	public void notify(Notification notification) {
		ProducerNotification producerNotification;
		ProducerFederationList producerBackAccountList;
		String publicationId = notification.getPublicationId();

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerNotification = this.producersFactory.get(Producers.NOTIFICATION);
		producerBackAccountList = this.producersFactory.get(Producers.FEDERATIONLIST);

		if (producerNotification.exists(publicationId)) {
			producerNotification.priorize(publicationId);
			return;
		}

		List<String> userIds = producerBackAccountList.loadUsersIds();
		for (String userId : userIds) {
			notification.setUserId(userId);
			producerNotification.create(notification);
		}
	}

	@Override
	public void notifyToUser(Notification notification, String userId) {
		ProducerNotification producerNotification;
		String publicationId = notification.getPublicationId();

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerNotification = this.producersFactory.get(Producers.NOTIFICATION);

		if (producerNotification.exists(publicationId)) {
			producerNotification.priorize(publicationId);
			return;
		}

		notification.setUserId(userId);
		producerNotification.create(notification);
	}

	@Override
	public void notifyToRole(Notification notification, String role) {
		ProducerNotification producerNotification;
		RoleLayer roleLayer;
		String publicationId = notification.getPublicationId();

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerNotification = this.producersFactory.get(Producers.NOTIFICATION);
		roleLayer = ComponentFederation.getInstance().getRoleLayer();

		if (producerNotification.exists(publicationId)) {
			producerNotification.priorize(publicationId);
			return;
		}

		List<String> userIds = roleLayer.loadRoleListUsersIds(role);
		for (String userId : userIds) {
			notification.setUserId(userId);
			producerNotification.create(notification);
		}
	}

	@Override
	public void notifyToTask(Notification notification, Task task) {
		ProducerNotification producerNotification;
		String publicationId = notification.getPublicationId();
		List<String> userIds;

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerNotification = (ProducerNotification) this.producersFactory.get(Producers.NOTIFICATION);

		if (producerNotification.exists(publicationId)) {
			producerNotification.priorize(publicationId);
			return;
		}

		if (task.getRole() == null) {
			ProducerFederationList producerFederationList = (ProducerFederationList) this.producersFactory.get(Producers.FEDERATIONLIST);
			userIds = producerFederationList.loadUsersIds();
		} else
			userIds = task.getEnrolmentsIdUsers();

		for (String userId : userIds) {
			notification.setUserId(userId);
			producerNotification.create(notification);
		}
	}

	@Override
	public void markAsRead(String ids) {
		ProducerNotification producerNotification;
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerNotification = (ProducerNotification) this.producersFactory.get(Producers.NOTIFICATION);
		producerNotification.markAsRead(ids);
	}

	@Override
	public void markAllAsRead() {
		ProducerNotification producerNotification;
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerNotification = (ProducerNotification) this.producersFactory.get(Producers.NOTIFICATION);
		producerNotification.markAllAsRead();
	}

}
