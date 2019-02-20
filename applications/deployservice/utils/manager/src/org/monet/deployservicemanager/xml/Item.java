package org.monet.deployservicemanager.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item {

	private Map<String, String> properties;
	private Map<String, Item> mitems;
	private List<Item> litems;
	private String content;
	
	public Item() { 
		  properties = new HashMap<String, String>();
		  mitems = new HashMap<String, Item>();
		  litems = new ArrayList<Item>();
	}
			
	public void setProperty(String key, String value) {
		properties.put(key, value);
	}
	
	public String getProperty(String key) {
		if (properties.containsKey(key)) 
	    return properties.get(key);
		else
			return "";		
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
		
  public void addItem(Item item) {
  	mitems.put(item.getProperty("id"), item);
  	litems.add(item);
	}
		
	public Item getItem(String id) {	
		return mitems.get(id);
	}

	public List<Item> getItems() {
		return litems;
	}
}