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

// ProviderDefinition
// Declaración que se utiliza para modelar un proveedor de una unidad de negocio

public abstract class ProviderDefinition extends InteroperabilityDefinition 
 {

protected @Attribute(name="remote-link") String _remoteLink;

public String getRemoteLink() { return _remoteLink; }

}







































