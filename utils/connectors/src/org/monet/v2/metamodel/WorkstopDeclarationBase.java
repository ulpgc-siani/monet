package org.monet.v2.metamodel;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

// WorkstopDeclaration
// Declaración que se utiliza para modelar un	workstop de un workline

@Root(name="workstop")
public  class WorkstopDeclarationBase extends IndexedDeclaration 
 {
@Root(name="label")
public static class Label {
protected @Attribute(name="language") String _language;
protected @Text(data = true) String content;
public String getLanguage() { return _language; }
public String getContent() { return content; }
}

protected @Attribute(name="workplace") String _workplace;
  protected @ElementMap(entry="label",key="language",attribute=true,inline=true,required=false) Map<String,String> _labelMap = new HashMap<String,String>();  

public String getWorkplace() { return _workplace; }
public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }

}







































