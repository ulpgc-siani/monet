package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public abstract class AbstractService extends BaseObject {
	private String partnerId;
	private String label;
	private String ontology;

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getOntology() {
		return this.ontology;
	}

	public void setOntology(String ontology) {
		this.ontology = ontology;
	}

	@Override
	public JSONObject toJson() {
		JSONObject result = new JSONObject();

		result.put("name", this.name);
		result.put("label", this.label);
		result.put("ontology", this.ontology);

		return result;
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

}
