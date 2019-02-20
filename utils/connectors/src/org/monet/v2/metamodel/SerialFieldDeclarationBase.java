package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

// SerialFieldDeclaration
// Declaración que se utiliza para modelar un	campo serial

@Root(name="field-serial")
public  class SerialFieldDeclarationBase extends MultipleableFieldDeclaration 
 {
@Root(name="generator")
public static class Generator {
protected @Attribute(name="name") String _name;
public String getName() { return _name; }
}
@Root(name="format")
public static class Format {
protected @Attribute(name="pattern") String _pattern;
public String getPattern() { return _pattern; }
}

protected @Element(name="generator") Generator _generator;
protected @Element(name="format") Format _format;

public Generator getGenerator() { return _generator; }
public Format getFormat() { return _format; }

}







































