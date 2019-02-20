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

// IndexedDeclaration
// Declaración referenciables por nombre en el modelo de negocio

public abstract class IndexedDeclaration extends Declaration 
 {

protected @Attribute(name="code") String _code;
protected @Attribute(name="name") String _name;

public String getCode() { return _code; }
public String getName() { return _name; }

}







































