package org.monet.bpi.types;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class Link {

	public static final String ID = "id";

	@Attribute(name = ID)
	private String id;
	@Text
	private String label;

	public Link() {
	}

	public Link(Link link) {
		this(link.id, link.label);
	}

	public Link(String id, String label) {
		this.id = id;
		this.label = label;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public Term toTerm() {
		return new Term(id, label);
	}

}
