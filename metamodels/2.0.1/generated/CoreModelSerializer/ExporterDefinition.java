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

// ExporterDefinition
// Definición de un exportador de datos que permite la exportación de datos hacia otro tipo de representación

@Root(name="exporter")
public  class ExporterDefinition extends Definition 
 {
@Root(name="schema")
public static class Schema {
protected @Text(data = true) String content;
public String getContent() { return content; }
}

protected @Element(name="schema",required=false) Schema _schema;

public Schema getSchema() { return _schema; }

}







































