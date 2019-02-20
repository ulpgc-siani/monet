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

// RoleDefinition
// Definition que se utiliza para modelar un rol que ejerce alguno de los trabajadores del espacio de negocio

@Root(name="role")
public  class RoleDefinition extends EntityDefinition 
 {
@Root(name = "use"
)
public static class Use {
protected @Attribute(name="environment") String _environment;
public String getEnvironment() { return _environment; }
public void setEnvironment(String value) { _environment = value; }
}

protected @Element(name="use",required=false) Use _use;

public Use getUse() { return _use; }
public void setUse(Use value) { _use = value; }

}







































