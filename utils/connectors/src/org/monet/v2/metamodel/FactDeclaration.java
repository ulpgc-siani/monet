package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// FactDeclaration
// Declaración que se utiliza para modelar el hecho de un cubo

@Root(name="fact")
public  class FactDeclaration extends Declaration 
 {

protected @ElementList(inline=true) ArrayList<AttributeDeclaration> _attributeDeclarationList = new ArrayList<AttributeDeclaration>();

public ArrayList<AttributeDeclaration> getAttributeDeclarationList() { return _attributeDeclarationList; }

}







































