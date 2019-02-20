package client.core.system;

import cosmos.types.Date;

public class Notification extends Entity implements client.core.model.Notification {
	private String userId;
	private String publicationId;
	private String icon;
	private String target;
	private Date createDate;
	private boolean read;

	public Notification() {
		super();
	}

	public Notification(String id, String label, Date createDate) {
		super(id, label);
		this.createDate = createDate;
	}

	@Override
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String getPublicationId() {
		return publicationId;
	}

	public void setPublicationId(String publicationId) {
		this.publicationId = publicationId;
	}

	@Override
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public boolean isRead() {
		return read;
	}

	public void setRead(boolean isRead) {
		this.read = isRead;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof String)
			return getId().equals(object);

		if (!(object instanceof Notification))
			return false;

		return getId().equals(((Notification)object).getId());
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.Notification.CLASS_NAME;
	}

}
