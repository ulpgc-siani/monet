package org.monet.metamodel;


import org.monet.bpi.BehaviorProject;
import org.monet.bpi.types.Event;

/**
 * Manifest Una definición que describe una entidad en el modelo de negocio
 */

public abstract class Project extends ProjectBase implements BehaviorProject {
	private String release;
	private String uuid;

	public abstract String getMetamodelVersion();

	public String getRelease() {
		return this.release;
	}

	public void setRelease(String revision) {
		this.release = revision;
	}

	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public void onReceiveEvent(Event event) {
	}

}
