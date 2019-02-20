package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

// SortDeclaration
// Se utiliza para indicar que atributos de la referencia de la colección se van a utilizar para ordenar los elementos de la lista

@Root(name="sort")
public  class SortDeclaration extends Declaration 
 {
public enum ModeEnumeration { ASC,DESC }

protected @Attribute(name="attribute") String _attribute;
protected @Attribute(name="mode",required=false) ModeEnumeration _mode;

public String getAttribute() { return _attribute; }
public ModeEnumeration getMode() { return _mode; }

}







































