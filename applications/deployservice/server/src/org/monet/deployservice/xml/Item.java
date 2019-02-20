package org.monet.deployservice.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class Item {

	private Map<String, String> properties;
	private Map<String, Item> mitems;
	private List<Item> litems;
	private String content;
	private Logger logger;

	public Item() {
		logger = Logger.getLogger(this.getClass());
		properties = new HashMap<String, String>();
		mitems = new HashMap<String, Item>();
		litems = new ArrayList<Item>();
	}

	public void setProperty(String key, String value) {
		properties.put(key, value);
	}

	public String getProperty(String key) {
		if (properties.containsKey(key)) {
			if (properties.get(key) == null) return "";
			return properties.get(key);
		} else
			return "";
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
	
	public Integer getCount() {
		return mitems.size();
	}

	public void addItem(Item item) {
		mitems.put(item.getProperty("id"), item);
		litems.add(item);
	}

	public Item getItem(String id) {
		Item result = null;
		try {
			result = mitems.get(id);
		} catch (Exception e) {
			String error = "";
			if (e != null)
				error = e.getMessage();
			logger.error("Fail to get item. Details: " + error);
		}

		return result;
	}

	public List<Item> getItems() {
		if (litems == null)
			logger.error("Fail to get items.");				
		
		return litems;
	}
}