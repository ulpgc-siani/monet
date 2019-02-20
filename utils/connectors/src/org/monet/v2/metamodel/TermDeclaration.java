package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// TermDeclaration
// Declaración de un término

@Root(name="term")
public  class TermDeclaration extends Declaration 
 {

protected @Attribute(name="code") String _code;
protected @Attribute(name="label") String _label;
protected @Attribute(name="is-category",required=false) boolean _isCategory;
protected @ElementList(inline=true,required=false) ArrayList<TermDeclaration> _termDeclarationList = new ArrayList<TermDeclaration>();

public String getCode() { return _code; }
public String getLabel() { return _label; }
public boolean isCategory() { return _isCategory; }
public ArrayList<TermDeclaration> getTermDeclarationList() { return _termDeclarationList; }

}







































