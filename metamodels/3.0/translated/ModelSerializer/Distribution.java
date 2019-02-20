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

	
	public static class ShowProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _tabEnvironment = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getTabEnvironment() { return _tabEnvironment; }public void setTabEnvironment(ArrayList<org.monet.metamodel.internal.Ref> value) { _tabEnvironment = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _tabDashboard = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getTabDashboard() { return _tabDashboard; }public void setTabDashboard(ArrayList<org.monet.metamodel.internal.Ref> value) { _tabDashboard = value; }public static class TabRolesProperty  {protected void copy(TabRolesProperty instance) {}protected void merge(TabRolesProperty child) {}}protected TabRolesProperty _tabRolesProperty;public TabRolesProperty getTabRoles() { return _tabRolesProperty; }public void setTabRoles(TabRolesProperty value) { if(_tabRolesProperty!=null) _tabRolesProperty.merge(value); else {_tabRolesProperty = value;} }public static class TabTasktrayProperty  {protected void copy(TabTasktrayProperty instance) {}protected void merge(TabTasktrayProperty child) {}}protected TabTasktrayProperty _tabTasktrayProperty;public TabTasktrayProperty getTabTasktray() { return _tabTasktrayProperty; }public void setTabTasktray(TabTasktrayProperty value) { if(_tabTasktrayProperty!=null) _tabTasktrayProperty.merge(value); else {_tabTasktrayProperty = value;} }public static class TabTaskboardProperty  {protected void copy(TabTaskboardProperty instance) {}protected void merge(TabTaskboardProperty child) {}}protected TabTaskboardProperty _tabTaskboardProperty;public TabTaskboardProperty getTabTaskboard() { return _tabTaskboardProperty; }public void setTabTaskboard(TabTaskboardProperty value) { if(_tabTaskboardProperty!=null) _tabTaskboardProperty.merge(value); else {_tabTaskboardProperty = value;} }public static class TabNewsProperty  {protected void copy(TabNewsProperty instance) {}protected void merge(TabNewsProperty child) {}}protected TabNewsProperty _tabNewsProperty;public TabNewsProperty getTabNews() { return _tabNewsProperty; }public void setTabNews(TabNewsProperty value) { if(_tabNewsProperty!=null) _tabNewsProperty.merge(value); else {_tabNewsProperty = value;} }public static class TabTrashProperty  {protected void copy(TabTrashProperty instance) {}protected void merge(TabTrashProperty child) {}}protected TabTrashProperty _tabTrashProperty;public TabTrashProperty getTabTrash() { return _tabTrashProperty; }public void setTabTrash(TabTrashProperty value) { if(_tabTrashProperty!=null) _tabTrashProperty.merge(value); else {_tabTrashProperty = value;} }protected void copy(ShowProperty instance) {this._tabEnvironment.addAll(instance._tabEnvironment);
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
		
		
		if(_showProperty == null) _showProperty = child._showProperty; else if (child._showProperty != null) {_showProperty.merge(child._showProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return Distribution.class;
	}

}

