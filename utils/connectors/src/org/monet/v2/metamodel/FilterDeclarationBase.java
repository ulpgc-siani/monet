package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

// FilterDeclaration
// Se utiliza para indicar un filtro sobre el valor de un atributo

@Root(name="filter")
public  class FilterDeclarationBase extends Declaration 
 {
public enum OperatorEnumeration { EQ,GT,GE,LT,LE,NEQ }

protected @Attribute(name="attribute") String _attribute;
protected @Attribute(name="operator",required=false) OperatorEnumeration _operator;
protected @Attribute(name="value") String _value;

public String getAttribute() { return _attribute; }
public OperatorEnumeration getOperator() { return _operator; }
public String getValue() { return _value; }

}







































