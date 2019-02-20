package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

// TextFieldDeclaration
// Declaración que se utiliza para modelar un	campo de texto

@Root(name="field-text")
public  class TextFieldDeclaration extends MultipleableFieldDeclaration 
 {
@Root(name="allow-history")
public static class AllowHistory {
protected @Attribute(name="datastore",required=false) String _datastore;
public String getDatastore() { return _datastore; }
}
@Root(name="length")
public static class Length {
protected @Attribute(name="max",required=false) int _max;
protected @Attribute(name="min",required=false) int _min;
public int getMax() { return _max; }
public int getMin() { return _min; }
}
@Root(name="edition")
public static class Edition {
public enum ModeEnumeration { UPPERCASE,LOWERCASE,CAPITALIZE,TITLE }
protected @Attribute(name="mode") ModeEnumeration _mode;
public ModeEnumeration getMode() { return _mode; }
}

protected @Element(name="allow-history",required=false) AllowHistory _allowHistory;
protected @Element(name="length",required=false) Length _length;
protected @Element(name="edition",required=false) Edition _edition;

public boolean allowHistory() { return (_allowHistory != null); }
public AllowHistory getAllowHistory() { return _allowHistory; }
public Length getLength() { return _length; }
public Edition getEdition() { return _edition; }

}







































