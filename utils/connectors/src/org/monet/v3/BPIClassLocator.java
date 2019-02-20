package org.monet.v3;

import org.monet.bpi.Schema;
import org.monet.metamodel.DatastoreDefinitionBase;
import org.monet.metamodel.Definition;
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.interfaces.HasBehaviour;
import org.monet.metamodel.interfaces.HasSchema;

public class BPIClassLocator {

	@SuppressWarnings("unchecked")
	public <T> T instantiateBehaviour(Definition definition) {
		if (definition instanceof HasBehaviour) {
			try {
				return (T) ((HasBehaviour) definition).getBehaviourClass().newInstance();
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T instantiateBehaviour(Class<?> behaviourClass) {
		try {
			return (T) behaviourClass.newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Schema> Class<T> getSchemaClass(Definition definition) {
		if (definition instanceof HasSchema) {
			try {
				return (Class<T>) ((HasSchema) definition).getSchemaClass();
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T instantiateSchema(Definition definition) {
		if (definition instanceof HasSchema) {
			try {
				return (T) ((HasSchema) definition).getSchemaClass().newInstance();
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T instantiateBehaviour(FieldProperty declaration) {
		if (declaration instanceof HasBehaviour) {
			try {
				return (T) ((HasBehaviour) declaration).getBehaviourClass().newInstance();
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T instantiateBehaviour(DatastoreDefinitionBase.DimensionProperty declaration) {
		if (declaration instanceof HasBehaviour) {
			try {
				return (T) ((HasBehaviour) declaration).getBehaviourClass().newInstance();
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T instantiateBehaviour(DatastoreDefinitionBase.CubeProperty declaration) {
		if (declaration instanceof HasBehaviour) {
			try {
				return (T) ((HasBehaviour) declaration).getBehaviourClass().newInstance();
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

}
