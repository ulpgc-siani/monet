package org.monet.space.office.presentation.user.renders;

import java.util.HashMap;

public class NotificationListPageRender extends OfficeRender {

	public NotificationListPageRender() {
		super();
	}

	private String initNotificationTemplate() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		return block("notificationTemplate:client-side", map);
	}

	@Override
	protected void init() {
		loadCanvas("page.notificationlist");

		addMark("notificationTemplate", this.initNotificationTemplate());
	}

}