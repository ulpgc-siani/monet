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

// NumberFieldDeclaration
// Declaración que se utiliza para modelar un campo numérico

@Root(name="field-number")
public  class NumberFieldDeclaration extends MultipleableFieldDeclaration 
 {
@Root(name="format")
public static class Format {
protected @Attribute(name="value") String _value;
public String getValue() { return _value; }
}
@Root(name="range")
public static class Range {
protected @Attribute(name="min") int _min;
protected @Attribute(name="max") int _max;
public int getMin() { return _min; }
public int getMax() { return _max; }
}

protected @Element(name="format",required=false) Format _format;
protected @Element(name="range",required=false) Range _range;
protected @ElementList(inline=true,required=false) ArrayList<MetricDeclaration> _metricDeclarationList = new ArrayList<MetricDeclaration>();

public Format getFormat() { return _format; }
public Range getRange() { return _range; }
public ArrayList<MetricDeclaration> getMetricDeclarationList() { return _metricDeclarationList; }

}







































