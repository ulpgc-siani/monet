package client.services.http;

import client.core.model.*;
import client.core.model.definition.Definition;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.types.Check;
import client.core.model.types.Term;
import client.core.model.types.TermList;
import client.core.model.workmap.Action;
import client.services.Dialog;
import client.services.callback.Callback;
import client.services.http.builders.*;
import client.services.http.builders.definition.DefinitionBuilder;
import client.services.http.builders.definition.entity.EntityDefinitionBuilder;
import client.services.http.builders.fields.FieldBuilder;
import client.services.http.builders.types.*;
import client.services.http.builders.workmap.ActionBuilder;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static client.core.model.Instance.ClassName;

public class HttpStub implements Stub {
	private final String location;
	private final ErrorHandler handler;

	protected static final Map<ClassName, Builder> builderMap = new HashMap<ClassName, Builder>() {{
		put(Space.CLASS_NAME, new SpaceBuilder());
		put(Space.Action.CLASS_NAME, new SpaceActionBuilder());
		put(Node.CLASS_NAME, new NodeBuilder());
		put(Definition.CLASS_NAME, new DefinitionBuilder());
		put(EntityDefinition.CLASS_NAME, new EntityDefinitionBuilder());
		put(Field.CLASS_NAME, new FieldBuilder());
		put(TermList.CLASS_NAME, new TermBuilder());
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
		put(Fact.CLASS_NAME, new FactBuilder());
		put(HtmlPage.CLASS_NAME, new HtmlPageBuilder());
	}};

    private static final Map<String, FieldValueBuilder> valueBuilders = new HashMap<>();
    static {
        valueBuilders.put("boolean", new FieldValueBuilder() {
            @Override
            public Object build(HttpInstance instance) {
                return Boolean.valueOf(instance.asString());
            }
        });
        valueBuilders.put("check", new FieldValueBuilder() {
            @Override
            public Object build(HttpInstance instance) {
                return new CheckBuilder().buildList(instance.getList("terms"));
            }
        });
        valueBuilders.put("composite", new FieldValueBuilder() {
            @Override
            public Object build(HttpInstance instance) {
                return Boolean.valueOf(instance.asString());
            }
        });
        valueBuilders.put("date", new FieldValueBuilder() {
            @Override
            public Object build(HttpInstance instance) {
                return instance.getString("formattedValue");
            }
        });
        valueBuilders.put("file", new FieldValueBuilder() {
            @Override
            public Object build(HttpInstance instance) {
                return new FileBuilder().build(instance);
            }
        });
        valueBuilders.put("link", new FieldValueBuilder() {
            @Override
            public Object build(HttpInstance instance) {
                return new LinkBuilder().build(instance);
            }
        });
        valueBuilders.put("memo", new FieldValueBuilder() {
            @Override
            public Object build(HttpInstance instance) {
                return instance.asString();
            }
        });
        valueBuilders.put("number", new FieldValueBuilder() {
            @Override
            public Object build(HttpInstance instance) {
                return new NumberBuilder().build(instance);
            }
        });
        valueBuilders.put("select", new FieldValueBuilder() {
            @Override
            public Object build(HttpInstance instance) {
                return new TermBuilder().build(instance);
            }
        });
        valueBuilders.put("serial", new FieldValueBuilder() {
            @Override
            public Object build(HttpInstance instance) {
                return instance.asString();
            }
        });
        valueBuilders.put("text", new FieldValueBuilder() {
            @Override
            public Object build(HttpInstance instance) {
                return instance.asString();
            }
        });
    }

	public HttpStub(String location, ErrorHandler handler) {
		this.location = location;
		this.handler = handler;
	}

	@Override
	public String getRequestUrl(Dialog dialog) {
		String parameters = dialog.serialize();
		return constructUrl(dialog) + (!parameters.isEmpty()?"?" + parameters:"");
	}

	@Override
	public <T> void request(Dialog dialog, final ClassName className, final Callback<T> callback) {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, constructUrl(dialog));

		Logger.getLogger("ApplicationLogger").log(Level.FINEST, "SERVER CALL: " + className + "#" + constructUrl(dialog) + "#");

		try {
			requestBuilder.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
			requestBuilder.sendRequest(dialog.serialize(), new RequestCallback() {
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
					HttpInstance instance = JsonUtils.safeEval(URL.decodeQueryString(response.getText()));

					if (instance == null) {
						callback.success(null);
						return;
					}

					callback.success(instance.isList() ? (T)builder.buildList((HttpList)instance.cast()) : builder.build(instance));
				}

				private <T> boolean isVoidCallback(ClassName className, Callback<T> callback) {

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
		}
		catch (Throwable caught) {
			callback.failure("Communication with server failure " + caught.getMessage());
		}
	}

	@Override
	public void requestFile(final Dialog dialog, final Callback<String> callback) {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, constructUrl(dialog));

		Logger.getLogger("ApplicationLogger").log(Level.FINEST, "SERVER CALL: saveFile#" + constructUrl(dialog) + "#");

		try {
			requestBuilder.setHeader("Content-type", "multipart/form-data;charset=UTF-8");
			requestBuilder.sendRequest(dialog.serialize(), new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (failure(response, callback)) return;

					if (response.getText().isEmpty())
						callback.failure("No response");
					else
						callback.success(URL.decodeQueryString(response.getText()));
				}

				@Override
				public void onError(Request request, Throwable exception) {
					callback.failure("Communication with server failure " + exception.getMessage());
				}
			});
		}
		catch (Throwable caught) {
			callback.failure("Communication with server failure " + caught.getMessage());
		}
	}

    @Override
    public Field buildField(String field) {
        return new FieldBuilder<>().build((HttpInstance) JsonUtils.safeEval(field));
    }

    @Override
    public Object buildFieldValue(String fieldType, String value) {
        if (valueBuilders.containsKey(fieldType))
            return valueBuilders.get(fieldType).build((HttpInstance) JsonUtils.safeEval(value));
        return null;
    }

    @Override
	public void requestString(Dialog dialog, final Callback<String> callback) {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, constructUrl(dialog));

		Logger.getLogger("ApplicationLogger").log(Level.FINEST, "SERVER CALL: " + "#" + constructUrl(dialog) + "#");

		try {
			requestBuilder.setHeader("Content-type", "application/x-www-form-urlencoded");
			requestBuilder.sendRequest(dialog.serialize(), new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (failure(response, callback))
						return;

					HttpInstance result = JsonUtils.safeEval(URL.decodeQueryString(response.getText()));
					callback.success(result.asString());
				}

				@Override
				public void onError(Request request, Throwable exception) {
					callback.failure("Communication with server failure " + exception.getMessage());
				}
			});
		}
		catch (Throwable caught) {
			callback.failure("Communication with server failure " + caught.getMessage());
		}
	}

	private <T> boolean failure(Response response, Callback<T> callback) {
		int statusCode = response.getStatusCode();

		if (statusCode >= 200 && statusCode < 300)
			return false;

		String responseText = response.getText();
		if (!handler.onError(responseText))
			callback.failure(responseText);

		return true;
	}

	private String constructUrl(Dialog dialog) {
		String entityId = dialog.getEntityId();
		return location + "/" + dialog.getOperation() + (entityId != null ? "/" + entityId : "");
	}

	public interface ErrorHandler {
		boolean onError(String error);
	}

    private interface FieldValueBuilder {
        Object build(HttpInstance instance);
    }
}
