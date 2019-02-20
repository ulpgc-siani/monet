package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.metamodel.LineActionPropertyBase;
import org.monet.metamodel.PlaceProperty;
import org.monet.metamodel.TaskDefinition;
import org.monet.space.explorer.control.displays.serializers.factory.TaskTypeAdapterFactory;
import org.monet.space.kernel.machines.ttm.model.Provider;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Role;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.TaskOrder;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

public class TaskSerializer<T extends TaskDefinition> extends EntitySerializer<Task, T> {

	public TaskSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(Task task) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapterFactory(new TaskTypeAdapterFactory(helper));
		return builder.create().toJsonTree(task);
	}

	@Override
	public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(task, type, jsonSerializationContext);

		result.addProperty("updateDate", task.getInternalUpdateDate().getTime());
		result.addProperty("state", task.getState());
		result.addProperty("type", task.getDefinition().getType().toString());
		result.add("shortcuts", serializeShortcuts(task.getShortcutsInstances()));
		result.add("workMap", serializeWorkMap(task, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeShortcuts(Map<String, Node> shortcuts) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Node.class, new EntitySerializer<>(helper));
		return builder.create().toJsonTree(shortcuts);
	}

	private JsonElement serializeWorkMap(Task task, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();
		result.add("place", serializePlace(task, jsonSerializationContext));
		return result;
	}

	private JsonElement serializePlace(Task task, JsonSerializationContext jsonSerializationContext) {
		PlaceProperty placeDefinition = task.getProcess().getCurrentPlace();

		JsonObject result = new JsonObject();
		result.addProperty("code", placeDefinition.getCode());
		result.add("action", serializeAction(task, task.getClass(), jsonSerializationContext));
		return result;
	}

	private JsonElement serializeAction(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
		AbstractActionSerializer serializer = (AbstractActionSerializer) getActionSerializer(task, helper);
		return serializer.serialize(task, type, jsonSerializationContext);
	}

	private ActionSerializer getActionSerializer(Task task, Helper helper) {
		PlaceProperty placeDefinition = task.getProcess().getCurrentPlace();

		if (placeDefinition.getDelegationActionProperty() != null)
			return new DelegationActionSerializer(helper);
		if (placeDefinition.getSendJobActionProperty() != null)
			return new NotImplementedActionSerializer(helper);
		else if (placeDefinition.getLineActionProperty() != null)
			return new LineActionSerializer(helper);
		else if (placeDefinition.getEditionActionProperty() != null)
			return new EditionActionSerializer(helper);
		else if (placeDefinition.getEnrollActionProperty() != null)
			return new NotImplementedActionSerializer(helper);
		else if (placeDefinition.getWaitActionProperty() != null)
			return new WaitActionSerializer(helper);

		return new NullActionSerializer(helper);
	}

	public interface ActionSerializer extends JsonSerializer<Task>, Serializer<Task, JsonElement> {
		JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext);
	}

	private abstract static class AbstractActionSerializer extends AbstractSerializer<Task> implements ActionSerializer {

		public AbstractActionSerializer(Helper helper) {
			super(helper);
		}

		public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
			JsonObject result = new JsonObject();
			result.addProperty("type", getType(task));
			return result;
		}

		protected JsonObject serializeNode(Node node) {

			if (node == null)
				return null;

			JsonObject result = new JsonObject();
			result.addProperty("id", node.getId());
			result.addProperty("type", node.getType().toString());
			result.addProperty("label", node.getLabel());
			result.add("definition", serializeDefinition(node.getDefinition()));

			return result;
		}

		private String getType(Task task) {
			PlaceProperty currentPlace = task.getProcess().getCurrentPlace();

			if (currentPlace.getDelegationActionProperty() != null)
				return "delegation";
			if (currentPlace.getSendJobActionProperty() != null)
				return "send_job";
			else if (currentPlace.getLineActionProperty() != null)
				return "line";
			else if (currentPlace.getEditionActionProperty() != null)
				return "edition";
			else if (currentPlace.getEnrollActionProperty() != null)
				return "enroll";
			else if (currentPlace.getWaitActionProperty() != null)
				return "wait";

			return null;
		}

		@Override
		public JsonElement serializeObject(Task object) {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			builder.registerTypeAdapter(Task.class, this);
			return builder.create().toJsonTree(object, Task.class);
		}
	}

	public static class DelegationActionSerializer extends AbstractActionSerializer {

		public DelegationActionSerializer(Helper helper) {
			super(helper);
		}

		@Override
		public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
			JsonObject result = (JsonObject) super.serialize(task, type, jsonSerializationContext);

			if (task.getProcess().getCurrentProvider() == null) {
				result.addProperty("failureDate", new Date().getTime());
				result.addProperty("step", -1);
				result.add("orderNode", new JsonObject());
				result.add("role", new JsonObject());
				return result;
			}

			Provider provider = task.getProcess().getCurrentProvider().getModel();
			Date failureDate = provider.getFailureDate();
			String orderId = provider.getOrderId();
			TaskOrder order = orderId != null ? helper.loadTaskOrder(orderId) : null;
			Node setupNode = order != null && order.getSetupNodeId() != null ? helper.loadNode(order.getSetupNodeId()) : null;

			if (failureDate != null)
				result.addProperty("failureDate", failureDate.getTime());

			result.addProperty("step", serializeStep(task, order));
			result.add("orderNode", serializeNode(setupNode));
			result.add("role", serializeRole(order));

			return result;
		}

		private String serializeStep(Task task, TaskOrder order) {
            if (!task.isPending())
			    return "sending";
            return (order == null || order.getRole() == null) ? "setup_role" : "setup_order";
		}

		private JsonElement serializeRole(TaskOrder order) {
			Role role = order != null ? order.getRole() : null;

			if (role == null)
				return null;

			JsonObject result = new JsonObject();
			result.addProperty("id", role.getId());
			result.addProperty("type", role.getType().toString());
			result.addProperty("label", role.getLabel());

			return result;
		}

	}

	public static class NotImplementedActionSerializer extends AbstractActionSerializer {
		public NotImplementedActionSerializer(Helper helper) {
			super(helper);
		}

		@Override
		public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
			return null;
		}
	}

	public static class LineActionSerializer extends AbstractActionSerializer {
		public LineActionSerializer(Helper helper) {
			super(helper);
		}

		@Override
		public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
			JsonObject result = (JsonObject) super.serialize(task, type, jsonSerializationContext);

			PlaceProperty placeDefinition = task.getProcess().getCurrentPlace();
			LineActionPropertyBase.TimeoutProperty timeoutDefinition = placeDefinition.getLineActionProperty().getTimeout();

			result.addProperty("dueDate", timeoutDefinition != null ? task.getProcess().getTimerDue(placeDefinition.getCode()) : -1);

			return result;
		}

	}

	public static class EditionActionSerializer extends AbstractActionSerializer {
		public EditionActionSerializer(Helper helper) {
			super(helper);
		}

		@Override
		public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
			JsonObject result = (JsonObject) super.serialize(task, type, jsonSerializationContext);

			String formId = task.getProcess().getEditionFormId();
			Node form = (formId != null && !formId.isEmpty()) ? helper.loadNode(formId) : null;

			result.add("form", serializeNode(form));

			return result;
		}
	}

	public static class WaitActionSerializer extends AbstractActionSerializer {
		public WaitActionSerializer(Helper helper) {
			super(helper);
		}

		@Override
		public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
			JsonObject result = (JsonObject) super.serialize(task, type, jsonSerializationContext);

			PlaceProperty placeDefinition = task.getProcess().getCurrentPlace();

			result.addProperty("dueDate", task.getProcess().getTimerDue(placeDefinition.getCode()));

			return result;
		}
	}

	public static class NullActionSerializer extends AbstractActionSerializer {
		public NullActionSerializer(Helper helper) {
			super(helper);
		}

		@Override
		public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
			return null;
		}
	}
}
