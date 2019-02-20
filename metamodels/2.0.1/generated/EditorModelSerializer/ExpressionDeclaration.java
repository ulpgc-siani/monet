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
public void setType(TypeEnumeration value) { _type = value; }
public String getIndicator() { return _indicator; }
public void setIndicator(String value) { _indicator = value; }
public double getValue() { return _value; }
public void setValue(double value) { _value = value; }

}







































