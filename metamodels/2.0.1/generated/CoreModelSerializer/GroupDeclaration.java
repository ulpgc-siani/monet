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







































