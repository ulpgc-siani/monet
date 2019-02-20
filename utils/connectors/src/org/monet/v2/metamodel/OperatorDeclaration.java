package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// OperatorDeclaration
// Declaración que se utiliza para modelar un atributo de una referencia/cubo

@Root(name="operator")
public  class OperatorDeclaration extends Declaration 
 {
public enum TypeEnumeration { DIV,MULT,SUM,MINUS,MAX,MIN }

protected @Attribute(name="type") TypeEnumeration _type;
protected @ElementList(inline=true,required=false) ArrayList<ExpressionDeclaration> _expressionDeclarationList = new ArrayList<ExpressionDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<OperatorDeclaration> _operatorDeclarationList = new ArrayList<OperatorDeclaration>();

public TypeEnumeration getType() { return _type; }
public ArrayList<ExpressionDeclaration> getExpressionDeclarationList() { return _expressionDeclarationList; }
public ArrayList<OperatorDeclaration> getOperatorDeclarationList() { return _operatorDeclarationList; }

}







































