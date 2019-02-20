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

// DesktopDefinition
// Declaración que se utiliza para modelar un escritorio

@Root(name="desktop")
public  class DesktopDefinitionBase extends NodeDefinition 
 {

protected @ElementList(inline=true,required=false) ArrayList<DesktopViewDeclaration> _desktopViewDeclarationList = new ArrayList<DesktopViewDeclaration>();

public ArrayList<DesktopViewDeclaration> getDesktopViewDeclarationList() { return _desktopViewDeclarationList; }

}







































