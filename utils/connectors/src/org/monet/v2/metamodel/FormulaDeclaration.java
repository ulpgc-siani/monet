package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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
public ExpressionDeclaration getExpressionDeclaration() { return _expressionDeclaration; }
public OperatorDeclaration getOperatorDeclaration() { return _operatorDeclaration; }

}







































