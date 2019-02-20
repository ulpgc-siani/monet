package client.services.http;

import client.core.model.Node;
import client.core.model.Space;
import client.services.NotificationService;
import client.services.NotificationService.MessageType;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.query.client.js.JsMap;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PushService implements client.services.PushService {
    private final Map<String, MessageType> messages;
    private final Space space;
    private NotificationService service;

    public PushService(Space space) {
        this.space = space;
        messages = new HashMap<>();
        messages.put(Message.UPDATE_TASK_STATE, MessageType.UPDATE_TASK_STATE);
        messages.put(Message.UPDATE_ACCOUNT, MessageType.UPDATE_ACCOUNT);
	}

    @Override
    public void init() {
        initPushClient(space.getInstanceId(), space.getConfiguration().getPushUrl());
    }

    @Override
    public void register(NotificationService service) {
        this.service = service;
    }

    @Override
    public void onMessageReceived(String jsonMessage) {
        Logger.getLogger("ApplicationLogger").log(Level.INFO, "Push message received - " + jsonMessage);
        final Message message = Message.parse(jsonMessage);
        if (messages.containsKey(message.getName()))
            service.notify(messages.get(message.getName()), message);
    }

    private native void initPushClient(String instanceId, String pushUri)/*-{
		var pushClient = new $wnd.PushClient(instanceId, pushUri);
        pushClient.onPushReceived = $entry(function(jsonMessage) {
            for (var i = 0; i < this.listeners.length; i++)
                this.listeners[i].@client.services.http.PushService::onMessageReceived(Ljava/lang/String;)(jsonMessage);
        });
		pushClient.addListener(this);
		pushClient.init();
	}-*/;

    private static class Message implements NotificationService.Message {
        private String name;
        private final Map<String, String> parameters = new HashMap<>();

        public static final String UPDATE_TASK_STATE = "updatetaskstate";
	    public static final String UPDATE_ACCOUNT = "updateaccount";

        public String getName() {
            return name;
        }

        @Override
        public String getString(String name) {
            if (!parameters.containsKey(name))
                return "";
            return parameters.get(name);
        }

        @Override
        public int getInteger(String name) {
            return Integer.valueOf(getString(name));
        }

        @Override
        public Node getNode() {
            return null;
        }

        public static Message parse(String json) {
            Message message = new Message();
            HttpInstance jsonMessage = JsonUtils.safeEval(json);
            JsMap<String, String> parameters = jsonMessage.getMap("data");

            message.name = jsonMessage.getString("op");
            for (String name : parameters.keys())
                message.parameters.put(name, (String)jsonMessage.getMapObject("data", name));

            return message;
        }
    }
}
