package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

// ResolveDeclaration
// Declaración de una variable genérica

@Root(name="resolve")
public  class ResolveDeclaration extends Declaration 
 {

protected @Attribute(name="generic") String _generic;
protected @Attribute(name="with") String _with;

public String getGeneric() { return _generic; }
public String getWith() { return _with; }

}







































