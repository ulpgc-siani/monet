package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

// ExpressionDeclaration
// Declaración que se utiliza para modelar una expressión en una formula

@Root(name="expression")
public  class ExpressionDeclaration extends Declaration 
 {
public enum TypeEnumeration { INDICATOR,CONSTANT }

protected @Attribute(name="type") TypeEnumeration _type;
protected @Attribute(name="indicator",required=false) String _indicator;
protected @Attribute(name="value",required=false) double _value;

public TypeEnumeration getType() { return _type; }
public String getIndicator() { return _indicator; }
public double getValue() { return _value; }

}







































