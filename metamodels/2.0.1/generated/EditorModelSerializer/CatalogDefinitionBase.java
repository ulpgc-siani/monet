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

// CatalogDefinition
// Declaración que se utiliza para modelar un catálogo

@Root(name="catalog")
public  class CatalogDefinitionBase extends SetDefinition 
 {

protected @ElementList(inline=true,required=false) ArrayList<ParameterDeclaration> _parameterDeclarationList = new ArrayList<ParameterDeclaration>();

public ArrayList<ParameterDeclaration> getParameterDeclarationList() { return _parameterDeclarationList; }

}







































