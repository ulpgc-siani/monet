package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;

// ProviderDefinition
// Declaración que se utiliza para modelar un proveedor de una unidad de negocio

public abstract class ProviderDefinition extends InteroperabilityDefinition 
 {

protected @Attribute(name="remote-link") String _remoteLink;

public String getRemoteLink() { return _remoteLink; }

}







































