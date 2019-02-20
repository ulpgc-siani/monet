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







































