package org.monet.editor.core.parsers.form;


public class Property extends PropertyList {
	private static final long serialVersionUID = -1417606715473573677L;
	private String name;
	private String type;
	private Boolean isMultiple;

	public Property(String name, String type) {
		super();
		this.name = name;
		this.type = type;
		this.isMultiple = false;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
	
	public Boolean isMultiple() {
		return isMultiple;
	}
	
	public void isMultiple(boolean value) {
		isMultiple = value;
	}

}
