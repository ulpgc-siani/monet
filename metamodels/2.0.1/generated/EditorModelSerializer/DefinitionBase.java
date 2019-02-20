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

// Definition
// TODO

public abstract class DefinitionBase  
 {
@Root(name = "is-abstract"
)
public static class IsAbstract {
}
@Root(name = "is-environment"
)
public static class IsEnvironment {
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
@Root(name = "help"
)
public static class Help {
protected @Attribute(name="language") String _language;
protected @Attribute(name="filename") String _filename;
public String getLanguage() { return _language; }
public void setLanguage(String value) { _language = value; }
public String getFilename() { return _filename; }
public void setFilename(String value) { _filename = value; }
}

protected @Attribute(name="code") String _code;
protected @Attribute(name="name") String _name;
protected @Attribute(name="extends",required=false) String _extends;
protected @Element(name="is-abstract",required=false) IsAbstract _isAbstract;
protected @Element(name="is-environment",required=false) IsEnvironment _isEnvironment;
  protected @ElementMap(entry="label",key="language",attribute=true,inline=true) Map<String,String> _labelMap = new HashMap<String,String>();  
  protected @ElementMap(entry="description",key="language",attribute=true,inline=true,required=false) Map<String,String> _descriptionMap = new HashMap<String,String>();  
  protected @ElementMap(entry="help",key="language",value="filename",attribute=true,inline=true,required=false) Map<String,String> _helpMap = new HashMap<String,String>();
protected @ElementList(inline=true,required=false) ArrayList<GenericDeclaration> _genericDeclarationList = new ArrayList<GenericDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<ResolveDeclaration> _resolveDeclarationList = new ArrayList<ResolveDeclaration>();

public String getCode() { return _code; }
public void setCode(String value) { _code = value; }
public String getName() { return _name; }
public void setName(String value) { _name = value; }
public String getExtends() { return _extends; }
public void setExtends(String value) { _extends = value; }
public boolean isAbstract() { return (_isAbstract != null); }
public void setIsAbstract(boolean value) { if(value) _isAbstract = new IsAbstract(); else _isAbstract = null;}
public boolean isEnvironment() { return (_isEnvironment != null); }
public void setIsEnvironment(boolean value) { if(value) _isEnvironment = new IsEnvironment(); else _isEnvironment = null;}
public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public String getDescription(String language) { if(_descriptionMap.get(language) == null) return ""; return _descriptionMap.get(language); }
public Collection<String> getDescriptions() { return _descriptionMap.values(); }
public String getHelp(String language) { if(_helpMap.get(language) == null) return ""; return _helpMap.get(language); }
public Collection<String> getHelps() { return _helpMap.values(); }
public ArrayList<GenericDeclaration> getGenericDeclarationList() { return _genericDeclarationList; }
public ArrayList<ResolveDeclaration> getResolveDeclarationList() { return _resolveDeclarationList; }

}







































