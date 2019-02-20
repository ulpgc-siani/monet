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

// LinkFieldDeclaration
// Declaración que se utiliza para modelar un campo vínculo

@Root(name="field-link")
public  class LinkFieldDeclaration extends MultipleableFieldDeclaration 
 {
@Root(name="source")
public static class Source {
protected @Attribute(name="set") String _set;
protected @Attribute(name="filter",required=false) String _filter;
public String getSet() { return _set; }
public String getFilter() { return _filter; }
}
@Root(name="bind")
public static class Bind {
protected @Attribute(name="parameter") String _parameter;
protected @Attribute(name="field") String _field;
public String getParameter() { return _parameter; }
public String getField() { return _field; }
}
@Root(name="allow-history")
public static class AllowHistory {
protected @Attribute(name="datastore",required=false) String _datastore;
public String getDatastore() { return _datastore; }
}
@Root(name="allow-search")
public static class AllowSearch {
}
@Root(name="allow-other")
public static class AllowOther {
}

protected @Element(name="source",required=false) Source _source;
protected @ElementList(inline=true,required=false) ArrayList<Bind> _bindList = new ArrayList<Bind>();
protected @Element(name="allow-history",required=false) AllowHistory _allowHistory;
protected @Element(name="allow-search",required=false) AllowSearch _allowSearch;
protected @Element(name="allow-other",required=false) AllowOther _allowOther;

public Source getSource() { return _source; }
public ArrayList<Bind> getBindList() { return _bindList; }
public boolean allowHistory() { return (_allowHistory != null); }
public AllowHistory getAllowHistory() { return _allowHistory; }
public boolean allowSearch() { return (_allowSearch != null); }
public AllowSearch getAllowSearch() { return _allowSearch; }
public boolean allowOther() { return (_allowOther != null); }
public AllowOther getAllowOther() { return _allowOther; }

}







































