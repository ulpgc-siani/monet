package org.monet.metamodel;

/**
 * FeederDefinition Un feeder es el mecanismo de Monet que permite obtener
 * información de vocabulario de otra unidad de negocio Se utilizan para
 * clasificar la información
 */

public class FeederDefinition extends SourceDefinition {

	public void merge(FeederDefinition child) {
		super.merge(child);

	}

	public Class<?> getMetamodelClass() {
		return FeederDefinition.class;
	}

}
