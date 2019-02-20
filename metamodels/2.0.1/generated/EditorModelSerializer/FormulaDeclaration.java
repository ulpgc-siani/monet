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

// FormulaDeclaration
// Declaración que se utiliza para modelar una expressión en una formula

@Root(name="expression")
public  class FormulaDeclaration extends Declaration 
 {
public enum TypeEnumeration { REAL,INTEGER,TEXT }

protected @Attribute(name="type") TypeEnumeration _type;
protected @Element(name="expression",required=false) ExpressionDeclaration _expressionDeclaration;
protected @Element(name="operator",required=false) OperatorDeclaration _operatorDeclaration;

public TypeEnumeration getType() { return _type; }
public void setType(TypeEnumeration value) { _type = value; }
public ExpressionDeclaration getExpressionDeclaration() { return _expressionDeclaration; }
public OperatorDeclaration getOperatorDeclaration() { return _operatorDeclaration; }

}







































