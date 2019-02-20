package org.monet.editor.dsl.metamodel;

import java.util.Collection;
import java.util.HashMap;

public class MetaModelStructureImpl implements MetaModelStructure {

  private HashMap<String, Item> definitions = new HashMap<String, Item>();
  private HashMap<String, Item> declarations = new HashMap<String, Item>();
  private HashMap<String, String> clazzHierarchy = new HashMap<String, String>();
  
  public MetaModelStructureImpl() {
  	initProperties();
    initPropertyContents();
    initDefinitions();
  }
  
  private void initProperties() {
  	::referenciableProperties::
  }
  
  private void initPropertyContents() {
    Item declarationItem = null;
    ::declarationContents::
  }
  
  private void initDefinitions() {
  	::definitionCalls::
  }
  
  ::definitionMethods::
  
  public Item getDefinition(String name) {
    return this.definitions.get(name);
  }
  
  public Collection<String> getDefinitions() {
    return this.definitions.keySet();
  }

  public boolean areFamily(String child, String ancestor) {
    if(child.equals(ancestor))
      return true;
    while (child != null) {
      String childParent = this.clazzHierarchy.get(child);
      if (childParent != null && childParent.equals(ancestor)) {
        return true;
      } else {
        child = childParent;
      }
    }
    return false;
  }
  
}

@referenciableProperty
clazzHierarchy.put("::type::", "::parent::");
::concrete::

@referenciableProperty$concrete
Item ::itemName:: = new Item(Item.PROPERTY, "::type::", null, "::token::");
::itemName::.setValueTypeQualifiedName("org.monet.metamodel.::type::");
::itemName::.setDescription("::description::");
::itemName::.setHint("::hint::");
declarations.put(::itemName::.getToken(), ::itemName::);
declarations.put(::itemName::.getName(), ::itemName::);

@declarationContent
declarationItem = declarations.get("::type::");
::name::
::code::
::attributes::
::properties::
::methods::  
::includes::  

@declarationContent$name
declarationItem.setHasName(true, ::required::);

@declarationContent$code
declarationItem.setHasCode(true, ::required::);

@attribute
Item ::itemName:: = new Item(Item.ATTRIBUTE, "::token::", "::metamodeltype::", "::token::", ::required::, ::multiple::);
::itemName::.setValueTypeQualifiedName("::attributeGramaticType::");
::itemName::.setDescription("::description::");
::itemName::.setHint("::hint::");
::enumItems::
::parentItem::.addChild(::itemName::);

@attribute$enumItem
Item ::enumItem:: = new Item(Item.ENUMVALUE, "::token::", null, "::token::");
::enumItem::.setDescription("::description::");
::enumItem::.setHint("::hint::");
::parentItem::.addChild(::enumItem::);

@property
::clazzHierarchy::
Item ::itemName:: = new Item(Item.PROPERTY, "::typeOrToken::", null, "::token::", ::required::, ::multiple::);
::itemName::.setDescription("::description::");
::itemName::.setHint("::hint::");
::baseType::
::attributes::
::properties::
::methods::
::includes::
::content::
::name::
::code::
::parentItem::.addChild(::itemName::);

@property$baseType
::itemName::.setRefBaseType("::baseType::");

@property$clazzHierarchy
clazzHierarchy.put("::type::", "::parent::");

@property$content
Item ::contentItemName:: = new Item(Item.ATTRIBUTE, "content", null, "content", true, false);
::contentItemName::.setValueTypeQualifiedName("java.lang.String");
::contentItemName::.setDescription("::description::");
::contentItemName::.setHint("::hint::");
::itemName::.addChild(::contentItemName::);

@property$name
::itemName::.setHasName(true, ::nameRequired::);

@property$code
::itemName::.setHasCode(true, ::codeRequired::);

@method
Item ::itemName:: = new Item(Item.METHOD, "::name::", null, "::name::", ::required::);
::itemName::.setDescription("::description::");
::itemName::.setHint("::hint::");
::parentItem::.addChild(::itemName::);
::parameters::

@method$parameter
Item ::itemName:: = new Item(Item.PARAMETER, "::name::", null, "::name::", true, false);
::itemName::.setValueTypeQualifiedName("::type::");
::itemName::.setDescription("::description::");
::itemName::.setHint("::hint::");
::parentItem::.addChild(::itemName::);

@include
::items::

@include$item
ItemProxy ::itemName:: = new ItemProxy(declarations.get("::name::"), ::required::, ::multiple::);
::parentItem::.addChild(::itemName::);

@definitionCall
this.initDefinition::index::();

@definitionMethod
private void initDefinition::index::() {
  	Item definitionItem = null;
	clazzHierarchy.put("::type::", "::parent::");
	::concrete::
}

@definition$concrete
/*Insert of definition*/
definitionItem = new Item(Item.DEFINITION, "::type::", null, "::token::");
definitionItem.setDescription("::description::");
definitionItem.setHint("::hint::");
definitions.put(definitionItem.getToken(), definitionItem);
::name::
::code::
::attributes::
::properties::
::methods::  
::includes::  

@definition$concrete$name
definitionItem.setHasName(true, ::required::);

@definition$concrete$code
definitionItem.setHasCode(true, ::required::);

