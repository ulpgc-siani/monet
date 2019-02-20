package client.core.system.definition;

import client.core.model.List;
import client.core.system.MonetList;

import java.util.HashMap;
import java.util.Map;

public class Dictionary implements client.core.model.definition.Dictionary {
	private int definitionCount = 0;
	private Map<String, client.core.model.definition.Definition> definitionMap = new HashMap<>();

	public Dictionary() {
	}

	public Dictionary(List<client.core.model.definition.Definition> definitions) {
		setDefinitions(definitions);
	}

	@Override
	public String getDefinitionLabel(String key) {
		if (!definitionMap.containsKey(key))
			return "";
		return definitionMap.get(key).getLabel();
	}

	@Override
	public String getDefinitionCode(String key) {
		if (!definitionMap.containsKey(key))
			return "";
		return definitionMap.get(key).getCode();
	}

	@Override
	public List<String> getDefinitionCodes(List<client.core.model.definition.Ref> refList) {
		List<String> result = new MonetList();

		for (client.core.model.definition.Ref element : refList)
			result.add(getDefinitionCode(element.getValue()));

		return result;
	}

	@Override
	public int getDefinitionCount() {
		return definitionCount;
	}

	public void setDefinitions(List<client.core.model.definition.Definition> definitions) {
		this.definitionCount = definitions.size();

		definitionMap.clear();
		for (client.core.model.definition.Definition definition : definitions) {
			definitionMap.put(definition.getCode(), definition);
			definitionMap.put(definition.getName(), definition);
		}
	}
}
