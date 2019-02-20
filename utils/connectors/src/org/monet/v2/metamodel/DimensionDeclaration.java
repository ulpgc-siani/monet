package org.monet.v2.metamodel;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

// DimensionDeclaration
// Declaración que se utiliza para modelar un dimensión de un cubo
// Una dimensión tiene que estar vinculada con un tesauro o con una referencia

@Root(name="dimension")
public  class DimensionDeclaration extends IndexedDeclaration 
 {
@Root(name="is-embedded")
public static class IsEmbedded {
}
@Root(name="label")
public static class Label {
protected @Attribute(name="language") String _language;
protected @Text(data = true) String content;
public String getLanguage() { return _language; }
public String getContent() { return content; }
}

protected @Attribute(name="reference",required=false) String _reference;
protected @Attribute(name="thesaurus",required=false) String _thesaurus;
protected @Element(name="is-embedded",required=false) IsEmbedded _isEmbedded;
  protected @ElementMap(entry="label",key="language",attribute=true,inline=true,required=false) Map<String,String> _labelMap = new HashMap<String,String>();  
protected @ElementList(inline=true) ArrayList<AttributeDeclaration> _attributeDeclarationList = new ArrayList<AttributeDeclaration>();

public String getReference() { return _reference; }
public String getThesaurus() { return _thesaurus; }
public boolean isEmbedded() { return (_isEmbedded != null); }
public IsEmbedded getIsEmbedded() { return _isEmbedded; }
public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public ArrayList<AttributeDeclaration> getAttributeDeclarationList() { return _attributeDeclarationList; }

}







































