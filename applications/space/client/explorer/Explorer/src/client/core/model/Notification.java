package client.core.model;

import cosmos.types.Date;

public interface Notification extends Entity {

	ClassName CLASS_NAME = new ClassName("Notification");

	String getUserId();
	String getPublicationId();
	String getLabel();
	String getIcon();
	String getTarget();
	Date getCreateDate();
	boolean isRead();
	ClassName getClassName();
	boolean equals(Object object);

}
