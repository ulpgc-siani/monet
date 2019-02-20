package org.monet.space.kernel.model;

import org.jdom.Element;
import org.monet.space.kernel.agents.AgentLogger;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CubeFact extends BaseObject {
	private Date time;
	private HashMap<String, Double> measures;
	private HashMap<String, String> components;

	public CubeFact() {
		this(null);
	}

	public CubeFact(Date time) {
		this.time = time;
		this.measures = new HashMap<>();
		this.components = new HashMap<>();
	}

	public Date getTime() {
		return this.time;
	}

	public HashMap<String, Double> getMeasures() {
		return this.measures;
	}

	public double getMeasure(String key) {
		return this.measures.get(key);
	}

	public void setMeasure(String key, double value) {
		this.measures.put(key, value);
	}

	public HashMap<String, String> getComponents() {
		return this.components;
	}

	public String getComponent(String key) {
		return this.components.get(key);
	}

	public void setComponent(String key, String component) {
		this.components.put(key, component);
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");

		serializer.startTag("", "cubefact");
		serializer.attribute("", "date", dateFormat.format(this.time));

		for (String measureKey : this.measures.keySet()) {
			serializer.startTag("", "measure");
			serializer.attribute("", "name", measureKey);
			serializer.attribute("", "value", String.valueOf(this.measures.get(measureKey)));
			serializer.endTag("", "measure");
		}

		for (String componentKey : this.components.keySet()) {
			serializer.startTag("", "component");
			serializer.attribute("", "name", componentKey);
			serializer.attribute("", "value", String.valueOf(this.components.get(componentKey)));
			serializer.endTag("", "component");
		}

		serializer.endTag("", "cubefact");
	}

	@Override
	public void unserializeFromXML(Element element) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");

		try {
			this.time = dateFormat.parse(element.getAttributeValue("date"));
		} catch (ParseException e) {
			AgentLogger.getInstance().error(e);
			return;
		}

		this.measures.clear();
		List<Element> measureList = element.getChildren("measure");
		for (Element measure : measureList)
			this.measures.put(measure.getAttributeValue("name"), Double.valueOf(measure.getAttributeValue("value")));

		this.components.clear();
		List<Element> componentList = element.getChildren("component");
		for (Element component : componentList)
			this.components.put(component.getAttributeValue("name"), component.getAttributeValue("value"));
	}
}
