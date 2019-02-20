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

// SetViewDeclaration

@Root(name="view")
public  class SetViewDeclaration extends NodeViewDeclaration 
 {
@Root(name="show-summary")
public static class ShowSummary {
public enum DrillByEnumeration { DEFINITION,ATTRIBUTE }
protected @Attribute(name="drill-by") DrillByEnumeration _drillBy;
protected @Attribute(name="attribute",required=false) String _attribute;
public DrillByEnumeration getDrillBy() { return _drillBy; }
public String getAttribute() { return _attribute; }
}
@Root(name="show-list")
public static class ShowList {
protected @Attribute(name="page",required=false) int _page = 20;
protected @Attribute(name="image",required=false) String _image;
protected @Attribute(name="reference-view") String _referenceView;
public int getPage() { return _page; }
public String getImage() { return _image; }
public String getReferenceView() { return _referenceView; }
}
@Root(name="show-grid")
public static class ShowGrid {
protected @Attribute(name="page",required=false) int _page = 20;
protected @Attribute(name="image",required=false) String _image;
protected @Attribute(name="reference-view") String _referenceView;
public int getPage() { return _page; }
public String getImage() { return _image; }
public String getReferenceView() { return _referenceView; }
}
@Root(name="show-location")
public static class ShowLocation {
protected @Attribute(name="with-name",required=false) String _withName;
public String getWithName() { return _withName; }
}
@Root(name="show-layer")
public static class ShowLayer {
}
@Root(name="allow-summary")
public static class AllowSummary {
}

protected @ElementList(inline=true,required=false) ArrayList<ShowSummary> _showSummaryList = new ArrayList<ShowSummary>();
protected @Element(name="show-list",required=false) ShowList _showList;
protected @Element(name="show-grid",required=false) ShowGrid _showGrid;
protected @Element(name="show-location",required=false) ShowLocation _showLocation;
protected @Element(name="show-layer",required=false) ShowLayer _showLayer;
protected @Element(name="allow-summary",required=false) AllowSummary _allowSummary;

public ArrayList<ShowSummary> getShowSummaryList() { return _showSummaryList; }
public ShowList getShowList() { return _showList; }
public ShowGrid getShowGrid() { return _showGrid; }
public ShowLocation getShowLocation() { return _showLocation; }
public ShowLayer getShowLayer() { return _showLayer; }
public boolean allowSummary() { return (_allowSummary != null); }
public AllowSummary getAllowSummary() { return _allowSummary; }

}







































