/**
 * LDAP_BCwsdlPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.monet.federation.accountoffice.core.layers.auth.impl.micv.BCLDAPwsdl;

public interface LDAP_BCwsdlPortType extends java.rmi.Remote {

    /**
     * Confirma si el usuario tiene acceso a Mi Curriculum Vitae
     */
    public java.lang.String servicioMICV(java.lang.String usuario) throws java.rmi.RemoteException;

    /**
     * Para el menÃº MiULPGC:Confirma si el usuario tiene acceso a
     * Mi Curriculum Vitae
     */
    public java.lang.String servicioMICVmiulpgc(java.lang.String usuario) throws java.rmi.RemoteException;

    /**
     * Para el menÃº MiULPGC:Confirma si el usuario tiene acceso a
     * RPDI
     */
    public java.lang.String servicioRPDImiulpgc(java.lang.String usuario) throws java.rmi.RemoteException;

    /**
     * Confirma si el usuario tiene acceso a Invitaciones para Acceso
     * a la Red InalÃ¡mbrica
     */
    public java.lang.String servicioADM_WIFI(java.lang.String usuario) throws java.rmi.RemoteException;

    /**
     * Para el menÃº MiULPGC:Confirma si el usuario tiene acceso a
     * Invitaciones para Acceso a la Red InalÃ¡mbrica
     */
    public java.lang.String servicioADM_WIFImiulpgc(java.lang.String usuario) throws java.rmi.RemoteException;
}
