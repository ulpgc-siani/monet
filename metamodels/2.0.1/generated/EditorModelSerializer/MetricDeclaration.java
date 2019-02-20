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

// MetricDeclaration
// Declaración que se utiliza para modelar la métrica de un campo de tipo numérico

@Root(name="metric")
public  class MetricDeclaration extends IndexedDeclaration 
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
@Root(name = "is-default"
)
public static class IsDefault {
}
@Root(name = "equivalence"
)
public static class Equivalence {
protected @Attribute(name="value",required=false) int _value;
public int getValue() { return _value; }
public void setValue(int value) { _value = value; }
}

  protected @ElementMap(entry="label",key="language",attribute=true,inline=true) Map<String,String> _labelMap = new HashMap<String,String>();  
protected @Element(name="is-default",required=false) IsDefault _isDefault;
protected @Element(name="equivalence",required=false) Equivalence _equivalence;

public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public boolean isDefault() { return (_isDefault != null); }
public void setIsDefault(boolean value) { if(value) _isDefault = new IsDefault(); else _isDefault = null;}
public Equivalence getEquivalence() { return _equivalence; }
public void setEquivalence(Equivalence value) { _equivalence = value; }

}







































