package org.monet.bpi.java;

import org.monet.bpi.MonetLink;
import org.monet.space.kernel.model.MonetLink.Type;

public class MonetLinkImpl extends MonetLink {

	protected org.monet.space.kernel.model.MonetLink link;

	public MonetLinkImpl(Type type, String id, String label, boolean editMode) {
		this.link = new org.monet.space.kernel.model.MonetLink(type, id, label, editMode);
	}

	public String getId() {
		return this.link.getId();
	}

	public Type getType() {
		return this.link.getType();
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

	public String toOfficeUrl() {
		return this.link.toOfficeUrl();
	}

	public org.monet.space.kernel.model.MonetLink getLink() {
		return link;
	}

}
