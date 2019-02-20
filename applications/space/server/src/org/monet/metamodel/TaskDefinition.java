package org.monet.metamodel;

import org.monet.space.kernel.model.DefinitionType;

import java.util.Collection;

/**
 * TaskDefinition Un trabajo o job es una pequeña tarea o actividad que se
 * realiza en un entorno de mobilidad
 */

public class TaskDefinition extends TaskDefinitionBase {

	public enum Type {
		service, activity, job
	}

	@Override
	public DefinitionType getType() {
		if (this.isService())
			return DefinitionType.service;
		else if (this.isActivity())
			return DefinitionType.activity;
		else if (this.isJob())
			return DefinitionType.job;
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

	public SendJobActionProperty getSendJobAction(String code) {
		if (this.isActivity()) {
			Collection<ActivityDefinition.ActivityPlaceProperty> placeDefinitionList = ((ActivityDefinition)this).getPlaceList();

			for (PlaceProperty placeDefinition : placeDefinitionList) {
				if (placeDefinition.getSendJobActionProperty() != null && placeDefinition.getSendJobActionProperty().getCode().equals(code))
					return placeDefinition.getSendJobActionProperty();
			}
		}
		else if (this.isService()) {
			Collection<ServiceDefinition.ServicePlaceProperty> placeDefinitionList = ((ServiceDefinition)this).getPlaceList();

			for (PlaceProperty placeDefinition : placeDefinitionList) {
				if (placeDefinition.getSendJobActionProperty() != null && placeDefinition.getSendJobActionProperty().getCode().equals(code))
					return placeDefinition.getSendJobActionProperty();
			}
		}

		return null;
	}

}
