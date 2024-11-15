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

// MemoFieldDeclaration
// Declaración que se utiliza para modelar un campo memo

@Root(name="field-memo")
public  class MemoFieldDeclaration extends MultipleableFieldDeclaration 
 {
@Root(name = "allow-history"
)
public static class AllowHistory {
protected @Attribute(name="datastore",required=false) String _datastore;
public String getDatastore() { return _datastore; }
public void setDatastore(String value) { _datastore = value; }
}
@Root(name = "length"
)
public static class Length {
protected @Attribute(name="max",required=false) int _max;
protected @Attribute(name="min",required=false) int _min;
public int getMax() { return _max; }
public void setMax(int value) { _max = value; }
public int getMin() { return _min; }
public void setMin(int value) { _min = value; }
}
@Root(name = "edition"
)
public static class Edition {
public enum ModeEnumeration { UPPERCASE,LOWERCASE,SENTENCE,TITLE }
protected @Attribute(name="mode") ModeEnumeration _mode;
public ModeEnumeration getMode() { return _mode; }
public void setMode(ModeEnumeration value) { _mode = value; }
}

protected @Element(name="allow-history",required=false) AllowHistory _allowHistory;
protected @Element(name="length",required=false) Length _length;
protected @Element(name="edition",required=false) Edition _edition;

public boolean allowHistory() { return (_allowHistory != null); }
public AllowHistory getAllowHistory() { return _allowHistory; }
public void setAllowHistory(boolean value) { if(value) _allowHistory = new AllowHistory(); else _allowHistory = null;}
public Length getLength() { return _length; }
public void setLength(Length value) { _length = value; }
public Edition getEdition() { return _edition; }
public void setEdition(Edition value) { _edition = value; }

}







































