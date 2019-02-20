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

// ViewDeclaration
// Declaración abstracta de una vista

public abstract class ViewDeclaration extends Declaration 
 {

protected @Attribute(name="code") String _code;
protected @Attribute(name="name",required=false) String _name;

public String getCode() { return _code; }
public void setCode(String value) { _code = value; }
public String getName() { return _name; }
public void setName(String value) { _name = value; }

}







































