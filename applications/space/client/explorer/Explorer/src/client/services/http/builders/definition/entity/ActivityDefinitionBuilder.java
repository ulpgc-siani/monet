package client.services.http.builders.definition.entity;

import client.core.model.List;
import client.core.model.definition.entity.PlaceDefinition;
import client.core.model.definition.entity.ProviderDefinition;
import client.core.model.workmap.Action;
import client.core.system.MonetList;
import client.core.system.definition.entity.ActivityDefinition;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.definition.RefBuilder;
import com.google.gwt.core.client.JsArray;

import static client.core.model.workmap.Action.Type.*;

public class ActivityDefinitionBuilder extends ProcessDefinitionBuilder<client.core.model.definition.entity.ActivityDefinition> {

	@Override
	public client.core.model.definition.entity.ActivityDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		ActivityDefinition definition = new ActivityDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.ActivityDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		ActivityDefinition definition = (ActivityDefinition)object;

		definition.setProviders(getProviders(instance.getList("providers")));
		definition.setPlaces(getPlaces(instance.getList("places")));
	}

	private List<ProviderDefinition> getProviders(HttpList providerList) {
		List<ProviderDefinition> result = new MonetList<>();

		for (int i=0; i<providerList.getItems().length(); i++)
			result.add(getProvider(providerList.getItems().get(0)));

		return result;
	}

	private ProviderDefinition getProvider(HttpInstance instance) {
		ActivityDefinition.ProviderDefinition providerDefinition = new ActivityDefinition.ProviderDefinition();

		providerDefinition.setCode(instance.getString("code"));
		providerDefinition.setName(instance.getString("name"));
		providerDefinition.setRole(new RefBuilder().build(instance.getObject("role")));

		return providerDefinition;
	}

	private List<PlaceDefinition> getPlaces(HttpList placeList) {
		List<PlaceDefinition> result = new MonetList<>();

		for (int i=0; i<placeList.getItems().length(); i++)
			result.add(getPlace(placeList.getItems().get(i)));

		return result;
	}

	private PlaceDefinition getPlace(HttpInstance instance) {
		client.core.system.definition.entity.PlaceDefinition placeDefinition = new client.core.system.definition.entity.PlaceDefinition();
		placeDefinition.setCode(instance.getString("code"));
		placeDefinition.setName(instance.getString("name"));
		placeDefinition.setAction(getPlaceAction(instance.getObject("action")));
		return placeDefinition;
	}

	private PlaceDefinition.ActionDefinition getPlaceAction(HttpInstance instance) {
		if (instance == null)
			return null;

		Action.Type type = Action.Type.fromString(instance.getString("type"));

        if (type == DELEGATION)
            return getPlaceDelegationAction(instance);
        if (type == LINE)
            return getPlaceLineAction(instance);
        if (type == EDITION)
            return getPlaceEditionAction(instance);
        if (type == WAIT)
            return getPlaceWaitAction(instance);
        return null;
	}

	private PlaceDefinition.ActionDefinition getPlaceDelegationAction(HttpInstance instance) {
		client.core.system.definition.entity.PlaceDefinition.DelegationActionDefinition definition = new client.core.system.definition.entity.PlaceDefinition.DelegationActionDefinition();
		definition.setLabel(instance.getString("label"));
		definition.setProvider(new RefBuilder().build(instance.getObject("provider")));
		return definition;
	}

	private PlaceDefinition.ActionDefinition getPlaceLineAction(HttpInstance instance) {
		client.core.system.definition.entity.PlaceDefinition.LineActionDefinition definition = new client.core.system.definition.entity.PlaceDefinition.LineActionDefinition();
		definition.setLabel(instance.getString("label"));
		definition.setTimeoutDefinition(getPlaceLineActionTimeout(instance.getObject("timeout")));
		definition.setStopDefinitions(getPlaceLineActionStops(instance.getArray("stop")));
		return definition;
	}

	public PlaceDefinition.LineActionDefinition.TimeoutDefinition getPlaceLineActionTimeout(HttpInstance instance) {
        if (instance == null) return null;
		client.core.system.definition.entity.PlaceDefinition.LineActionDefinition.TimeoutDefinition timeoutDefinition = new client.core.system.definition.entity.PlaceDefinition.LineActionDefinition.TimeoutDefinition();
		timeoutDefinition.setTake(new RefBuilder().build(instance.getObject("take")));
		return timeoutDefinition;
	}

	public PlaceDefinition.LineActionDefinition.LineStopDefinition[] getPlaceLineActionStops(JsArray instances) {
		List<PlaceDefinition.LineActionDefinition.LineStopDefinition> result = new MonetList<>();

		for (int i = 0; i < instances.length(); i++)
			result.add(getPlaceLineActionStop((HttpInstance) instances.get(i)));

		return result.toArray(new PlaceDefinition.LineActionDefinition.LineStopDefinition[result.size()]);
	}

	private PlaceDefinition.LineActionDefinition.LineStopDefinition getPlaceLineActionStop(HttpInstance instance) {
		client.core.system.definition.entity.PlaceDefinition.LineActionDefinition.LineStopDefinition definition = new client.core.system.definition.entity.PlaceDefinition.LineActionDefinition.LineStopDefinition();
		definition.setCode(instance.getString("code"));
		definition.setLabel(instance.getString("label"));
		return definition;
	}

	private PlaceDefinition.ActionDefinition getPlaceEditionAction(HttpInstance instance) {
		client.core.system.definition.entity.PlaceDefinition.EditionActionDefinition definition = new client.core.system.definition.entity.PlaceDefinition.EditionActionDefinition();
		definition.setLabel(instance.getString("label"));
		return definition;
	}

	private PlaceDefinition.ActionDefinition getPlaceWaitAction(HttpInstance instance) {
		client.core.system.definition.entity.PlaceDefinition.WaitActionDefinition definition = new client.core.system.definition.entity.PlaceDefinition.WaitActionDefinition();
		definition.setLabel(instance.getString("label"));
		return definition;
	}

}