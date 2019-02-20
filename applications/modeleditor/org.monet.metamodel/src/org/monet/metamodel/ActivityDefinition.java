package org.monet.metamodel;

import org.monet.metamodel.ServiceDefinitionBase.CustomerPropertyBase.CustomerRequestProperty;

/**
 * ActivityDefinition Una tarea es una trabajo colectivo o individual que se
 * desarrolla en la unidad de negocio
 */

public class ActivityDefinition extends ActivityDefinitionBase {

  public static class ContestantsProperty /*extends ContestantsPropertyBase */{

    public CustomerRequestProperty getRequestByCode(String code) {
      return null;
    }

  }

  public static class ActivityPlaceProperty extends ActivityPlacePropertyBase {

  }

}
