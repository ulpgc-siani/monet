package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

// CheckFieldDeclaration
// Declaración que se utiliza para modelar un campo de comprobación
// Los campos de comprobación permiten realizar listas de comprobación a partir de una lista de términos

@Root(name="field-check")
public  class CheckFieldDeclarationBase extends FieldDeclaration 
 {
@Root(name="import")
public static class Import {
public enum FlattenEnumeration { ALL,LEVEL,LEAF,INTERNAL }
protected @Attribute(name="thesaurus",required=false) String _thesaurus;
protected @Attribute(name="language") String _language;
protected @Attribute(name="flatten",required=false) FlattenEnumeration _flatten;
protected @Attribute(name="depth",required=false) int _depth;
protected @Attribute(name="from",required=false) String _from;
public String getThesaurus() { return _thesaurus; }
public String getLanguage() { return _language; }
public FlattenEnumeration getFlatten() { return _flatten; }
public int getDepth() { return _depth; }
public String getFrom() { return _from; }
}
@Root(name="allow-code")
public static class AllowCode {
}

protected @Element(name="import",required=false) Import _import;
protected @Element(name="allow-code",required=false) AllowCode _allowCode;
protected @Element(name="term-index",required=false) TermIndexDeclaration _termIndexDeclaration;

public Import getImport() { return _import; }
public boolean allowCode() { return (_allowCode != null); }
public AllowCode getAllowCode() { return _allowCode; }
public TermIndexDeclaration getTermIndexDeclaration() { return _termIndexDeclaration; }

}







































