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

// NodeViewDeclaration
// Declaración abstracta de una vista de un nodo
  

public abstract class NodeViewDeclaration extends ViewDeclaration 
 {
public enum TypeEnumeration { TAB,EMBEDDED }
@Root(name = "is-default"
)
public static class IsDefault {
}
@Root(name = "label"
)
public static class Label {
protected @Attribute(name="language") String _language;
protected @Text(data = true) String content;
public String getLanguage() { return _language; }
public void setLanguage(String value) { _language = value; }
public String getContent() { return content; }
public void setContent(String content) { this.content = content; }
}
@Root(name = "show-set"
)
public static class ShowSet {
public enum SetEnumeration { LINKS_IN,LINKS_OUT,TASKS,REVISIONS,NOTES,PROTOTYPES }
protected @Attribute(name="set") SetEnumeration _set;
public SetEnumeration getSet() { return _set; }
public void setSet(SetEnumeration value) { _set = value; }
}
@Root(name = "elements-per-page"
)
public static class ElementsPerPage {
protected @Attribute(name="value") String _value;
public String getValue() { return _value; }
public void setValue(String value) { _value = value; }
}

protected @Attribute(name="type",required=false) TypeEnumeration _type;
protected @Element(name="is-default",required=false) IsDefault _isDefault;
  protected @ElementMap(entry="label",key="language",attribute=true,inline=true,required=false) Map<String,String> _labelMap = new HashMap<String,String>();  
protected @Element(name="show-set",required=false) ShowSet _showSet;
protected @Element(name="elements-per-page",required=false) ElementsPerPage _elementsPerPage;
protected @ElementList(inline=true,required=false) ArrayList<SelectDeclaration> _selectDeclarationList = new ArrayList<SelectDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<FilterDeclaration> _filterDeclarationList = new ArrayList<FilterDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<SortDeclaration> _sortDeclarationList = new ArrayList<SortDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<GroupDeclaration> _groupDeclarationList = new ArrayList<GroupDeclaration>();

public TypeEnumeration getType() { return _type; }
public void setType(TypeEnumeration value) { _type = value; }
public boolean isDefault() { return (_isDefault != null); }
public void setIsDefault(boolean value) { if(value) _isDefault = new IsDefault(); else _isDefault = null;}
public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public ShowSet getShowSet() { return _showSet; }
public void setShowSet(ShowSet value) { _showSet = value; }
public ElementsPerPage getElementsPerPage() { return _elementsPerPage; }
public void setElementsPerPage(ElementsPerPage value) { _elementsPerPage = value; }
public ArrayList<SelectDeclaration> getSelectDeclarationList() { return _selectDeclarationList; }
public ArrayList<FilterDeclaration> getFilterDeclarationList() { return _filterDeclarationList; }
public ArrayList<SortDeclaration> getSortDeclarationList() { return _sortDeclarationList; }
public ArrayList<GroupDeclaration> getGroupDeclarationList() { return _groupDeclarationList; }

}







































