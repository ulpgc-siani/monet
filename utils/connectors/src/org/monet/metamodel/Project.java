package org.monet.metamodel;


/**
 * Manifest Una definición describe una entidad en el modelo de negocio
 */

public abstract class Project extends ProjectBase {
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

}
