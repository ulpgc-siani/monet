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

// AttributeDeclaration
// Declaración que se utiliza para modelar un atributo de una referencia/cubo

@Root(name="attribute")
public  class AttributeDeclaration extends IndexedDeclaration 
 {
public enum TypeEnumeration { BOOLEAN,TEXT,INTEGER,REAL,DATE,DATETIME,TIME,TERM,CHECK,DIMENSION }
public enum PrecisionEnumeration { YEARS,MONTHS,DAYS,HOURS,MINUTES,SECONDS }
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
@Root(name="is-identifier")
public static class IsIdentifier {
}

protected @Attribute(name="type") TypeEnumeration _type;
protected @Attribute(name="precision",required=false) PrecisionEnumeration _precision;
protected @Attribute(name="dimension",required=false) String _dimension;
  protected @ElementMap(entry="label",key="language",attribute=true,inline=true,required=false) Map<String,String> _labelMap = new HashMap<String,String>();  
  protected @ElementMap(entry="description",key="language",attribute=true,inline=true,required=false) Map<String,String> _descriptionMap = new HashMap<String,String>();  
protected @Element(name="is-identifier",required=false) IsIdentifier _isIdentifier;

public TypeEnumeration getType() { return _type; }
public PrecisionEnumeration getPrecision() { return _precision; }
public String getDimension() { return _dimension; }
public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public String getDescription(String language) { if(_descriptionMap.get(language) == null) return ""; return _descriptionMap.get(language); }
public Collection<String> getDescriptions() { return _descriptionMap.values(); }
public boolean isIdentifier() { return (_isIdentifier != null); }
public IsIdentifier getIsIdentifier() { return _isIdentifier; }

}







































