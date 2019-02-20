package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;

// IndexedDeclaration
// Declaración referenciables por nombre en el modelo de negocio

public abstract class IndexedDeclaration extends Declaration 
 {

protected @Attribute(name="code") String _code;
protected @Attribute(name="name") String _name;

public String getCode() { return _code; }
public String getName() { return _name; }

}







































