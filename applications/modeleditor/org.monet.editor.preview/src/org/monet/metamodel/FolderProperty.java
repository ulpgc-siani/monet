package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
FolderProperty
Esta propiedad se utiliza para clasificar los indicadores de un panel de control de la organización

*/

public  class FolderProperty  {

	protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _indicator = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getIndicator() { return _indicator; }public void setIndicator(ArrayList<org.monet.metamodel.internal.Ref> value) { _indicator = value; }
	
	protected ArrayList<FolderProperty> _folderPropertyList = new ArrayList<FolderProperty>();public void addFolderProperty(FolderProperty value) { _folderPropertyList.add(value); }public ArrayList<FolderProperty> getFolderPropertyList() { return _folderPropertyList; }

	public void copy(FolderProperty instance) {
		this._label = instance._label;
this._indicator.addAll(instance._indicator);

		
		_folderPropertyList.addAll(instance._folderPropertyList);
	}

	public void merge(FolderProperty child) {
		
		
		if(child._label != null)this._label = child._label;
if(child._indicator != null)this._indicator.addAll(child._indicator);

		
		_folderPropertyList.addAll(child._folderPropertyList);
	}

	public Class<?> getMetamodelClass() {
		return FolderProperty.class;
	}

}

