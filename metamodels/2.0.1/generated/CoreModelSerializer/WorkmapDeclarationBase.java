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

// WorkmapDeclaration
// Declaración que se utiliza para modelar un workmap

@Root(name="workmap")
public  class WorkmapDeclarationBase extends Declaration 
 {
@Root(name="label")
public static class Label {
protected @Attribute(name="language") String _language;
protected @Text(data = true) String content;
public String getLanguage() { return _language; }
public String getContent() { return content; }
}
@Root(name="description")
public static class Description {
protected @Attribute(name="language") String _language;
protected @Text(data = true) String content;
public String getLanguage() { return _language; }
public String getContent() { return content; }
}

  protected @ElementMap(entry="label",key="language",attribute=true,inline=true,required=false) Map<String,String> _labelMap = new HashMap<String,String>();  
  protected @ElementMap(entry="description",key="language",attribute=true,inline=true,required=false) Map<String,String> _descriptionMap = new HashMap<String,String>();  
protected @ElementList(inline=true,required=false) ArrayList<WorkplaceDeclaration> _workplaceDeclarationList = new ArrayList<WorkplaceDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<WorklineDeclaration> _worklineDeclarationList = new ArrayList<WorklineDeclaration>();

public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public String getDescription(String language) { if(_descriptionMap.get(language) == null) return ""; return _descriptionMap.get(language); }
public Collection<String> getDescriptions() { return _descriptionMap.values(); }
public ArrayList<WorkplaceDeclaration> getWorkplaceDeclarationList() { return _workplaceDeclarationList; }
public ArrayList<WorklineDeclaration> getWorklineDeclarationList() { return _worklineDeclarationList; }

}







































