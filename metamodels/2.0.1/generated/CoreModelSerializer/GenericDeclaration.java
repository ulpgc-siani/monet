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

// GenericDeclaration
// Declaración de una variable genérica

@Root(name="generic")
public  class GenericDeclaration extends Declaration 
 {
public enum OfTypeEnumeration { CATALOG,COLLECTION,CONTAINER,DESKTOP,DOCUMENT,FORM,REFERENCE,TASK,THESAURUS }

protected @Attribute(name="name") String _name;
protected @Attribute(name="extends",required=false) String _extends;
protected @Attribute(name="of-type",required=false) OfTypeEnumeration _ofType;

public String getName() { return _name; }
public String getExtends() { return _extends; }
public OfTypeEnumeration getOfType() { return _ofType; }

}







































