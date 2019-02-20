package org.monet.metamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

import org.monet.metamodel.ActivityDefinitionBase.ActivityPlacePropertyBase;
import org.monet.metamodel.ActivityDefinitionBase.ContestantsPropertyBase;
import org.monet.metamodel.ActivityDefinitionBase.ContestantsPropertyBase.ContestantRequestProperty;

/**
ActivityDefinition
Una tarea es una trabajo colectivo o individual que se desarrolla en la unidad de negocio

*/

public  class ActivityDefinition extends ActivityDefinitionBase {

  public static class ContestantsProperty extends ContestantsPropertyBase {

    private HashMap<String, ContestantRequestProperty> requestByCode = new HashMap<String, ActivityDefinitionBase.ContestantsPropertyBase.ContestantRequestProperty>();

    public void init() {
      for (ContestantRequestProperty request : this._contestantRequestPropertyMap.values())
        this.requestByCode.put(request.getCode(), request);
    }

    public ContestantRequestProperty getRequestByCode(String code) {
      return this.requestByCode.get(code);
    }

  }

  public static class ActivityPlaceProperty extends ActivityPlacePropertyBase {

  }	

}

