package org.monet.space.kernel.model;

import org.monet.bpi.types.Link;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class CustomFactExtraData {

	@Element(required = false)
	private Link link;

	@Element
	private String title;
	@Element(required = false)
	private String text;

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}