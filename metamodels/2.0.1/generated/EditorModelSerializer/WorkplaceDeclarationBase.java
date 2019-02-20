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

// WorkplaceDeclaration
// Declaración que se utiliza para modelar un workplace dentro de un workmap

@Root(name="workplace")
public  class WorkplaceDeclarationBase extends IndexedDeclaration 
 {
public enum TypeEnumeration { EVENT,GOAL,DEAD_END }
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

protected @Attribute(name="type",required=false) TypeEnumeration _type;
  protected @ElementMap(entry="label",key="language",attribute=true,inline=true,required=false) Map<String,String> _labelMap = new HashMap<String,String>();  

public TypeEnumeration getType() { return _type; }
public void setType(TypeEnumeration value) { _type = value; }
public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }

}







































