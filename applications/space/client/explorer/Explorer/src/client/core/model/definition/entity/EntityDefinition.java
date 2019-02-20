package client.core.model.definition.entity;

import client.core.model.definition.Definition;

import static client.core.model.Instance.ClassName;

public interface EntityDefinition extends Definition {
	ClassName CLASS_NAME = new ClassName("EntityDefinition");

	enum Type {
		ABSTRACT,
		CONTAINER, DESKTOP, COLLECTION, FORM, DOCUMENT, CATALOG,
		CUBE, MAP,
		THESAURUS_SERVICE,
		SERVICE,
		ACTIVITY,
		JOB,
		MAP_PROVIDER,
		THESAURUS_PROVIDER,
		CUBE_PROVIDER,
		DELEGATE,
		WORKER,
		SERVICE_PROVIDER,
		TASK,
		INDEX,
		THESAURUS,
		GLOSSARY,
		FEEDER,
		IMPORTER,
		EXPORTER,
		ROLE,
		DATASTORE,
		TASK_BOARD,
		TASK_TRAY;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

		public static Type fromString(String type) {
			return Type.valueOf(type.toUpperCase());
		}
	}

}
