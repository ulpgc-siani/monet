package org.monet.space.kernel.bpi.java.locator;

import org.monet.bpi.Schema;
import org.monet.metamodel.DatastoreDefinitionBase.CubeProperty;
import org.monet.metamodel.DatastoreDefinitionBase.DimensionProperty;
import org.monet.metamodel.Definition;
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.interfaces.HasBehaviour;
import org.monet.metamodel.interfaces.HasSchema;
import org.monet.space.kernel.agents.AgentLogger;

public class BPIClassLocator {

	private static BPIClassLocator instance;

	public static synchronized BPIClassLocator getInstance() {
		if (instance == null) instance = new BPIClassLocator();
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T> T instantiateBehaviour(Definition definition) {
		if (definition instanceof HasBehaviour) {
			try {
				return (T) ((HasBehaviour) definition).getBehaviourClass().newInstance();
			} catch (Exception e) {
				AgentLogger.getInstance().error(e);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T instantiateBehaviour(Class<?> behaviourClass) {
		try {
			return (T) behaviourClass.newInstance();
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T extends Schema> Class<T> getSchemaClass(Definition definition) {
		if (definition instanceof HasSchema) {
			try {
				return (Class<T>) ((HasSchema) definition).getSchemaClass();
			} catch (Exception e) {
				AgentLogger.getInstance().error(e);
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
				AgentLogger.getInstance().error(e);
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
				AgentLogger.getInstance().error(e);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T instantiateBehaviour(DimensionProperty declaration) {
		if (declaration instanceof HasBehaviour) {
			try {
				return (T) ((HasBehaviour) declaration).getBehaviourClass().newInstance();
			} catch (Exception e) {
				AgentLogger.getInstance().error(e);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T instantiateBehaviour(CubeProperty declaration) {
		if (declaration instanceof HasBehaviour) {
			try {
				return (T) ((HasBehaviour) declaration).getBehaviourClass().newInstance();
			} catch (Exception e) {
				AgentLogger.getInstance().error(e);
			}
		}
		return null;
	}

}
