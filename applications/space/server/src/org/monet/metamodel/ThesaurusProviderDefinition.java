package org.monet.metamodel;

/**
 * ThesaurusProviderDefinition Un proveedor de tesauros es un tipo de proveedor
 * que obtiene términos desde un tesauro externo
 */

public class ThesaurusProviderDefinition extends ProviderDefinition {

	public void merge(ThesaurusProviderDefinition child) {
		super.merge(child);

	}

	public Class<?> getMetamodelClass() {
		return ThesaurusProviderDefinition.class;
	}

}
