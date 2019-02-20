package client.core.system.definition.entity;

import client.core.model.List;
import client.core.model.definition.Ref;
import client.core.system.definition.Definition;

import java.util.HashMap;
import java.util.Map;

public class ActivityDefinition extends ProcessDefinition implements client.core.model.definition.entity.ActivityDefinition {
	private List<client.core.model.definition.entity.ProviderDefinition> providerList;
	private Map<String, client.core.model.definition.entity.ProviderDefinition> providerMap = new HashMap<>();
	private List<client.core.model.definition.entity.PlaceDefinition> placeList;
	private Map<String, client.core.model.definition.entity.PlaceDefinition> placeMap = new HashMap<>();

	public ActivityDefinition() {
	}

	@Override
	public List<client.core.model.definition.entity.ProviderDefinition> getProviders() {
		return providerList;
	}

	@Override
	public client.core.model.definition.entity.ProviderDefinition getProvider(String key) {
		return providerMap.get(key);
	}

	public void setProviders(List<client.core.model.definition.entity.ProviderDefinition> providers) {
		providerList = providers;

		providerMap.clear();
		for (client.core.model.definition.entity.ProviderDefinition provider : providers) {
			providerMap.put(provider.getCode(), provider);
			providerMap.put(provider.getName(), provider);
		}
	}

	@Override
	public List<client.core.model.definition.entity.PlaceDefinition> getPlaces() {
		return placeList;
	}

	@Override
	public client.core.model.definition.entity.PlaceDefinition getPlace(String key) {
		return placeMap.get(key);
	}

	public void setPlaces(List<client.core.model.definition.entity.PlaceDefinition> places) {
		placeList = places;

		placeMap.clear();
		for (client.core.model.definition.entity.PlaceDefinition place : places) {
			placeMap.put(place.getCode(), place);
			placeMap.put(place.getName(), place);
		}
	}

	public static class ProviderDefinition extends Definition implements client.core.model.definition.entity.ProviderDefinition {
		private Ref role;

		@Override
		public Ref getRole() {
			return role;
		}

		public void setRole(Ref role) {
			this.role = role;
		}
	}

}
