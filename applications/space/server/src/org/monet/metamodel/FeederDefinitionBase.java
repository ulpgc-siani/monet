package org.monet.metamodel;

/**
 * FeederDefinition Un feeder es el mecanismo de Monet que permite obtener
 * información de vocabulario de otra unidad de negocio Se utilizan para
 * clasificar la información
 */

public class FeederDefinitionBase extends SourceDefinition {

	public void merge(FeederDefinitionBase child) {
		super.merge(child);

	}

	public Class<?> getMetamodelClass() {
		return FeederDefinitionBase.class;
	}

}
