package org.monet.bpi.types;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class Link {

	public static enum LinkType {NODE, TASK}

	@Attribute
	private String id;
	@Attribute
	private LinkType type;
	@Text
	private String label;

	private String code;
	private Object extraData;

	public Link() {
	}

	public Link(LinkType type, String id, String label) {
		this.type = type;
		this.id = id;
		this.label = label;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public Object getExtraData() {
		return extraData;
	}

	public void getExtraData(Object extraData) {
		this.extraData = extraData;
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

	public LinkType getType() {
		return type;
	}

	public void setType(LinkType type) {
		this.type = type;
	}

}
