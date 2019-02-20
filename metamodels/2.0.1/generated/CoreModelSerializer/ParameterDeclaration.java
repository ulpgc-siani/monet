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

// ParameterDeclaration
// Declaración que se utiliza para modelar un parámetro de un catálogo/informe

@Root(name="parameter")
public  class ParameterDeclaration extends IndexedDeclaration 
 {
public enum TypeEnumeration { TEXT,NUMBER,DATE,DATE_RANGE,DIMENSION }
public enum PrecisionEnumeration { YEARS,MONTHS,DAYS,HOURS,MINUTES,SECONDS }
@Root(name="use")
public static class Use {
protected @Attribute(name="attribute",required=false) String _attribute;
public String getAttribute() { return _attribute; }
}
@Root(name="label")
public static class Label {
protected @Attribute(name="language") String _language;
protected @Text(data = true) String content;
public String getLanguage() { return _language; }
public String getContent() { return content; }
}

protected @Attribute(name="type") TypeEnumeration _type;
protected @Attribute(name="precision",required=false) PrecisionEnumeration _precision;
protected @Attribute(name="dimension",required=false) String _dimension;
protected @Attribute(name="default-value",required=false) String _defaultValue;
protected @Element(name="use",required=false) Use _use;
  protected @ElementMap(entry="label",key="language",attribute=true,inline=true,required=false) Map<String,String> _labelMap = new HashMap<String,String>();  

public TypeEnumeration getType() { return _type; }
public PrecisionEnumeration getPrecision() { return _precision; }
public String getDimension() { return _dimension; }
public String getDefaultValue() { return _defaultValue; }
public Use getUse() { return _use; }
public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }

}







































