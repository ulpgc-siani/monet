package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// DesktopViewDeclaration

@Root(name="view")
public  class DesktopViewDeclaration extends NodeViewDeclaration 
 {
@Root(name="show")
public static class Show {
protected @Attribute(name="entity") String _entity;
protected @Attribute(name="view",required=false) String _view;
public String getEntity() { return _entity; }
public String getView() { return _view; }
}
@Root(name="show-news")
public static class ShowNews {
}

protected @ElementList(inline=true,required=false) ArrayList<Show> _showList = new ArrayList<Show>();
protected @Element(name="show-news",required=false) ShowNews _showNews;

public ArrayList<Show> getShowList() { return _showList; }
public ShowNews getShowNews() { return _showNews; }

}







































