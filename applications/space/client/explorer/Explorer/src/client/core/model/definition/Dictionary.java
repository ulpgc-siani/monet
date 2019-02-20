package client.core.model.definition;

import client.core.model.List;

import static client.core.model.Instance.ClassName;

public interface Dictionary {

	ClassName CLASS_NAME = new ClassName("Model");

	String getDefinitionLabel(String key);
	String getDefinitionCode(String key);
	List<String> getDefinitionCodes(List<Ref> refList);
	int getDefinitionCount();
}
