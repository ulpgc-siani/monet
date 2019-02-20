package org.monet.bpi.types;

import java.util.HashMap;
import java.util.Map;

public class Event {
	private String name;
	private Date dueDate;
	private final Map<String, String> properties;

	public Event() {
		this(null, null);
	}

	public Event(String name, Date dueDate) {
		this(name, dueDate, new HashMap<String, String>());
	}

	public Event(String name, Date dueDate, Map<String, String> properties) {
		this.name = name;
		this.dueDate = dueDate;
		this.properties = properties;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public String getProperty(String name) {
		return properties.get(name);
	}

	public void addProperty(String name, String value) {
	  properties.put(name, value);
	}
}
