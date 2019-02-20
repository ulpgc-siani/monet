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

// DimensionDeclaration
// Declaración que se utiliza para modelar un dimensión de un cubo
// Una dimensión tiene que estar vinculada con un tesauro o con una referencia

@Root(name="dimension")
public  class DimensionDeclaration extends IndexedDeclaration 
 {
@Root(name = "is-embedded"
)
public static class IsEmbedded {
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

protected @Attribute(name="reference",required=false) String _reference;
protected @Attribute(name="thesaurus",required=false) String _thesaurus;
protected @Element(name="is-embedded",required=false) IsEmbedded _isEmbedded;
  protected @ElementMap(entry="label",key="language",attribute=true,inline=true,required=false) Map<String,String> _labelMap = new HashMap<String,String>();  
protected @ElementList(inline=true) ArrayList<AttributeDeclaration> _attributeDeclarationList = new ArrayList<AttributeDeclaration>();

public String getReference() { return _reference; }
public void setReference(String value) { _reference = value; }
public String getThesaurus() { return _thesaurus; }
public void setThesaurus(String value) { _thesaurus = value; }
public boolean isEmbedded() { return (_isEmbedded != null); }
public void setIsEmbedded(boolean value) { if(value) _isEmbedded = new IsEmbedded(); else _isEmbedded = null;}
public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public ArrayList<AttributeDeclaration> getAttributeDeclarationList() { return _attributeDeclarationList; }

}







































