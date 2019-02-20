package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
FieldProperty
Declaracion del tipo abstracto de campo

*/

public abstract class FieldPropertyBase extends ReferenceableProperty {

	protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected Object _description;public Object getDescription() { return _description; }public void setDescription(Object value) { _description = value; }public enum TemplateEnumeration { INLINE,MULTILINE }protected TemplateEnumeration _template;public TemplateEnumeration getTemplate() { return _template; }public void setTemplate(TemplateEnumeration value) { _template = value; }
	public static class IsRequired  {protected void copy(IsRequired instance) {}protected void merge(IsRequired child) {}}protected IsRequired _isRequired;public boolean isRequired() { return (_isRequired != null); }public IsRequired getIsRequired() { return _isRequired; }public void setIsRequired(boolean value) { if(value) _isRequired = new IsRequired(); else {_isRequired = null;}}public static class IsReadonly  {protected void copy(IsReadonly instance) {}protected void merge(IsReadonly child) {}}protected IsReadonly _isReadonly;public boolean isReadonly() { return (_isReadonly != null); }public IsReadonly getIsReadonly() { return _isReadonly; }public void setIsReadonly(boolean value) { if(value) _isReadonly = new IsReadonly(); else {_isReadonly = null;}}public static class IsCollapsible  {protected void copy(IsCollapsible instance) {}protected void merge(IsCollapsible child) {}}protected IsCollapsible _isCollapsible;public boolean isCollapsible() { return (_isCollapsible != null); }public IsCollapsible getIsCollapsible() { return _isCollapsible; }public void setIsCollapsible(boolean value) { if(value) _isCollapsible = new IsCollapsible(); else {_isCollapsible = null;}}public static class IsExtended  {protected void copy(IsExtended instance) {}protected void merge(IsExtended child) {}}protected IsExtended _isExtended;public boolean isExtended() { return (_isExtended != null); }public IsExtended getIsExtended() { return _isExtended; }public void setIsExtended(boolean value) { if(value) _isExtended = new IsExtended(); else {_isExtended = null;}}public static class IsStatic  {protected void copy(IsStatic instance) {}protected void merge(IsStatic child) {}}protected IsStatic _isStatic;public boolean isStatic() { return (_isStatic != null); }public IsStatic getIsStatic() { return _isStatic; }public void setIsStatic(boolean value) { if(value) _isStatic = new IsStatic(); else {_isStatic = null;}}public static class IsUnivocal  {protected void copy(IsUnivocal instance) {}protected void merge(IsUnivocal child) {}}protected IsUnivocal _isUnivocal;public boolean isUnivocal() { return (_isUnivocal != null); }public IsUnivocal getIsUnivocal() { return _isUnivocal; }public void setIsUnivocal(boolean value) { if(value) _isUnivocal = new IsUnivocal(); else {_isUnivocal = null;}}public static class DisplayProperty  {protected Object _message;public Object getMessage() { return _message; }public void setMessage(Object value) { _message = value; }public enum WhenEnumeration { EMPTY,REQUIRED,READ_ONLY,INVALID }protected WhenEnumeration _when;public WhenEnumeration getWhen() { return _when; }public void setWhen(WhenEnumeration value) { _when = value; }protected void copy(DisplayProperty instance) {this._message = instance._message;
this._when = instance._when;
}protected void merge(DisplayProperty child) {if(child._message != null)this._message = child._message;
if(child._when != null)this._when = child._when;
}}protected ArrayList<DisplayProperty> _displayPropertyList = new ArrayList<DisplayProperty>();public ArrayList<DisplayProperty> getDisplayList() { return _displayPropertyList; }
	

	public void copy(FieldPropertyBase instance) {
		this._label = instance._label;
this._description = instance._description;
this._template = instance._template;
this._code = instance._code;
this._name = instance._name;

		this._isRequired = instance._isRequired; 
this._isReadonly = instance._isReadonly; 
this._isCollapsible = instance._isCollapsible; 
this._isExtended = instance._isExtended; 
this._isStatic = instance._isStatic; 
this._isUnivocal = instance._isUnivocal; 
_displayPropertyList.addAll(instance._displayPropertyList);

		
	}

	public void merge(FieldPropertyBase child) {
		super.merge(child);
		
		if(child._label != null)this._label = child._label;
if(child._description != null)this._description = child._description;
if(child._template != null)this._template = child._template;

		if(_isRequired == null) _isRequired = child._isRequired; else if (child._isRequired != null) {_isRequired.merge(child._isRequired);}
if(_isReadonly == null) _isReadonly = child._isReadonly; else if (child._isReadonly != null) {_isReadonly.merge(child._isReadonly);}
if(_isCollapsible == null) _isCollapsible = child._isCollapsible; else if (child._isCollapsible != null) {_isCollapsible.merge(child._isCollapsible);}
if(_isExtended == null) _isExtended = child._isExtended; else if (child._isExtended != null) {_isExtended.merge(child._isExtended);}
if(_isStatic == null) _isStatic = child._isStatic; else if (child._isStatic != null) {_isStatic.merge(child._isStatic);}
if(_isUnivocal == null) _isUnivocal = child._isUnivocal; else if (child._isUnivocal != null) {_isUnivocal.merge(child._isUnivocal);}
_displayPropertyList.addAll(child._displayPropertyList);

		
	}

	public Class<?> getMetamodelClass() {
		return FieldPropertyBase.class;
	}

}

