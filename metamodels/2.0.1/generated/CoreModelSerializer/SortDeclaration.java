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







































