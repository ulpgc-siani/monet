package org.monet.bpi.java;

import org.monet.bpi.MonetLink;

public class MonetLinkImpl extends MonetLink {

	protected org.monet.api.space.backservice.impl.model.MonetLink link;

	public MonetLinkImpl(Type type, String id, String label, boolean editMode) {
		org.monet.api.space.backservice.impl.model.MonetLink.Type monetLinkType = null;

		switch (type) {
			case News: monetLinkType = org.monet.api.space.backservice.impl.model.MonetLink.Type.News;
			case Node: monetLinkType = org.monet.api.space.backservice.impl.model.MonetLink.Type.Node;
			case Task: monetLinkType = org.monet.api.space.backservice.impl.model.MonetLink.Type.Task;
			case User: monetLinkType = org.monet.api.space.backservice.impl.model.MonetLink.Type.User;
		}

		this.link = new org.monet.api.space.backservice.impl.model.MonetLink(monetLinkType, id, label, editMode);
	}

	public String getId() {
		return this.link.getId();
	}

	public Type getType() {
		switch (this.link.getType()) {
			case News: return Type.News;
			case Node: return Type.Node;
			case Task: return Type.Task;
			case User: return Type.User;
		}
		return null;
	}

	public String getLabel() {
		return this.link.getLabel();
	}

	public boolean isEditMode() {
		return this.link.isEditMode();
	}

	@Override
	public String toString() {
		return this.link.toString();
	}

	public org.monet.api.space.backservice.impl.model.MonetLink getLink() {
		return link;
	}

}
