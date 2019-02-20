package org.monet.modelling.kernel.model;

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

// ServiceProviderDefinition
// Declaración que se utiliza para modelar un proveedor de una unidad de negocio

@Root(name="service-provider")
public  class ServiceProviderDefinition extends ProviderDefinition 
 {
@Root(name = "request"
)
public static class Request {
protected @Attribute(name="document") String _document;
public String getDocument() { return _document; }
public void setDocument(String value) { _document = value; }
}
@Root(name = "response"
)
public static class Response {
protected @Attribute(name="document") String _document;
public String getDocument() { return _document; }
public void setDocument(String value) { _document = value; }
}
@Root(name = "implements"
)
public static class Implements {
protected @Attribute(name="cube") String _cube;
public String getCube() { return _cube; }
public void setCube(String value) { _cube = value; }
}

protected @Element(name="request") Request _request;
protected @Element(name="response",required=false) Response _response;
protected @ElementList(inline=true,required=false) ArrayList<Implements> _implementsList = new ArrayList<Implements>();

public Request getRequest() { return _request; }
public void setRequest(Request value) { _request = value; }
public Response getResponse() { return _response; }
public void setResponse(Response value) { _response = value; }
public ArrayList<Implements> getImplementsList() { return _implementsList; }

}







































