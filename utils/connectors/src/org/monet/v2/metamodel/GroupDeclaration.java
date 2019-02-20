package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

// GroupDeclaration
// Se utiliza para indicar que atributos de la referencia de la colección se van a utilizar para agrupar los elementos de la lista

@Root(name="group")
public  class GroupDeclaration extends Declaration 
 {
public enum FunctionEnumeration { MONTH,YEAR }

protected @Attribute(name="attribute") String _attribute;
protected @Attribute(name="function",required=false) FunctionEnumeration _function;

public String getAttribute() { return _attribute; }
public FunctionEnumeration getFunction() { return _function; }

}







































