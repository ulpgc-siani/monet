package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

// CubeDefinition
// Declaración que se utiliza para modelar un cubo multidimensional de la unidad de negocio

@Root(name="cube")
public  class CubeDefinition extends AnalyticalDefinition 
 {
@Root(name="schema")
public static class Schema {
protected @Text(data = true) String content;
public String getContent() { return content; }
}
@Root(name="external")
public static class External {
protected @Attribute(name="provider") String _provider;
public String getProvider() { return _provider; }
}

protected @Element(name="schema",required=false) Schema _schema;
protected @Element(name="external",required=false) External _external;
protected @Element(name="fact",required=false) FactDeclaration _factDeclaration;
protected @ElementList(inline=true,required=false) ArrayList<DimensionDeclaration> _dimensionDeclarationList = new ArrayList<DimensionDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<IndicatorDeclaration> _indicatorDeclarationList = new ArrayList<IndicatorDeclaration>();

public Schema getSchema() { return _schema; }
public External getExternal() { return _external; }
public FactDeclaration getFactDeclaration() { return _factDeclaration; }
public ArrayList<DimensionDeclaration> getDimensionDeclarationList() { return _dimensionDeclarationList; }
public ArrayList<IndicatorDeclaration> getIndicatorDeclarationList() { return _indicatorDeclarationList; }

}







































