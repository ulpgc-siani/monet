package org.monet.kernel.model.definition;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

// ServiceLockDeclaration
// Declaración de un bloqueo de servicio. Estos bloqueos realizan una petición de servicio y se resuelven automáticamente cuando el servicio termina

@Root(name="service-lock")
public  class ServiceLockDeclaration extends WorklockDeclaration 
 {
@Root(name="use")
public static class Use {
protected @Attribute(name="service-provider") String _serviceProvider;
public String getServiceProvider() { return _serviceProvider; }
}
@Root(name="request")
public static class Request {
protected @Attribute(name="document") String _document;
public String getDocument() { return _document; }
}
@Root(name="response")
public static class Response {
protected @Attribute(name="document") String _document;
public String getDocument() { return _document; }
}

protected @Element(name="use") Use _use;
protected @Element(name="request") Request _request;
protected @Element(name="response",required=false) Response _response;

public Use getUse() { return _use; }
public Request getRequest() { return _request; }
public Response getResponse() { return _response; }

}







































