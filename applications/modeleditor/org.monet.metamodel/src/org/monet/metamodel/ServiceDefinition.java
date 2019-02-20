package org.monet.metamodel;

/**
 * ServiceDefinition Una tarea es una trabajo colectivo o individual que se
 * desarrolla en la unidad de negocio
 */

public class ServiceDefinition extends ServiceDefinitionBase {

  public static class CustomerProperty extends CustomerPropertyBase {

    public CustomerRequestProperty getRequestByCode(String code) {
      return null;
    }

  }

  public static class ServicePlaceProperty extends ServicePlacePropertyBase {

  }

}
