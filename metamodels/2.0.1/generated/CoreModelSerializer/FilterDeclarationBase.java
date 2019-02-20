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







































