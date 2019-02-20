package client.services;

import client.core.model.Entity;
import client.core.model.Space;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.factory.EntityFactory;
import client.services.callback.DefinitionCallback;
import client.services.callback.SpaceCallback;

import static client.core.model.Instance.ClassName;

public interface SpaceService extends Service {
	void load(SpaceCallback callback);
	void loadDefinition(final String definitionKey, final ClassName definitionClassName, final DefinitionCallback<EntityDefinition> callback);
	<T extends Entity> void loadDefinition(final T entity, final DefinitionCallback<EntityDefinition> callback);
	Space load();
	EntityFactory getEntityFactory();
}
