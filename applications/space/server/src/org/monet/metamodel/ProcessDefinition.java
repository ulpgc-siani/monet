package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;

import java.util.HashMap;
import java.util.Map;

/**
 * ProcessDefinition Una tarea es una trabajo colectivo o individual que se
 * desarrolla en la unidad de negocio
 */

public abstract class ProcessDefinition extends ProcessDefinitionBase implements IsInitiable {
	private ViewProperty defaultView = null;
	protected PlaceProperty initialPlace = null;
	protected Map<String, PlaceProperty> placesMap = new HashMap<String, PlaceProperty>();
	protected Map<String, ViewProperty> viewsMap = new HashMap<String, ViewProperty>();

	public void init() {
		for (ViewProperty view : this._viewPropertyMap.values()) {
			if (view.isDefault())
				defaultView = view;

			this.viewsMap.put(view.getCode(), view);
			this.viewsMap.put(view.getName(), view);
		}

		HashMap<String, TaskProviderProperty> taskProviderPropertyMap = new HashMap<String, TaskProviderProperty>();
		for (TaskProviderProperty providerDefinition : this._taskProviderPropertyMap.values()) {
			taskProviderPropertyMap.put(providerDefinition.getCode(), providerDefinition);
			taskProviderPropertyMap.put(providerDefinition.getName(), providerDefinition);
		}
		this._taskProviderPropertyMap.clear();
		this._taskProviderPropertyMap.putAll(taskProviderPropertyMap);

		HashMap<String, TaskContestProperty> taskContestPropertyMap = new HashMap<String, TaskContestProperty>();
		for (TaskContestProperty contestDefinition : this._taskContestPropertyMap.values()) {
			taskContestPropertyMap.put(contestDefinition.getCode(), contestDefinition);
			taskContestPropertyMap.put(contestDefinition.getName(), contestDefinition);
		}
		this._taskContestPropertyMap.clear();
		this._taskContestPropertyMap.putAll(taskContestPropertyMap);

	}

	public ViewProperty getDefaultView() {
		return this.defaultView;
	}

	public ViewProperty getTaskViewDeclaration(String key) {
		return this.viewsMap.get(key);
	}

	public Class<?> getSubBehaviorClass(String name) {
		return null;
	}

	public PlaceProperty getInitialPlace() {
		return this.initialPlace;
	}

	public PlaceProperty getPlace(String place) {
		return this.placesMap.get(place);
	}
}
