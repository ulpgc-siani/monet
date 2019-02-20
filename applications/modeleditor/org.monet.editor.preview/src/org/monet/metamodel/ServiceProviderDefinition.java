package org.monet.metamodel;

/**
 * ServiceProviderDefinition Un proveedor de servicio es un tipo de proveedor
 * que se utiliza para solicitar servicios a otras unidades de negocio
 */

public class ServiceProviderDefinition extends ProviderDefinition {

  protected org.monet.metamodel.internal.Ref _request;

  public org.monet.metamodel.internal.Ref getRequest() {
    return _request;
  }

  public void setRequest(org.monet.metamodel.internal.Ref value) {
    _request = value;
  }

  protected org.monet.metamodel.internal.Ref _response;

  public org.monet.metamodel.internal.Ref getResponse() {
    return _response;
  }

  public void setResponse(org.monet.metamodel.internal.Ref value) {
    _response = value;
  }

  public void merge(ServiceProviderDefinition child) {
    super.merge(child);

    if (child._request != null)
      this._request = child._request;
    if (child._response != null)
      this._response = child._response;

  }

  public Class<?> getMetamodelClass() {
    return ServiceProviderDefinition.class;
  }

}
