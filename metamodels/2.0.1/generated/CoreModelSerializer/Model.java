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

// Model
// Define todo lo relativo al modelo de negocio

@Root(name="model")
public  class Model  
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
@Root(name="version")
public static class Version {
protected @Attribute(name="date") int _date;
protected @Attribute(name="compilation") String _compilation;
protected @Attribute(name="metamodel") String _metamodel;
public int getDate() { return _date; }
public String getCompilation() { return _compilation; }
public String getMetamodel() { return _metamodel; }
}

protected @Attribute(name="code") String _code;
protected @Attribute(name="name") String _name;
  protected @ElementMap(entry="label",key="language",attribute=true,inline=true) Map<String,String> _labelMap = new HashMap<String,String>();  
  protected @ElementMap(entry="description",key="language",attribute=true,inline=true,required=false) Map<String,String> _descriptionMap = new HashMap<String,String>();  
protected @Element(name="version",required=false) Version _version;
protected @ElementList(inline=true,required=false) ArrayList<GenericDeclaration> _genericDeclarationList = new ArrayList<GenericDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<ResolveDeclaration> _resolveDeclarationList = new ArrayList<ResolveDeclaration>();

public String getCode() { return _code; }
public String getName() { return _name; }
public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public String getDescription(String language) { if(_descriptionMap.get(language) == null) return ""; return _descriptionMap.get(language); }
public Collection<String> getDescriptions() { return _descriptionMap.values(); }
public Version getVersion() { return _version; }
public ArrayList<GenericDeclaration> getGenericDeclarationList() { return _genericDeclarationList; }
public ArrayList<ResolveDeclaration> getResolveDeclarationList() { return _resolveDeclarationList; }

}







































