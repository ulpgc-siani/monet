package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
ThesaurusProviderDefinition
Un proveedor de tesauros es un tipo de proveedor que obtiene términos desde un tesauro externo

*/

public  class ThesaurusProviderDefinition extends ProviderDefinition {

	
	
	

	public void merge(ThesaurusProviderDefinition child) {
		super.merge(child);
		
		
		
		
	}

	public Class<?> getMetamodelClass() {
		return ThesaurusProviderDefinition.class;
	}

}

