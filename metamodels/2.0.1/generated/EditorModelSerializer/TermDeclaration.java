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
public void setCode(String value) { _code = value; }
public String getLabel() { return _label; }
public void setLabel(String value) { _label = value; }
public boolean isCategory() { return _isCategory; }
public void setIsCategory(boolean value) { _isCategory = value; }
public ArrayList<TermDeclaration> getTermDeclarationList() { return _termDeclarationList; }

}







































