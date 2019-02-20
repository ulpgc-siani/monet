package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
Distribution
Define cómo va a ser el despliege del modelo de negocio en un espacio de negocio

*/

public  class Distribution extends AbstractManifest {

	public enum TimezoneEnumeration { ATLANTIC_BAR_BERMUDA,CANADA_BAR_ATLANTIC,ATLANTIC_BAR_STANLEY,ATLANTIC_BAR_SOUTH_GEORGIA,ATLANTIC_BAR_AZORES,ATLANTIC_BAR_CAPE_VERDE,ATLANTIC_BAR_CANARY,ATLANTIC_BAR_FAEROE,ATLANTIC_BAR_FAROE,ATLANTIC_BAR_MADEIRA,ATLANTIC_BAR_REYKJAVIK,ATLANTIC_BAR_ST_HELENA,EUROPE_BAR_BELFAST,EUROPE_BAR_DUBLIN,EUROPE_BAR_GUERNSEY,EUROPE_BAR_ISLE_OF_MAN,EUROPE_BAR_JERSEY,EUROPE_BAR_LISBON,EUROPE_BAR_LONDON,ATLANTIC_BAR_JAN_MAYEN,EUROPE_BAR_AMSTERDAM,EUROPE_BAR_ANDORRA,EUROPE_BAR_BELGRADE,EUROPE_BAR_BERLIN,EUROPE_BAR_BRATISLAVA,EUROPE_BAR_BRUSSELS,EUROPE_BAR_BUDAPEST,EUROPE_BAR_BUSINGEN,EUROPE_BAR_COPENHAGEN,EUROPE_BAR_GIBRALTAR,EUROPE_BAR_LJUBLJANA,EUROPE_BAR_LUXEMBOURG,EUROPE_BAR_MADRID,EUROPE_BAR_MALTA,EUROPE_BAR_MONACO,EUROPE_BAR_OSLO,EUROPE_BAR_PARIS,EUROPE_BAR_PODGORICA,EUROPE_BAR_PRAGUE,EUROPE_BAR_ROME,EUROPE_BAR_SAN_MARINO,EUROPE_BAR_SARAJEVO,EUROPE_BAR_SKOPJE,EUROPE_BAR_STOCKHOLM,EUROPE_BAR_TIRANE,EUROPE_BAR_VADUZ,EUROPE_BAR_VATICAN,EUROPE_BAR_VIENNA,EUROPE_BAR_WARSAW,EUROPE_BAR_ZAGREB,EUROPE_BAR_ZURICH,EUROPE_BAR_ATHENS,EUROPE_BAR_BUCHAREST,EUROPE_BAR_CHISINAU,EUROPE_BAR_HELSINKI,EUROPE_BAR_ISTANBUL,EUROPE_BAR_KIEV,EUROPE_BAR_MARIEHAMN,EUROPE_BAR_NICOSIA,EUROPE_BAR_RIGA,EUROPE_BAR_SIMFEROPOL,EUROPE_BAR_SOFIA,EUROPE_BAR_TALLINN,EUROPE_BAR_TIRASPOL,EUROPE_BAR_UZHGOROD,EUROPE_BAR_VILNIUS,EUROPE_BAR_ZAPOROZHYE,EUROPE_BAR_KALININGRAD,EUROPE_BAR_MINSK,EUROPE_BAR_MOSCOW,EUROPE_BAR_SAMARA,EUROPE_BAR_VOLGOGRAD }protected TimezoneEnumeration _timezone;public TimezoneEnumeration getTimezone() { return _timezone; }public void setTimezone(TimezoneEnumeration value) { _timezone = value; }
	public static class ShowProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _tabEnvironment = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getTabEnvironment() { return _tabEnvironment; }public void setTabEnvironment(ArrayList<org.monet.metamodel.internal.Ref> value) { _tabEnvironment = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _tabDashboard = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getTabDashboard() { return _tabDashboard; }public void setTabDashboard(ArrayList<org.monet.metamodel.internal.Ref> value) { _tabDashboard = value; }public static class TabRolesProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _for = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getFor() { return _for; }public void setFor(ArrayList<org.monet.metamodel.internal.Ref> value) { _for = value; }protected void copy(TabRolesProperty instance) {this._for.addAll(instance._for);
}protected void merge(TabRolesProperty child) {if(child._for != null)this._for.addAll(child._for);
}}protected TabRolesProperty _tabRolesProperty;public TabRolesProperty getTabRoles() { return _tabRolesProperty; }public void setTabRoles(TabRolesProperty value) { if(_tabRolesProperty!=null) _tabRolesProperty.merge(value); else {_tabRolesProperty = value;} }public static class TabTasktrayProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _for = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getFor() { return _for; }public void setFor(ArrayList<org.monet.metamodel.internal.Ref> value) { _for = value; }public enum ViewEnumeration { ALL,ALIVE,ACTIVE,PENDING,FINISHED }protected ViewEnumeration _view;public ViewEnumeration getView() { return _view; }public void setView(ViewEnumeration value) { _view = value; }protected void copy(TabTasktrayProperty instance) {this._for.addAll(instance._for);
this._view = instance._view;
}protected void merge(TabTasktrayProperty child) {if(child._for != null)this._for.addAll(child._for);
if(child._view != null)this._view = child._view;
}}protected TabTasktrayProperty _tabTasktrayProperty;public TabTasktrayProperty getTabTasktray() { return _tabTasktrayProperty; }public void setTabTasktray(TabTasktrayProperty value) { if(_tabTasktrayProperty!=null) _tabTasktrayProperty.merge(value); else {_tabTasktrayProperty = value;} }public static class TabTaskboardProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _for = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getFor() { return _for; }public void setFor(ArrayList<org.monet.metamodel.internal.Ref> value) { _for = value; }public enum ViewEnumeration { ALL,ALIVE,ACTIVE,PENDING,FINISHED }protected ViewEnumeration _view;public ViewEnumeration getView() { return _view; }public void setView(ViewEnumeration value) { _view = value; }protected void copy(TabTaskboardProperty instance) {this._for.addAll(instance._for);
this._view = instance._view;
}protected void merge(TabTaskboardProperty child) {if(child._for != null)this._for.addAll(child._for);
if(child._view != null)this._view = child._view;
}}protected TabTaskboardProperty _tabTaskboardProperty;public TabTaskboardProperty getTabTaskboard() { return _tabTaskboardProperty; }public void setTabTaskboard(TabTaskboardProperty value) { if(_tabTaskboardProperty!=null) _tabTaskboardProperty.merge(value); else {_tabTaskboardProperty = value;} }public static class TabNewsProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _for = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getFor() { return _for; }public void setFor(ArrayList<org.monet.metamodel.internal.Ref> value) { _for = value; }protected void copy(TabNewsProperty instance) {this._for.addAll(instance._for);
}protected void merge(TabNewsProperty child) {if(child._for != null)this._for.addAll(child._for);
}}protected TabNewsProperty _tabNewsProperty;public TabNewsProperty getTabNews() { return _tabNewsProperty; }public void setTabNews(TabNewsProperty value) { if(_tabNewsProperty!=null) _tabNewsProperty.merge(value); else {_tabNewsProperty = value;} }public static class TabTrashProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _for = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getFor() { return _for; }public void setFor(ArrayList<org.monet.metamodel.internal.Ref> value) { _for = value; }protected void copy(TabTrashProperty instance) {this._for.addAll(instance._for);
}protected void merge(TabTrashProperty child) {if(child._for != null)this._for.addAll(child._for);
}}protected TabTrashProperty _tabTrashProperty;public TabTrashProperty getTabTrash() { return _tabTrashProperty; }public void setTabTrash(TabTrashProperty value) { if(_tabTrashProperty!=null) _tabTrashProperty.merge(value); else {_tabTrashProperty = value;} }protected void copy(ShowProperty instance) {this._tabEnvironment.addAll(instance._tabEnvironment);
this._tabDashboard.addAll(instance._tabDashboard);
this._tabRolesProperty = instance._tabRolesProperty; 
this._tabTasktrayProperty = instance._tabTasktrayProperty; 
this._tabTaskboardProperty = instance._tabTaskboardProperty; 
this._tabNewsProperty = instance._tabNewsProperty; 
this._tabTrashProperty = instance._tabTrashProperty; 
}protected void merge(ShowProperty child) {if(child._tabEnvironment != null)this._tabEnvironment.addAll(child._tabEnvironment);
if(child._tabDashboard != null)this._tabDashboard.addAll(child._tabDashboard);
if(_tabRolesProperty == null) _tabRolesProperty = child._tabRolesProperty; else if (child._tabRolesProperty != null) {_tabRolesProperty.merge(child._tabRolesProperty);}
if(_tabTasktrayProperty == null) _tabTasktrayProperty = child._tabTasktrayProperty; else if (child._tabTasktrayProperty != null) {_tabTasktrayProperty.merge(child._tabTasktrayProperty);}
if(_tabTaskboardProperty == null) _tabTaskboardProperty = child._tabTaskboardProperty; else if (child._tabTaskboardProperty != null) {_tabTaskboardProperty.merge(child._tabTaskboardProperty);}
if(_tabNewsProperty == null) _tabNewsProperty = child._tabNewsProperty; else if (child._tabNewsProperty != null) {_tabNewsProperty.merge(child._tabNewsProperty);}
if(_tabTrashProperty == null) _tabTrashProperty = child._tabTrashProperty; else if (child._tabTrashProperty != null) {_tabTrashProperty.merge(child._tabTrashProperty);}
}}protected ShowProperty _showProperty;public ShowProperty getShow() { return _showProperty; }public void setShow(ShowProperty value) { if(_showProperty!=null) _showProperty.merge(value); else {_showProperty = value;} }
	

	public void copy(Distribution instance) {
		this._timezone = instance._timezone;
this._name = instance._name;
this._title = instance._title;
this._subtitle = instance._subtitle;
this._script.addAll(instance._script);

		this._showProperty = instance._showProperty; 
this._spaceProperty = instance._spaceProperty; 
this._defaultLocationProperty = instance._defaultLocationProperty; 
this._federationProperty = instance._federationProperty; 
_publishPropertyList.addAll(instance._publishPropertyList);
_unpublishPropertyList.addAll(instance._unpublishPropertyList);
this._disableProperty = instance._disableProperty; 
_assignRolePropertyList.addAll(instance._assignRolePropertyList);

		
	}

	public void merge(Distribution child) {
		super.merge(child);
		
		if(child._timezone != null)this._timezone = child._timezone;

		if(_showProperty == null) _showProperty = child._showProperty; else if (child._showProperty != null) {_showProperty.merge(child._showProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return Distribution.class;
	}

}

