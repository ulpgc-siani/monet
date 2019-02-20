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

// FieldDeclaration
// Declaración abstracta que se utiliza para  modelar un campo de un formulario

public abstract class FieldDeclarationBase extends IndexedDeclaration 
 {
@Root(name = "is-required"
)
public static class IsRequired {
}
@Root(name = "is-extended"
)
public static class IsExtended {
}
@Root(name = "is-super"
)
public static class IsSuper {
}
@Root(name = "is-univocal"
)
public static class IsUnivocal {
}
@Root(name = "is-static"
)
public static class IsStatic {
}
@Root(name = "default-value"
)
public static class DefaultValue {
protected @Attribute(name="code",required=false) String _code;
protected @Attribute(name="checked",required=false) String _checked;
protected @Text(data = true) String content;
public String getCode() { return _code; }
public void setCode(String value) { _code = value; }
public String getChecked() { return _checked; }
public void setChecked(String value) { _checked = value; }
public String getContent() { return content; }
public void setContent(String content) { this.content = content; }
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
@Root(name = "message"
)
public static class Message {
public enum TypeEnumeration { IF_EDITING,IF_EMPTY,IF_REQUIRED }
protected @Attribute(name="type",required=false) TypeEnumeration _type;
protected @Attribute(name="language") String _language;
protected @Text(data = true) String content;
public TypeEnumeration getType() { return _type; }
public void setType(TypeEnumeration value) { _type = value; }
public String getLanguage() { return _language; }
public void setLanguage(String value) { _language = value; }
public String getContent() { return content; }
public void setContent(String content) { this.content = content; }
}

protected @Element(name="is-required",required=false) IsRequired _isRequired;
protected @Element(name="is-extended",required=false) IsExtended _isExtended;
protected @Element(name="is-super",required=false) IsSuper _isSuper;
protected @Element(name="is-univocal",required=false) IsUnivocal _isUnivocal;
protected @Element(name="is-static",required=false) IsStatic _isStatic;
protected @ElementList(inline=true,required=false) ArrayList<DefaultValue> _defaultValueList = new ArrayList<DefaultValue>();
  protected @ElementMap(entry="label",key="language",attribute=true,inline=true,required=false) Map<String,String> _labelMap = new HashMap<String,String>();  
  protected @ElementMap(entry="description",key="language",attribute=true,inline=true,required=false) Map<String,String> _descriptionMap = new HashMap<String,String>();  
protected @ElementList(inline=true,required=false) ArrayList<Message> _messageList = new ArrayList<Message>();

public boolean isRequired() { return (_isRequired != null); }
public void setIsRequired(boolean value) { if(value) _isRequired = new IsRequired(); else _isRequired = null;}
public boolean isExtended() { return (_isExtended != null); }
public void setIsExtended(boolean value) { if(value) _isExtended = new IsExtended(); else _isExtended = null;}
public boolean isSuper() { return (_isSuper != null); }
public void setIsSuper(boolean value) { if(value) _isSuper = new IsSuper(); else _isSuper = null;}
public boolean isUnivocal() { return (_isUnivocal != null); }
public void setIsUnivocal(boolean value) { if(value) _isUnivocal = new IsUnivocal(); else _isUnivocal = null;}
public boolean isStatic() { return (_isStatic != null); }
public void setIsStatic(boolean value) { if(value) _isStatic = new IsStatic(); else _isStatic = null;}
public ArrayList<DefaultValue> getDefaultValueList() { return _defaultValueList; }
public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public String getDescription(String language) { if(_descriptionMap.get(language) == null) return ""; return _descriptionMap.get(language); }
public Collection<String> getDescriptions() { return _descriptionMap.values(); }
public ArrayList<Message> getMessageList() { return _messageList; }

}







































