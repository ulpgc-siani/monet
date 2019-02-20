package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

// RoleDefinition
// Definition que se utiliza para modelar un rol que ejerce alguno de los trabajadores del espacio de negocio

@Root(name="role")
public  class RoleDefinition extends EntityDefinition 
 {
@Root(name="use")
public static class Use {
protected @Attribute(name="environment") String _environment;
public String getEnvironment() { return _environment; }
}

protected @Element(name="use",required=false) Use _use;

public Use getUse() { return _use; }

}







































