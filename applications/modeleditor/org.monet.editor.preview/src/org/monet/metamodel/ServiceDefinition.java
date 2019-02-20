package org.monet.metamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

import org.monet.metamodel.ServiceDefinitionBase.CustomerPropertyBase;
import org.monet.metamodel.ServiceDefinitionBase.ServicePlacePropertyBase;
import org.monet.metamodel.ServiceDefinitionBase.CustomerPropertyBase.CustomerRequestProperty;

/**
ServiceDefinition
Una tarea es una trabajo colectivo o individual que se desarrolla en la unidad de negocio

*/

public class ServiceDefinition extends ServiceDefinitionBase {

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

