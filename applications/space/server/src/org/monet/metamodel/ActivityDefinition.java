package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;

/**
 * ActivityDefinition Una tarea es una trabajo colectivo o individual que se
 * desarrolla en la unidad de negocio
 */

public class ActivityDefinition extends ActivityDefinitionBase implements IsInitiable {

	public void init() {
		super.init();

		for (PlaceProperty place : this._activityPlacePropertyMap.values()) {
			this.placesMap.put(place.getCode(), place);
			this.placesMap.put(place.getName(), place);

			if (place.isInitial())
				this.initialPlace = place;

			place.init();
		}

// CONTESTANTS
//		if (this._contestantsProperty != null)
//			this._contestantsProperty.init();
	}

	public static class ContestantsProperty /*extends ContestantsPropertyBase */{

//		private HashMap<String, ContestantRequestProperty> requestByCode = new HashMap<String, ActivityDefinitionBase.ContestantsPropertyBase.ContestantRequestProperty>();

//		public void init() {
//			for (ContestantRequestProperty request : this._contestantRequestPropertyMap.values())
//				this.requestByCode.put(request.getCode(), request);
//		}

//		public ContestantRequestProperty getRequestByCode(String code) {
//			return this.requestByCode.get(code);
//		}

	}

	public static class ActivityPlaceProperty extends ActivityPlacePropertyBase {

	}

}
