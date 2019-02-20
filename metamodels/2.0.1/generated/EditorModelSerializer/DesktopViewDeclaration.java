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

// DesktopViewDeclaration

@Root(name="view")
public  class DesktopViewDeclaration extends NodeViewDeclaration 
 {
@Root(name = "show"
)
public static class Show {
protected @Attribute(name="entity") String _entity;
protected @Attribute(name="view",required=false) String _view;
public String getEntity() { return _entity; }
public void setEntity(String value) { _entity = value; }
public String getView() { return _view; }
public void setView(String value) { _view = value; }
}
@Root(name = "show-news"
)
public static class ShowNews {
}

protected @ElementList(inline=true,required=false) ArrayList<Show> _showList = new ArrayList<Show>();
protected @Element(name="show-news",required=false) ShowNews _showNews;

public ArrayList<Show> getShowList() { return _showList; }
public ShowNews getShowNews() { return _showNews; }
public void setShowNews(ShowNews value) { _showNews = value; }

}







































