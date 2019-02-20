package org.monet.api.space.backservice.impl.model;

import org.jdom.Element;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DimensionComponent extends BaseObject {
	private String dimension;
	private HashMap<String, ArrayList<Object>> features = new HashMap<String, ArrayList<Object>>();

	public DimensionComponent(String dimension) {
		super();
		this.dimension = dimension;
		this.features = new HashMap<String, ArrayList<Object>>();
	}

	public String getDimension() {
		return this.dimension;
	}

	public void addFeature(String key, double value) {
		this.addGenericFeature(key, value);
	}

	public void addFeature(String key, boolean value) {
		this.addGenericFeature(key, value);
	}

	public void addFeature(String key, String value) {
		this.addGenericFeature(key, value);
	}

	public void addFeature(String key, Term value, ArrayList<Term> ancestors) {
		Term term = (Term) value;
		String code = term.getCode();
		String label = term.getLabel();

		if (ancestors == null)
			ancestors = new ArrayList<Term>();

		for (Term ancestor : ancestors) {
			code = ancestor.getCode() + "@" + code;
			label = ancestor.getLabel() + "@" + label;
		}

		this.addGenericFeature(key, label);
		this.addGenericFeature(key + "_extra", code);
	}

	public Object getFeatureValue(String code) {
		if (!this.features.containsKey(code))
			return null;
		if (this.features.get(code).size() == 0)
			return null;
		return this.features.get(code).get(0);
	}

	public ArrayList<Object> getFeatureValues(String code) {
		if (!this.features.containsKey(code))
			return null;
		return this.features.get(code);
	}

	public HashMap<String, ArrayList<Object>> getFeatures() {
		return this.features;
	}

	@Override
	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "dimensioncomponent");
		serializer.attribute("", "id", this.id);
		serializer.attribute("", "dimension", this.dimension);

		for (String feature : this.features.keySet()) {
			ArrayList<Object> values = this.features.get(feature);

			serializer.startTag("", "feature");
			serializer.attribute("", "name", feature);

			for (Object value : values) {
				serializer.startTag("", "value");
				serializer.text(String.valueOf(value));
				serializer.endTag("", "value");
			}

			serializer.endTag("", "feature");
		}

		serializer.endTag("", "dimensioncomponent");
	}

	@Override
	public void deserializeFromXML(Element element) throws ParseException {
		if (element.getAttribute("dimension") != null)
			this.dimension = element.getAttributeValue("dimension");

		this.features.clear();
		List<Element> features = element.getChildren("feature");
		for (final Element feature : features) {
			ArrayList<Object> featureValues = new ArrayList<Object>();
			List<Element> values = feature.getChildren();

			for (Element value : values)
				featureValues.add(value.getText());

			this.features.put(feature.getAttributeValue("name"), featureValues);
		}
	}

	private void addGenericFeature(String key, Object value) {
		if (!this.features.containsKey(key))
			this.features.put(key, new ArrayList<Object>());
		this.features.get(key).add(value);
	}

}
