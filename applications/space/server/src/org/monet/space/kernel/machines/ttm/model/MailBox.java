package org.monet.space.kernel.machines.ttm.model;

import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.MailBoxUri;

public class MailBox {

	public static enum Type {CUSTOMER, INTERNAL_PROVIDER, EXTERNAL_PROVIDER, CONTEST, CONTESTANT}

	private String id;
	private String code;
	private String taskId;
	private Type type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

    public MailBoxUri toUri() {
        return MailBoxUri.build(BusinessUnit.getInstance().getName(), this.getId());
    }

}
