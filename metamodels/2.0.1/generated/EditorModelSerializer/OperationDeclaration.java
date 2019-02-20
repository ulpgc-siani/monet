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

// OperationDeclaration
// Declaración abstracta que se utiliza para  modelar una operación en un nodo

@Root(name="operation")
public  class OperationDeclaration extends IndexedDeclaration 
 {
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
@Root(name = "description"
)
public static class Description {
protected @Attribute(name="language") String _language;
protected @Text(data = true) String content;
public String getLanguage() { return _language; }
public void setLanguage(String value) { _language = value; }
public String getContent() { return content; }
public void setContent(String content) { this.content = content; }
}

  protected @ElementMap(entry="label",key="language",attribute=true,inline=true,required=false) Map<String,String> _labelMap = new HashMap<String,String>();  
  protected @ElementMap(entry="description",key="language",attribute=true,inline=true,required=false) Map<String,String> _descriptionMap = new HashMap<String,String>();  

public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public String getDescription(String language) { if(_descriptionMap.get(language) == null) return ""; return _descriptionMap.get(language); }
public Collection<String> getDescriptions() { return _descriptionMap.values(); }

}







































