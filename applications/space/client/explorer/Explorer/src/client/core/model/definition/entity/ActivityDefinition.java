package client.core.model.definition.entity;

import client.core.model.List;

public interface ActivityDefinition extends ProcessDefinition {
	List<ProviderDefinition> getProviders();
	ProviderDefinition getProvider(String key);

	List<PlaceDefinition> getPlaces();
	PlaceDefinition getPlace(String key);
}
