package org.monet.v2.metamodel;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

// MetricDeclaration
// Declaración que se utiliza para modelar la métrica de un campo de tipo numérico

@Root(name="metric")
public  class MetricDeclaration extends IndexedDeclaration 
 {
@Root(name="label")
public static class Label {
protected @Attribute(name="language") String _language;
protected @Text(data = true) String content;
public String getLanguage() { return _language; }
public String getContent() { return content; }
}
@Root(name="is-default")
public static class IsDefault {
}
@Root(name="equivalence")
public static class Equivalence {
protected @Attribute(name="value",required=false) int _value;
public int getValue() { return _value; }
}

  protected @ElementMap(entry="label",key="language",attribute=true,inline=true) Map<String,String> _labelMap = new HashMap<String,String>();  
protected @Element(name="is-default",required=false) IsDefault _isDefault;
protected @Element(name="equivalence",required=false) Equivalence _equivalence;

public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public boolean isDefault() { return (_isDefault != null); }
public IsDefault getIsDefault() { return _isDefault; }
public Equivalence getEquivalence() { return _equivalence; }

}







































