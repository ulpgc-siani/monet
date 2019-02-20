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

// CheckFieldDeclaration
// Declaración que se utiliza para modelar un campo de comprobación
// Los campos de comprobación permiten realizar listas de comprobación a partir de una lista de términos

@Root(name="field-check")
public  class CheckFieldDeclaration extends FieldDeclaration 
 {
@Root(name = "import"
)
public static class Import {
public enum FlattenEnumeration { ALL,LEVEL,LEAF,INTERNAL }
protected @Attribute(name="thesaurus",required=false) String _thesaurus;
protected @Attribute(name="language") String _language;
protected @Attribute(name="flatten",required=false) FlattenEnumeration _flatten;
protected @Attribute(name="depth",required=false) int _depth;
protected @Attribute(name="from",required=false) String _from;
public String getThesaurus() { return _thesaurus; }
public void setThesaurus(String value) { _thesaurus = value; }
public String getLanguage() { return _language; }
public void setLanguage(String value) { _language = value; }
public FlattenEnumeration getFlatten() { return _flatten; }
public void setFlatten(FlattenEnumeration value) { _flatten = value; }
public int getDepth() { return _depth; }
public void setDepth(int value) { _depth = value; }
public String getFrom() { return _from; }
public void setFrom(String value) { _from = value; }
}
@Root(name = "allow-code"
)
public static class AllowCode {
}

protected @Element(name="import",required=false) Import _import;
protected @Element(name="allow-code",required=false) AllowCode _allowCode;
protected @Element(name="term-index",required=false) TermIndexDeclaration _termIndexDeclaration;

public Import getImport() { return _import; }
public void setImport(Import value) { _import = value; }
public boolean allowCode() { return (_allowCode != null); }
public AllowCode getAllowCode() { return _allowCode; }
public void setAllowCode(boolean value) { if(value) _allowCode = new AllowCode(); else _allowCode = null;}
public TermIndexDeclaration getTermIndexDeclaration() { return _termIndexDeclaration; }

}







































