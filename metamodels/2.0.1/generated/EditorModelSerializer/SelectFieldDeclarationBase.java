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

// SelectFieldDeclaration
// Declaración que se utiliza para modelar un	campo de selección

@Root(name="field-select")
public  class SelectFieldDeclarationBase extends MultipleableFieldDeclaration 
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
@Root(name = "is-embedded"
)
public static class IsEmbedded {
}
@Root(name = "allow-history"
)
public static class AllowHistory {
protected @Attribute(name="datastore",required=false) String _datastore;
public String getDatastore() { return _datastore; }
public void setDatastore(String value) { _datastore = value; }
}
@Root(name = "allow-search"
)
public static class AllowSearch {
}
@Root(name = "allow-other"
)
public static class AllowOther {
}
@Root(name = "allow-code"
)
public static class AllowCode {
}

protected @Element(name="import",required=false) Import _import;
protected @Element(name="is-embedded",required=false) IsEmbedded _isEmbedded;
protected @Element(name="allow-history",required=false) AllowHistory _allowHistory;
protected @Element(name="allow-search",required=false) AllowSearch _allowSearch;
protected @Element(name="allow-other",required=false) AllowOther _allowOther;
protected @Element(name="allow-code",required=false) AllowCode _allowCode;
protected @Element(name="term-index",required=false) TermIndexDeclaration _termIndexDeclaration;

public Import getImport() { return _import; }
public void setImport(Import value) { _import = value; }
public boolean isEmbedded() { return (_isEmbedded != null); }
public void setIsEmbedded(boolean value) { if(value) _isEmbedded = new IsEmbedded(); else _isEmbedded = null;}
public boolean allowHistory() { return (_allowHistory != null); }
public AllowHistory getAllowHistory() { return _allowHistory; }
public void setAllowHistory(boolean value) { if(value) _allowHistory = new AllowHistory(); else _allowHistory = null;}
public boolean allowSearch() { return (_allowSearch != null); }
public AllowSearch getAllowSearch() { return _allowSearch; }
public void setAllowSearch(boolean value) { if(value) _allowSearch = new AllowSearch(); else _allowSearch = null;}
public boolean allowOther() { return (_allowOther != null); }
public AllowOther getAllowOther() { return _allowOther; }
public void setAllowOther(boolean value) { if(value) _allowOther = new AllowOther(); else _allowOther = null;}
public boolean allowCode() { return (_allowCode != null); }
public AllowCode getAllowCode() { return _allowCode; }
public void setAllowCode(boolean value) { if(value) _allowCode = new AllowCode(); else _allowCode = null;}
public TermIndexDeclaration getTermIndexDeclaration() { return _termIndexDeclaration; }

}







































