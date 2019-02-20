/**
 * LDAP_BCwsdlLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.monet.federation.accountoffice.core.layers.auth.impl.micv.BCLDAPwsdl;

@SuppressWarnings("serial")
public class LDAP_BCwsdlLocator extends org.apache.axis.client.Service implements LDAP_BCwsdl {

    public LDAP_BCwsdlLocator() {
    }


    public LDAP_BCwsdlLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public LDAP_BCwsdlLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for LDAP_BCwsdlPort
    private java.lang.String LDAP_BCwsdlPort_address = "https://ldapbc.ulpgc.es:443/index.php";

    public java.lang.String getLDAP_BCwsdlPortAddress() {
        return LDAP_BCwsdlPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LDAP_BCwsdlPortWSDDServiceName = "LDAP_BCwsdlPort";

    public java.lang.String getLDAP_BCwsdlPortWSDDServiceName() {
        return LDAP_BCwsdlPortWSDDServiceName;
    }

    public void setLDAP_BCwsdlPortWSDDServiceName(java.lang.String name) {
        LDAP_BCwsdlPortWSDDServiceName = name;
    }

    public LDAP_BCwsdlPortType getLDAP_BCwsdlPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LDAP_BCwsdlPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getLDAP_BCwsdlPort(endpoint);
    }

    public LDAP_BCwsdlPortType getLDAP_BCwsdlPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            LDAP_BCwsdlBindingStub _stub = new LDAP_BCwsdlBindingStub(portAddress, this);
            _stub.setPortName(getLDAP_BCwsdlPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setLDAP_BCwsdlPortEndpointAddress(java.lang.String address) {
        LDAP_BCwsdlPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    @SuppressWarnings("rawtypes")
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (LDAP_BCwsdlPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                LDAP_BCwsdlBindingStub _stub = new LDAP_BCwsdlBindingStub(new java.net.URL(LDAP_BCwsdlPort_address), this);
                _stub.setPortName(getLDAP_BCwsdlPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    @SuppressWarnings("rawtypes")
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("LDAP_BCwsdlPort".equals(inputPortName)) {
            return getLDAP_BCwsdlPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:BCLDAPwsdl", "LDAP_BCwsdl");
    }

    @SuppressWarnings("rawtypes")
    private java.util.HashSet ports = null;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:BCLDAPwsdl", "LDAP_BCwsdlPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("LDAP_BCwsdlPort".equals(portName)) {
            setLDAP_BCwsdlPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
