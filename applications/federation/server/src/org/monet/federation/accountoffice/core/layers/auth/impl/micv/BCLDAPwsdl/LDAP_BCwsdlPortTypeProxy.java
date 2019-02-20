package org.monet.federation.accountoffice.core.layers.auth.impl.micv.BCLDAPwsdl;

public class LDAP_BCwsdlPortTypeProxy implements LDAP_BCwsdlPortType {
  private String _endpoint = null;
  private LDAP_BCwsdlPortType lDAP_BCwsdlPortType = null;
  
  public LDAP_BCwsdlPortTypeProxy() {
    _initLDAP_BCwsdlPortTypeProxy();
  }
  
  public LDAP_BCwsdlPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initLDAP_BCwsdlPortTypeProxy();
  }
  
  private void _initLDAP_BCwsdlPortTypeProxy() {
    try {
      lDAP_BCwsdlPortType = (new LDAP_BCwsdlLocator()).getLDAP_BCwsdlPort();
      if (lDAP_BCwsdlPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)lDAP_BCwsdlPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)lDAP_BCwsdlPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (lDAP_BCwsdlPortType != null)
      ((javax.xml.rpc.Stub)lDAP_BCwsdlPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public LDAP_BCwsdlPortType getLDAP_BCwsdlPortType() {
    if (lDAP_BCwsdlPortType == null)
      _initLDAP_BCwsdlPortTypeProxy();
    return lDAP_BCwsdlPortType;
  }
  
  public java.lang.String servicioMICV(java.lang.String usuario) throws java.rmi.RemoteException{
    if (lDAP_BCwsdlPortType == null)
      _initLDAP_BCwsdlPortTypeProxy();
    return lDAP_BCwsdlPortType.servicioMICV(usuario);
  }
  
  public java.lang.String servicioMICVmiulpgc(java.lang.String usuario) throws java.rmi.RemoteException{
    if (lDAP_BCwsdlPortType == null)
      _initLDAP_BCwsdlPortTypeProxy();
    return lDAP_BCwsdlPortType.servicioMICVmiulpgc(usuario);
  }
  
  public java.lang.String servicioRPDImiulpgc(java.lang.String usuario) throws java.rmi.RemoteException{
    if (lDAP_BCwsdlPortType == null)
      _initLDAP_BCwsdlPortTypeProxy();
    return lDAP_BCwsdlPortType.servicioRPDImiulpgc(usuario);
  }
  
  public java.lang.String servicioADM_WIFI(java.lang.String usuario) throws java.rmi.RemoteException{
    if (lDAP_BCwsdlPortType == null)
      _initLDAP_BCwsdlPortTypeProxy();
    return lDAP_BCwsdlPortType.servicioADM_WIFI(usuario);
  }
  
  public java.lang.String servicioADM_WIFImiulpgc(java.lang.String usuario) throws java.rmi.RemoteException{
    if (lDAP_BCwsdlPortType == null)
      _initLDAP_BCwsdlPortTypeProxy();
    return lDAP_BCwsdlPortType.servicioADM_WIFImiulpgc(usuario);
  }
  
  
}