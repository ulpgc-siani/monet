package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
::typeComment::
::descriptionComment::
::hintComment::
*/

public ::abstract:: class ::className:: ::extends:: {

	::attributes::
	::properties::
	::includes::

	public void merge(::className:: child) {
		::mergeSuper::
		
		::mergeAttributes::
		::mergeProperties::
		::mergeIncludes::
	}

	public Class<?> getMetamodelClass() {
		return ::className::.class;
	}

}

@abstract
abstract

@extends
extends ::parentType::

@attribute$class
public enum ::token:: { ::values:: }

@attribute$member.simple
protected ::token:: ::name::;

@attribute$member.multiple
protected ArrayList<::token::> ::name:: = new ArrayList<::token::>();

@attribute$method.boolean
public boolean ::name::() { return ::memberName:: != null && ::memberName::; }
public void set::methodName::(boolean value) { ::memberName:: = value; }
		
@attribute$method.enumeration
public ::methodName::Enumeration get::methodName::() { return ::memberName::; }
public void set::methodName::(::methodName::Enumeration value) { ::memberName:: = value; }

@attribute$method.enumerationMultiple
public ArrayList<::methodName::Enumeration> get::methodName::() { return ::memberName::; }
public void set::methodName::(ArrayList<::methodName::Enumeration> value) { ::memberName:: = value; }

@attribute$method.multiple
public ArrayList<::type::> get::methodName::() { return ::memberName::; }
public void set::methodName::(ArrayList<::type::> value) { ::memberName:: = value; }

@attribute$method.simple
public ::type:: get::methodName::() { return ::memberName::; }
public void set::methodName::(::type:: value) { ::memberName:: = value; }


@property$class
public static class ::propertyClassName:: ::extends:: {
  ::attributes::
  ::content::
  ::children::
  ::includes::
  
  protected void merge(::propertyClassName:: child) {
    ::parentName|super.merge(child);::
    ::mergeAttributes::
    ::mergeProperties::
    ::mergeIncludes::
  }
}

@property$class$extends
extends org.monet.metamodel.::parentName::

@property$class$content
protected String content;
public String getContent() { return content; }
public void setContent(String content) { this.content = content; }

@property$member.map
protected LinkedHashMap<String, ::name::> ::memberName::Map = new LinkedHashMap<String, ::name::>();

@property$member.multiple
protected ArrayList<::name::> ::memberName::List = new ArrayList<::name::>();

@property$member.simple
protected ::name:: ::memberName::;

@property$method
::method::

@property$method.boolean
public boolean ::tokenBooleanMethod::() { return (::tokenVariable:: != null); }
public ::tokenClass:: ::tokenMethod:: { return ::tokenVariable::; }
public void ::tokenSetMethod:: { if(value) ::tokenVariable:: = new ::tokenClass::(); else {::tokenVariable:: = null;}}

@property$method.allowable
public boolean ::tokenBooleanMethod::() { return (::tokenVariable:: != null); }
public ::tokenClass:: ::tokenMethod:: { return ::tokenVariable::; }
public void ::tokenSetMethod:: { if(value) ::tokenVariable:: = new ::tokenClass::(); else {::tokenVariable:: = null;}}

@property$method.multipleWithName
public void ::tokenAddMethod:: {
  ::tokenClass:: current = ::tokenTypeMap::.get(value.getName());
  if (current != null) {
    if (current instanceof value) current = value;
    else current.merge(value);
  } else {
  	::tokenTypeMap::.put(value.getName(), value);
  } 
}
public java.util.Map<String,::tokenClass::> ::tokenMethodMap:: { return ::tokenTypeMap::; }
public java.util.Collection<::tokenClass::> ::tokenMethodList:: { return ::tokenTypeMap::.values(); }

@property$method.multipleWithNameAndCode
public void ::tokenAddMethod:: {
  String key = value.getName() != null ? value.getName() : value.getCode();
  ::tokenClass:: current = ::tokenTypeMap::.get(key);
  if (current != null) {
    if (current instanceof value) current = value;
    else current.merge(value);
  } else {
  	::tokenTypeMap::.put(key, value);
  } 
}
public java.util.Map<String,::tokenClass::> ::tokenMethodMap:: { return ::tokenTypeMap::; }
public java.util.Collection<::tokenClass::> ::tokenMethodList:: { return ::tokenTypeMap::.values(); }

@property$method.multiple
#set ($aux = $name + "List")
public ArrayList<::tokenClass::> ::tokenMethodList:: { return ::tokenTypeList::; }

@property$method.multiple.key
public String ::tokenMethodKey:: { 
  if(::tokenVariable::Map.get(::key::) == null) 
    return ""; 
  return ::tokenVariable::Map.get($key); 
}

public Collection<String> ::tokenMethodPluralName:: { 
  return ::tokenVariable::Map.values(); 
}

@property$method.other
public ::tokenClass:: ::tokenMethod:: { 
  return ::tokenVariable::; 
}

public void ::tokenSetMethodWithParameters:: { 
  if(::tokenVariable::!=null) 
    ::tokenVariable::.merge(value); 
  else {
    ::tokenVariable:: = value;
  } 
}

@mergeSuper
super.merge(child);

@mergeAttribute
if(child.::name:: != null)
	::asignation::

@mergeAttributeMultipleAsignation
this.::name::.addAll(child.::name::);

@mergeAttributeSimpleAsignation
this.::name:: = child.::name::;

@mergePropertyMultipleWithNames
for(::nameType:: item : child.::name::Map.values())
	this.add::nameMethod::(item);

@mergePropertyMultiple
::name::List.addAll(child.::name::List);

@mergePropertySimple
if(::name:: == null) 
	::name:: = child.::name::; 
else {
	::name::.merge(child.::name::);
}

@mergeIncludeMultipleWithNames
for(::type:: item : child.::typeMember::Map.values())
	this.add::type::(item);

@mergeIncludeMultiple
::typeMember::List.addAll(child.::typeMember::List);

@mergeIncludeSimple
this.set::type::(child.::typeMember::);

@include
::classList::

@include$class.multiple.map
protected LinkedHashMap<String, ::tokenType::> ::tokenTypeMap:: = new LinkedHashMap<String, ::tokenType::>();

@include$class.multiple.list
protected ArrayList<::tokenType::> ::tokenTypeList:: = new ArrayList<::tokenType::>();

@include$class.simple
protected ::tokenType:: ::tokenTypeVariable::;

@includeMethod
::methodList::

@includeMethod$class$parameters
::tokenType:: value

@includeMethod$class.multiple.map
public void add::tokenType::(::tokenType:: value) {
  String key = value.getName() != null ? value.getName() : value.getCode();
  ::tokenType:: current = ::tokenTypeMap::.get(key);
  if (current != null) {
    if (current instanceof value) current = value;
    else current.merge(value);
  } else {
  	::tokenTypeMap::.put(key, value);
  } 
}

public java.util.Map<String,::tokenType::> ::tokenMethodMap:: { 
  return ::tokenTypeMap::; 
}

public java.util.Collection<::tokenType::> ::tokenMethodList:: { 
  return ::tokenTypeMap::.values(); 
}

@includeMethod$class.multiple.list
public void add::tokenType::(::tokenType:: value) { 
  ::tokenTypeList::.add(value); 
}

public ArrayList<::tokenType::> ::tokenMethodList:: { 
  return ::tokenTypeList::; 
}

@includeMethod$class.simple
public ::tokenType:: ::tokenMethod:: { 
  return ::tokenTypeVariable::; 
}

public void ::tokenSetMethod:: { 
  if (::tokenTypeVariable:: == null) 
    ::tokenTypeVariable:: = value; 
  else {
    ::tokenTypeVariable::.merge(value);
  } 
}

@includeListHelper
protected ::helperType:: ::helperName::;
protected void create::helperClass::() {
  ::helperName:: = new ::helperType::();
  ::listHelper::
}

public ::helperType:: get::helperClass::() {
  if (::helperName:: == null) create::helperClass::();
  return ::helperName::;
}

@includeListHelper$item
::helperName::.addAll(::itemName::Map.values());

@includeMapHelper
protected ::helperType:: ::helperName::;
protected void create::helperClass::() {
  ::helperName:: = new ::helperType::();
  if (::listName:: == null) 
    create::listClass::();
  for (::type:: item \: ::listName::) {
    ::helperName::.put(item.getCode(), item);
    ::helperName::.put(item.getName(), item);
  }
}

public ::type:: get::helperItem::(String key) {
  if (::helperName:: == null) create::helperClass::();
  return ::helperName::.get(key);
}