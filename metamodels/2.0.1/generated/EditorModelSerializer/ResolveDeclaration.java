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

// ResolveDeclaration
// Declaración de una variable genérica

@Root(name="resolve")
public  class ResolveDeclaration extends Declaration 
 {

protected @Attribute(name="generic") String _generic;
protected @Attribute(name="with") String _with;

public String getGeneric() { return _generic; }
public void setGeneric(String value) { _generic = value; }
public String getWith() { return _with; }
public void setWith(String value) { _with = value; }

}







































