package org.monet.metamodel;

/**
 * SensorDefinition Un sensor es una pequeña tarea o actividad que se realiza en
 * un entorno de mobilidad
 */

public class SensorDefinition extends SensorDefinitionBase {

	@Override
	public org.monet.mobile.model.TaskDefinition toMobileDefinition() {
		org.monet.mobile.model.SensorDefinition definition = new org.monet.mobile.model.SensorDefinition();

		this.fillJobDefinition(definition);

		return definition;
	}

}
