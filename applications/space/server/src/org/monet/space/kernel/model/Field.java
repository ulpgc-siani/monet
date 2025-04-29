package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import org.monet.metamodel.FieldProperty;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class Field extends BaseObject {
	Node node;
	FieldProperty fieldDeclaration;
	Attribute attribute;
	String content;

	public static String getFilename(String nodeId, String name) {
		return withStamp(String.format("%s%s", nodeId, name));
	}

	public Field(Node node, Attribute attribute, FieldProperty fieldDeclaration) {
		this.node = node;
		this.attribute = attribute;
		this.fieldDeclaration = fieldDeclaration;
	}

	public Node getNode() {
		return this.node;
	}

	public Attribute getAttribute() {
		return this.attribute;
	}

	public <T extends FieldProperty> T getFieldDefinition() {
		return (T) this.fieldDeclaration;
	}

    public void setFieldDeclaration(FieldProperty fieldDeclaration) {
        this.fieldDeclaration = fieldDeclaration;
    }

	public String getIndicatorValue(String code) {
		if (this.attribute == null) return "";
		return this.attribute.getIndicatorValue(code);
	}

	public void setIndicatorValue(String code, String value) {
		Indicator indicator;

		if (this.attribute == null) return;

		indicator = this.attribute.getIndicator(code);

		if (indicator == null) {
			indicator = new Indicator(code, -1, value);
			this.attribute.getIndicatorList().add(indicator);
		}

		indicator.setData(value);
	}

	public String getCode() {
		return this.fieldDeclaration.getCode();
	}

	public String getName() {
		return this.fieldDeclaration.getName();
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		if (this.attribute != null)
			this.attribute.serializeToXML(serializer, depth);
	}

	public JSONObject toJson() {
		String content = this.getContent();
		Boolean isPartialLoading = this.isPartialLoading();
		JSONObject result = new JSONObject();

		if (isPartialLoading)
			this.disablePartialLoading();

		result.put("content", content);

		if (isPartialLoading)
			this.enablePartialLoading();

		return result;
	}

	private static String withStamp(String name) {
		return stamp() + "-" + name;
	}

	private static String stamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

}
