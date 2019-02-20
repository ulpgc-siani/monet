package org.monet.metamodel;

/**
 * ActivityActionProperty
 */

public abstract class ActivityActionProperty extends SimpleActionProperty {

	public void merge(ActivityActionProperty child) {
		super.merge(child);

	}

	public Class<?> getMetamodelClass() {
		return ActivityActionProperty.class;
	}

}
