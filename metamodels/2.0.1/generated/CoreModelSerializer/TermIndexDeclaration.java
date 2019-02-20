package org.monet.kernel.model.definition;

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







































