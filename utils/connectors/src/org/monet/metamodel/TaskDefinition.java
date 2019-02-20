package org.monet.metamodel;

/**
 * TaskDefinition Un trabajo o job es una pequeña tarea o actividad que se
 * realiza en un entorno de mobilidad
 */

public class TaskDefinition extends TaskDefinitionBase {

	public enum Type {
		service, activity, job
	}

	public Type getType() {
		if (this.isService())
			return Type.service;
		else if (this.isActivity())
			return Type.activity;
		else if (this.isJob())
			return Type.job;
		return null;
	}

	public boolean isService() {
		return this instanceof ServiceDefinition;
	}

	public boolean isActivity() {
		return this instanceof ActivityDefinition;
	}

	public boolean isJob() {
		return this instanceof JobDefinition;
	}

	public boolean isSensor() {
		return this instanceof SensorDefinition;
	}

	public boolean isPublic() {
		return this._isPrivate == null;
	}

}
