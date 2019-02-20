package org.monet.metamodel.interfaces;

import org.monet.metamodel.internal.SchemaDefinition;

public interface HasSchema {

	Class<?> getSchemaClass();

	SchemaDefinition getSchemaDefinition();

}
