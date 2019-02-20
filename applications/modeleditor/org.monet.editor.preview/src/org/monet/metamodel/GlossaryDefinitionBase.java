package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
GlossaryDefinition
Un glosario es una fuente en la cual el conjunto de términos se obtienen a partir de los datos de un índice.
Se utiliza cuando los términos de una fuente son dinámicos
*/

public  class GlossaryDefinitionBase extends SourceDefinition {

	protected org.monet.metamodel.internal.Ref _index;public org.monet.metamodel.internal.Ref getIndex() { return _index; }public void setIndex(org.monet.metamodel.internal.Ref value) { _index = value; }protected org.monet.metamodel.internal.Ref _key;public org.monet.metamodel.internal.Ref getKey() { return _key; }public void setKey(org.monet.metamodel.internal.Ref value) { _key = value; }protected org.monet.metamodel.internal.Ref _label;public org.monet.metamodel.internal.Ref getLabel() { return _label; }public void setLabel(org.monet.metamodel.internal.Ref value) { _label = value; }protected org.monet.metamodel.internal.Ref _parent;public org.monet.metamodel.internal.Ref getParent() { return _parent; }public void setParent(org.monet.metamodel.internal.Ref value) { _parent = value; }
	
	

	public void merge(GlossaryDefinitionBase child) {
		super.merge(child);
		
		if(child._index != null)this._index = child._index;
if(child._key != null)this._key = child._key;
if(child._label != null)this._label = child._label;
if(child._parent != null)this._parent = child._parent;

		
		
	}

	public Class<?> getMetamodelClass() {
		return GlossaryDefinitionBase.class;
	}

}

