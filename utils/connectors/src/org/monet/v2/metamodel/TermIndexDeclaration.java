package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// TermIndexDeclaration
// Declaración que se utiliza para modelar una lista de términos

@Root(name="term-index")
public  class TermIndexDeclaration extends Declaration 
 {

protected @Attribute(name="language") String _language;
protected @ElementList(inline=true) ArrayList<TermDeclaration> _termDeclarationList = new ArrayList<TermDeclaration>();

public String getLanguage() { return _language; }
public ArrayList<TermDeclaration> getTermDeclarationList() { return _termDeclarationList; }

}







































