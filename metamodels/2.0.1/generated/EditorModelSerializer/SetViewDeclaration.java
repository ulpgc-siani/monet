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

// SetViewDeclaration

@Root(name="view")
public  class SetViewDeclaration extends NodeViewDeclaration 
 {
@Root(name = "show-summary"
)
public static class ShowSummary {
public enum DrillByEnumeration { DEFINITION,ATTRIBUTE }
protected @Attribute(name="drill-by") DrillByEnumeration _drillBy;
protected @Attribute(name="attribute",required=false) String _attribute;
public DrillByEnumeration getDrillBy() { return _drillBy; }
public void setDrillBy(DrillByEnumeration value) { _drillBy = value; }
public String getAttribute() { return _attribute; }
public void setAttribute(String value) { _attribute = value; }
}
@Root(name = "show-list"
)
public static class ShowList {
protected @Attribute(name="page",required=false) int _page;
protected @Attribute(name="image",required=false) String _image;
protected @Attribute(name="reference-view") String _referenceView;
public int getPage() { return _page; }
public void setPage(int value) { _page = value; }
public String getImage() { return _image; }
public void setImage(String value) { _image = value; }
public String getReferenceView() { return _referenceView; }
public void setReferenceView(String value) { _referenceView = value; }
}
@Root(name = "show-grid"
)
public static class ShowGrid {
protected @Attribute(name="page",required=false) int _page;
protected @Attribute(name="image",required=false) String _image;
protected @Attribute(name="reference-view") String _referenceView;
public int getPage() { return _page; }
public void setPage(int value) { _page = value; }
public String getImage() { return _image; }
public void setImage(String value) { _image = value; }
public String getReferenceView() { return _referenceView; }
public void setReferenceView(String value) { _referenceView = value; }
}
@Root(name = "show-location"
)
public static class ShowLocation {
protected @Attribute(name="with-name",required=false) String _withName;
public String getWithName() { return _withName; }
public void setWithName(String value) { _withName = value; }
}
@Root(name = "show-layer"
)
public static class ShowLayer {
}
@Root(name = "allow-summary"
)
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
public void setShowList(ShowList value) { _showList = value; }
public ShowGrid getShowGrid() { return _showGrid; }
public void setShowGrid(ShowGrid value) { _showGrid = value; }
public ShowLocation getShowLocation() { return _showLocation; }
public void setShowLocation(ShowLocation value) { _showLocation = value; }
public ShowLayer getShowLayer() { return _showLayer; }
public void setShowLayer(ShowLayer value) { _showLayer = value; }
public boolean allowSummary() { return (_allowSummary != null); }
public AllowSummary getAllowSummary() { return _allowSummary; }
public void setAllowSummary(boolean value) { if(value) _allowSummary = new AllowSummary(); else _allowSummary = null;}

}







































