package org.monet.v2.metamodel;


import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

// CatalogDefinition
// Declaración que se utiliza para modelar un catálogo

@Root(name="catalog")
public  class CatalogDefinitionBase extends SetDefinition 
 {

protected @ElementList(inline=true,required=false) ArrayList<ParameterDeclaration> _parameterDeclarationList = new ArrayList<ParameterDeclaration>();

public ArrayList<ParameterDeclaration> getParameterDeclarationList() { return _parameterDeclarationList; }

}







































