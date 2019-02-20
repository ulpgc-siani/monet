package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;

import java.util.HashMap;

/**
 * ServiceDefinition Una tarea es una trabajo colectivo o individual que se
 * desarrolla en la unidad de negocio
 */

public class ServiceDefinition extends ServiceDefinitionBase implements IsInitiable {

	public void init() {
		super.init();

		for (PlaceProperty place : this._servicePlacePropertyMap.values()) {
			this.placesMap.put(place.getCode(), place);
			this.placesMap.put(place.getName(), place);

			if (place.isInitial())
				this.initialPlace = place;

			place.init();
		}

		if (this._customerProperty != null)
			this._customerProperty.init();
	}

	public static class CustomerProperty extends CustomerPropertyBase {

		private HashMap<String, CustomerRequestProperty> requestByCode = new HashMap<String, ServiceDefinitionBase.CustomerPropertyBase.CustomerRequestProperty>();

		public void init() {
			for (CustomerRequestProperty request : this._customerRequestPropertyMap.values())
				this.requestByCode.put(request.getCode(), request);
		}

		public CustomerRequestProperty getRequestByCode(String code) {
			return this.requestByCode.get(code);
		}

	}

	public static class ServicePlaceProperty extends ServicePlacePropertyBase {

	}

}
