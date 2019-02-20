package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// DesktopDefinition
// Declaración que se utiliza para modelar un escritorio

@Root(name="desktop")
public  class DesktopDefinitionBase extends NodeDefinition 
 {

protected @ElementList(inline=true,required=false) ArrayList<DesktopViewDeclaration> _desktopViewDeclarationList = new ArrayList<DesktopViewDeclaration>();

public ArrayList<DesktopViewDeclaration> getDesktopViewDeclarationList() { return _desktopViewDeclarationList; }

}







































