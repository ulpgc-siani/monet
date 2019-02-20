package client.services.http;

import client.core.model.List;
import client.core.model.User;
import client.core.system.MonetList;
import client.services.Services;

import java.util.HashMap;
import java.util.Map;

public class NotificationService extends HttpService implements client.services.NotificationService {

    private final Map<MessageType, Notifier> notifiers = new HashMap<>();
    private final List<UpdateAccountListener> updateAccountListeners = new MonetList<>();
    private final List<UpdateTaskStateListener> updateTaskStateListeners = new MonetList<>();
    private final List<UpdateFieldListener> updateFieldListeners = new MonetList<>();
    private final List<AddFieldListener> addFieldListeners = new MonetList<>();
    private final List<DeleteFieldListener> deleteFieldListeners = new MonetList<>();
    private final List<UpdateNodeListener> updateNodeListeners = new MonetList<>();

    public NotificationService(Stub stub, Services services) {
        super(stub, services);
        notifiers.put(MessageType.UPDATE_ACCOUNT, new Notifier() {
            @Override
            public void notify(Message message) {
                notifyUpdateAccount(message);
            }
        });
        notifiers.put(MessageType.UPDATE_TASK_STATE, new Notifier() {
            @Override
            public void notify(Message message) {
                notifyUpdateTaskState(message);
            }
        });
        notifiers.put(MessageType.ADD_FIELD, new Notifier() {
            @Override
            public void notify(Message message) {
                notifyAddField(message);
            }
        });
        notifiers.put(MessageType.UPDATE_FIELD, new Notifier() {
            @Override
            public void notify(Message message) {
                notifyUpdateField(message);
            }
        });
        notifiers.put(MessageType.DELETE_FIELD, new Notifier() {
            @Override
            public void notify(Message message) {
                notifyDeleteField(message);
            }
        });
        notifiers.put(MessageType.UPDATE_NODE, new Notifier() {
            @Override
            public void notify(Message message) {
                notifyUpdateNode(message);
            }
        });
    }

    @Override
    public void registerSource(MessageSource source) {
        source.register(this);
    }

    @Override
    public void notify(MessageType messageType, Message message) {
        notifiers.get(messageType).notify(message);
    }

    @Override
    public void registerListener(UpdateAccountListener listener) {
        updateAccountListeners.add(listener);
    }

    @Override
    public void registerListener(UpdateTaskStateListener listener) {
        updateTaskStateListeners.add(listener);
    }

    @Override
    public void registerListener(UpdateFieldListener listener) {
        updateFieldListeners.add(listener);
    }

    @Override
    public void registerListener(AddFieldListener listener) {
        addFieldListeners.add(listener);
    }

    @Override
    public void registerListener(DeleteFieldListener listener) {
        deleteFieldListeners.add(listener);
    }

    @Override
    public void registerListener(UpdateNodeListener listener) {
        updateNodeListeners.add(listener);
    }

    @Override
    public void deregisterListener(UpdateTaskStateListener listener) {
        updateTaskStateListeners.remove(listener);
    }

    @Override
    public void deregisterListener(UpdateAccountListener listener) {
        updateAccountListeners.remove(listener);
    }

    @Override
    public void deregisterListener(UpdateFieldListener listener) {
        updateFieldListeners.remove(listener);
    }

    @Override
    public void deregisterListener(AddFieldListener listener) {
        addFieldListeners.remove(listener);
    }

    @Override
    public void deregisterListener(DeleteFieldListener listener) {
        deleteFieldListeners.remove(listener);
    }

    @Override
    public void deregisterListener(UpdateNodeListener listener) {
        updateNodeListeners.remove(listener);
    }

    private void notifyUpdateAccount(Message message) {
        for (UpdateAccountListener listener : updateAccountListeners)
            listener.notify(createUser(message));
    }

    private void notifyUpdateTaskState(Message message) {
        for (UpdateTaskStateListener listener : updateTaskStateListeners)
            listener.notify(message.getString("task"));
    }

    private void notifyAddField(Message message) {
        for (AddFieldListener listener : addFieldListeners)
            listener.notify(message.getString("fieldPath"), message.getString("entity"), stub.buildField(message.getString("field")), message.getInteger("position"));
    }

    private void notifyUpdateField(Message message) {
        for (UpdateFieldListener listener : updateFieldListeners)
            listener.notify(message.getString("field"), message.getString("entity"), stub.buildFieldValue(message.getString("type"), message.getString("value")));
    }

    private void notifyDeleteField(Message message) {
        for (DeleteFieldListener listener : deleteFieldListeners)
            listener.notify(message.getString("field"), message.getString("entity"), message.getInteger("position"));
    }

    private void notifyUpdateNode(Message message) {
        for (UpdateNodeListener listener : updateNodeListeners)
            listener.notify(message.getNode());
    }

    private User createUser(Message message) {
        client.core.system.User user = new client.core.system.User(message.getString("fullName"), message.getString("email"), message.getString("photo"));
        user.setId(message.getString("id"));
        return user;
    }

    private interface Notifier {
        void notify(Message message);
    }
}
