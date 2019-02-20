package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

// DateFieldDeclaration
// Declaración que se utiliza para modelar un campo fecha

@Root(name="field-date")
public  class DateFieldDeclaration extends MultipleableFieldDeclaration 
 {
@Root(name="format")
public static class Format {
protected @Attribute(name="value") String _value;
public String getValue() { return _value; }
}

protected @Element(name="format",required=false) Format _format;

public Format getFormat() { return _format; }

}







































