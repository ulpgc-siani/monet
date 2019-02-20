package org.monet.space.kernel.model;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

public class NodeState {

	private HashSet<Enum<?>> nodeFlags = new HashSet<Enum<?>>();
	private HashMap<String, HashSet<Enum<?>>> fieldsFlags = new HashMap<String, HashSet<Enum<?>>>();
	private HashMap<String, HashSet<Enum<?>>> viewsFlags = new HashMap<String, HashSet<Enum<?>>>();
	private HashMap<String, HashSet<Enum<?>>> linksFlags = new HashMap<String, HashSet<Enum<?>>>();
	private HashMap<String, HashSet<Enum<?>>> operationsFlags = new HashMap<String, HashSet<Enum<?>>>();
	private Date date = new Date();

	public void setFlag(org.monet.metamodel.NodeDefinition.RuleNodeProperty.AddFlagEnumeration flag) {
		this.nodeFlags.add(flag);
	}

	public void setFieldFlag(String fieldCode, org.monet.metamodel.FormDefinition.RuleFormProperty.AddFlagEnumeration flag) {
		this.setFlag(this.fieldsFlags, fieldCode, flag);
	}

	public void setViewFlag(String viewCode, org.monet.metamodel.NodeDefinition.RuleViewProperty.AddFlagEnumeration flag) {
		this.setFlag(this.viewsFlags, viewCode, flag);
	}

	public void setLinkFlag(String linkCode, org.monet.metamodel.DesktopDefinition.RuleLinkProperty.AddFlagEnumeration flag) {
		this.setFlag(this.linksFlags, linkCode, flag);
	}

	public void setOperationFlag(String operationCode, org.monet.metamodel.NodeDefinition.RuleOperationProperty.AddFlagEnumeration flag) {
		this.setFlag(this.operationsFlags, operationCode, flag);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void setFlag(Map flagMap, String code, Enum flag) {
		HashSet flagSet = (HashSet) flagMap.get(code);
		if (flagSet == null) {
			flagSet = new HashSet();
			flagMap.put(code, flagSet);
		}
		flagSet.add(flag);
	}

	public boolean existsFlagForView(String codeView) {
		return viewsFlags.containsKey(codeView);
	}

	public boolean existsFlagForOperation(String codeOperation) {
		return operationsFlags.containsKey(codeOperation);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();

		JSONArray jsonNodeFlags = new JSONArray();
		for (Enum<?> flag : nodeFlags) {
			jsonNodeFlags.add(flag.toString());
		}
		json.put("flags", jsonNodeFlags);
		json.put("date", this.date.getTime());

		this.mapToJson(json, "fieldsFlags", this.fieldsFlags);
		this.mapToJson(json, "viewsFlags", this.viewsFlags);
		this.mapToJson(json, "linksFlags", this.linksFlags);
		this.mapToJson(json, "operationsFlags", this.operationsFlags);

		return json;
	}

	private void mapToJson(JSONObject json, String field, HashMap<String, HashSet<Enum<?>>> map) {
		JSONObject jsonMapFlags = new JSONObject();
		for (Entry<String, HashSet<Enum<?>>> flags : map.entrySet()) {
			JSONArray jsonFlags = new JSONArray();
			for (Enum<?> flag : flags.getValue())
				jsonFlags.add(flag.toString());
			jsonMapFlags.put(flags.getKey(), jsonFlags);
		}
		json.put(field, jsonMapFlags);
	}

}
