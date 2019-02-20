package client.services.http;

import client.ApplicationTestCase;
import client.core.model.*;
import client.core.model.definition.Definition;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.factory.EntityFactory;
import client.core.model.types.Check;
import client.core.model.types.Term;
import client.core.model.workmap.Action;
import client.core.system.NodeIndexEntry;
import client.services.AccountService;
import client.services.*;
import client.services.IndexService;
import client.services.NewsService;
import client.services.NodeService;
import client.services.NotificationService;
import client.services.SourceService;
import client.services.TaskService;
import client.services.callback.DefinitionCallback;
import client.services.callback.SpaceCallback;
import client.services.http.builders.*;
import client.services.http.builders.definition.DefinitionBuilder;
import client.services.http.builders.definition.entity.EntityDefinitionBuilder;
import client.services.http.builders.types.CheckBuilder;
import client.services.http.builders.types.TermBuilder;
import client.services.http.builders.workmap.ActionBuilder;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

import java.util.HashMap;
import java.util.Map;

public abstract class ServiceTest extends ApplicationTestCase {

	protected abstract <T extends client.services.Service> T createService();

	protected Stub createStub() {
		return new Stub() {
			private boolean logged = false;
			private String HOST_URL = GWT.getHostPageBaseURL().replace("/Application.JUnit/", "");
			private String EXPLORER_URL = HOST_URL + "/monet/explorer";
			private String FEDERATION_URL = HOST_URL + "/federation";

			@Override
			public String getRequestUrl(Dialog dialog) {
				return constructApiUrl(dialog);
			}

			@Override
			public <T> void request(final Dialog dialog, final Instance.ClassName className, final client.services.callback.Callback<T> callback) {
				if (logged)
					requestObject(dialog, className, callback);
				else
					new ExplorerProxy().login(FEDERATION_URL, EXPLORER_URL, new Callback() {
						@Override
						public void onFailure(Object reason) {
						}

						@Override
						public void onSuccess(Object result) {
							logged = true;
							requestObject(dialog, className, callback);
						}
					});

				delayTestFinish(100000);
			}

			@Override
			public void requestString(Dialog dialog, client.services.callback.Callback<String> callback) {
			}

			@Override
			public void requestFile(Dialog dialog, client.services.callback.Callback<String> callback) {

			}

            @Override
            public Field buildField(String field) {
                return null;
            }

            @Override
            public Object buildFieldValue(String fieldType, String value) {
                return null;
            }

            private <T> void requestObject(Dialog dialog, final Instance.ClassName className, final client.services.callback.Callback<T> callback) {
				RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, constructApiUrl(dialog));

				try {
					builder.setHeader("Content-type", "application/x-www-form-urlencoded");
					builder.sendRequest(dialog.serialize(), new RequestCallback() {
						@Override
						public void onResponseReceived(Request request, Response response) {
							if (failure(response, callback))
								return;

							if (isVoidCallback(className, callback))
								return;

							if (!builderMap.containsKey(className)) {
								callback.failure("Builder for class " + className.toString() + " not found!");
								return;
							}

							Builder<T, List<T>> builder = builderMap.get(className);
							HttpInstance instance = JsonUtils.safeEval(response.getText());

							callback.success(instance.isList()?(T)builder.buildList((HttpList)instance.cast()):builder.build(instance));
						}

						private <T> boolean failure(Response response, client.services.callback.Callback<T> callback) {

							if (response.getStatusCode() != Response.SC_INTERNAL_SERVER_ERROR)
								return false;

							callback.failure(response.getText());
							return true;
						}

						private <T> boolean isVoidCallback(Instance.ClassName className, client.services.callback.Callback<T> callback) {

							if (className != null)
								return false;

							callback.success(null);
							return true;
						}

						@Override
						public void onError(Request request, Throwable exception) {
							callback.failure("Communication with server failure " + exception.getMessage());
						}
					});
				} catch (Throwable caught) {
					callback.failure("Communication with server failure " + caught.getMessage());
				}
			}

			private String constructApiUrl(Dialog dialog) {
				String entityId = dialog.getEntityId();
				return EXPLORER_URL + "/api/" + dialog.getOperation() + (entityId != null ? "/" + entityId : "");
			}

		};
	}

	protected client.services.Services createServices() {
		return new Services() {

			@Override
			public client.services.SpaceService getSpaceService() {
				return new client.services.SpaceService() {
					client.core.system.Space space = new client.core.system.Space();
					client.services.http.SpaceService spaceService = new client.services.http.SpaceService(createStub(), null);

					@Override
					public void load(SpaceCallback callback) {
						callback.success(space);
					}

					@Override
					public void loadDefinition(String definitionKey, Instance.ClassName definitionClassName, DefinitionCallback<EntityDefinition> callback) {
						spaceService.loadDefinition(definitionKey, definitionClassName, callback);
					}

					@Override
					public <T extends Entity> void loadDefinition(T entity, DefinitionCallback<EntityDefinition> callback) {
						spaceService.loadDefinition(entity, callback);
					}

					@Override
					public Space load() {
						space.setInstanceId("abcd-efgh-1234-5678");
						return space;
					}

					@Override
					public EntityFactory getEntityFactory() {
						return spaceService.getEntityFactory();
					}
				};
			}

			@Override
			public NodeService getNodeService() {
				return null;
			}

			@Override
			public TaskService getTaskService() {
				return null;
			}

			@Override
			public AccountService getAccountService() {
				return null;
			}

			@Override
			public SourceService getSourceService() {
				return null;
			}

			@Override
			public IndexService getIndexService() {
				return null;
			}

			@Override
			public NewsService getNewsService() {
				return null;
			}

			@Override
			public client.services.TranslatorService getTranslatorService() {
				return new client.services.http.TranslatorService(createStub(), null);
			}

            @Override
            public NotificationService getNotificationService() {
                return null;
            }
		};
	}

	protected static final Map<Instance.ClassName, Builder> builderMap = new HashMap<Instance.ClassName, Builder>() {{
		put(Space.CLASS_NAME, new SpaceBuilder());
		put(Node.CLASS_NAME, new NodeBuilder());
		put(Definition.CLASS_NAME, new DefinitionBuilder());
		put(EntityDefinition.CLASS_NAME, new EntityDefinitionBuilder());
		put(NodeIndexEntry.CLASS_NAME, new NodeIndexEntryBuilder());
		put(Filter.Option.CLASS_NAME, new FilterBuilder.OptionBuilder());
		put(Notification.CLASS_NAME, new NotificationBuilder());
		put(BusinessUnit.CLASS_NAME, new BusinessUnitBuilder());
		put(Source.CLASS_NAME, new SourceBuilder());
		put(Term.CLASS_NAME, new TermBuilder());
		put(Check.CLASS_NAME, new CheckBuilder());
		put(Task.CLASS_NAME, new TaskBuilder());
		put(Role.CLASS_NAME, new RoleBuilder());
		put(Action.CLASS_NAME, new ActionBuilder());
		put(Report.CLASS_NAME, new ReportBuilder());
	}};
}
