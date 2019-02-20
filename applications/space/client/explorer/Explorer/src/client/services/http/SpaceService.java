package client.services.http;

import client.core.model.Entity;
import client.core.model.List;
import client.core.model.Node;
import client.core.model.Space;
import client.core.model.definition.Definition;
import client.core.model.definition.entity.EntityDefinition;
import client.core.system.MonetList;
import client.services.Services;
import client.services.callback.DefinitionCallback;
import client.services.callback.NodeCallback;
import client.services.callback.SpaceCallback;
import client.services.http.builders.EntityBuilder;
import client.services.http.dialogs.space.DefinitionDialog;
import client.services.http.dialogs.space.SpaceDialog;

import java.util.HashMap;
import java.util.Map;

import static client.core.model.Instance.ClassName;

public class SpaceService extends HttpService implements client.services.SpaceService {
	private static Space space;
	private static Map<String, Definition> definitionMap = new HashMap<>();
	private final Map<String, List<DefinitionCallback<EntityDefinition>>> callbackMap = new HashMap<>();

	public SpaceService(Stub stub, Services services) {
		super(stub, services);
	}

	@Override
	public void load(final SpaceCallback callback) {
		stub.request(new SpaceDialog(), Space.CLASS_NAME, new SpaceCallback() {
			@Override
			public void success(Space object) {
				space = object;

				services.getNodeService().open(space.getAccount().getRootNode().getId(), new NodeCallback() {
					@Override
					public void success(Node object) {
						space.getAccount().setRootNode(object);
						callback.success(space);
					}

					@Override
					public void failure(String error) {
						callback.failure(error);
					}
				});
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public <T extends Entity> void loadDefinition(final T entity, final DefinitionCallback<EntityDefinition> callback) {
		loadDefinition(entity.getDefinition().getCode(), EntityDefinition.CLASS_NAME, callback);
	}

	@Override
	public void loadDefinition(final String key, final ClassName definitionClassName, final DefinitionCallback<EntityDefinition> callback) {

		if (definitionMap.containsKey(key)) {
			callback.success((EntityDefinition)definitionMap.get(key));
			return;
		}

		if (callbackMap.containsKey(key)) {
			callbackMap.get(key).add(callback);
			return;
		}

		callbackMap.put(key, new MonetList<DefinitionCallback<EntityDefinition>>());
		callbackMap.get(key).add(callback);

		stub.request(new DefinitionDialog(key), definitionClassName, new DefinitionCallback<EntityDefinition>() {
			@Override
			public void success(EntityDefinition definition) {
				definitionMap.put(definition.getCode(), definition);
				definitionMap.put(definition.getName(), definition);
				for (DefinitionCallback<EntityDefinition> definitionCallback : callbackMap.get(key))
					definitionCallback.success(definition);
				callbackMap.remove(key);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public Space load() {
		return space;
	}

	@Override
	public client.core.model.factory.EntityFactory getEntityFactory() {
		return new EntityBuilder();
	}
}
