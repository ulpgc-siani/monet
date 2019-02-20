package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
AnalysisboardDefinition
Representa el panel de control del sistema de información. En él se podrá tener una visión del estado actual de la organización

*/

public  class AnalysisboardDefinitionBase extends Definition {

	public enum ScaleEnumeration { MONTHS,DAYS,HOURS,MINUTES,SECONDS }protected ScaleEnumeration _scale;public ScaleEnumeration getScale() { return _scale; }public void setScale(ScaleEnumeration value) { _scale = value; }
	public static class AnalysisboardViewProperty extends org.monet.metamodel.ViewProperty {protected ArrayList<FolderProperty> _folderPropertyList = new ArrayList<FolderProperty>();public void addFolderProperty(FolderProperty value) { _folderPropertyList.add(value); }public ArrayList<FolderProperty> getFolderPropertyList() { return _folderPropertyList; }protected void copy(AnalysisboardViewProperty instance) {this._code = instance._code;
this._name = instance._name;
_folderPropertyList.addAll(instance._folderPropertyList);}protected void merge(AnalysisboardViewProperty child) {super.merge(child);_folderPropertyList.addAll(child._folderPropertyList);}}protected LinkedHashMap<String, AnalysisboardViewProperty> _analysisboardViewPropertyMap = new LinkedHashMap<String, AnalysisboardViewProperty>();public void addView(AnalysisboardViewProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();AnalysisboardViewProperty current = _analysisboardViewPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {AnalysisboardViewProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_analysisboardViewPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_analysisboardViewPropertyMap.put(key, value);} }public java.util.Map<String,AnalysisboardViewProperty> getViewMap() { return _analysisboardViewPropertyMap; }public java.util.Collection<AnalysisboardViewProperty> getViewList() { return _analysisboardViewPropertyMap.values(); }
	

	public void copy(AnalysisboardDefinitionBase instance) {
		this._scale = instance._scale;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		for(AnalysisboardViewProperty item : instance._analysisboardViewPropertyMap.values())this.addView(item);
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(AnalysisboardDefinitionBase child) {
		super.merge(child);
		
		if(child._scale != null)this._scale = child._scale;

		for(AnalysisboardViewProperty item : child._analysisboardViewPropertyMap.values())this.addView(item);

		
	}

	public Class<?> getMetamodelClass() {
		return AnalysisboardDefinitionBase.class;
	}

}

