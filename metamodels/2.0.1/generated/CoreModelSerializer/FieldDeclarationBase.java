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

// FieldDeclaration
// Declaración abstracta que se utiliza para  modelar un campo de un formulario

public abstract class FieldDeclarationBase extends IndexedDeclaration 
 {
@Root(name="is-required")
public static class IsRequired {
}
@Root(name="is-extended")
public static class IsExtended {
}
@Root(name="is-super")
public static class IsSuper {
}
@Root(name="is-univocal")
public static class IsUnivocal {
}
@Root(name="is-static")
public static class IsStatic {
}
@Root(name="default-value")
public static class DefaultValue {
protected @Attribute(name="code",required=false) String _code;
protected @Attribute(name="checked",required=false) String _checked;
protected @Text(data = true) String content;
public String getCode() { return _code; }
public String getChecked() { return _checked; }
public String getContent() { return content; }
}
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
@Root(name="message")
public static class Message {
public enum TypeEnumeration { IF_EDITING,IF_EMPTY,IF_REQUIRED }
protected @Attribute(name="type",required=false) TypeEnumeration _type;
protected @Attribute(name="language") String _language;
protected @Text(data = true) String content;
public TypeEnumeration getType() { return _type; }
public String getLanguage() { return _language; }
public String getContent() { return content; }
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
public IsRequired getIsRequired() { return _isRequired; }
public boolean isExtended() { return (_isExtended != null); }
public IsExtended getIsExtended() { return _isExtended; }
public boolean isSuper() { return (_isSuper != null); }
public IsSuper getIsSuper() { return _isSuper; }
public boolean isUnivocal() { return (_isUnivocal != null); }
public IsUnivocal getIsUnivocal() { return _isUnivocal; }
public boolean isStatic() { return (_isStatic != null); }
public IsStatic getIsStatic() { return _isStatic; }
public ArrayList<DefaultValue> getDefaultValueList() { return _defaultValueList; }
public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public String getDescription(String language) { if(_descriptionMap.get(language) == null) return ""; return _descriptionMap.get(language); }
public Collection<String> getDescriptions() { return _descriptionMap.values(); }
public ArrayList<Message> getMessageList() { return _messageList; }

}







































