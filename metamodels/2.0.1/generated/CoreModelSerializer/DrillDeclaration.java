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

// DrillDeclaration
// TODO

@Root(name="drill")
public  class DrillDeclaration extends IndexedDeclaration 
 {
@Root(name="use")
public static class Use {
protected @Attribute(name="dimension",required=false) String _dimension;
public String getDimension() { return _dimension; }
}

protected @Element(name="use",required=false) Use _use;
protected @Element(name="drill",required=false) DrillDeclaration _drillDeclaration;

public Use getUse() { return _use; }
public DrillDeclaration getDrillDeclaration() { return _drillDeclaration; }

}







































