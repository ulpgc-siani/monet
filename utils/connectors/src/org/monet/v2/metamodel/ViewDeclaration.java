package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;

// ViewDeclaration
// Declaración abstracta de una vista

public abstract class ViewDeclaration extends Declaration 
 {

protected @Attribute(name="code") String _code;
protected @Attribute(name="name",required=false) String _name;

public String getCode() { return _code; }
public String getName() { return _name; }

}







































