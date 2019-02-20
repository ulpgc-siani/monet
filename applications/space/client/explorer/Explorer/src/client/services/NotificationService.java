package client.services;

import client.core.model.Field;
import client.core.model.Node;
import client.core.model.User;

public interface NotificationService extends Service {

    void registerSource(MessageSource source);
    void notify(MessageType messageType, Message message);

    void registerListener(UpdateTaskStateListener listener);
    void registerListener(UpdateAccountListener listener);
    void registerListener(UpdateFieldListener listener);
    void registerListener(AddFieldListener listener);
    void registerListener(DeleteFieldListener listener);
    void registerListener(UpdateNodeListener listener);

    void deregisterListener(UpdateTaskStateListener listener);
    void deregisterListener(UpdateAccountListener listener);
    void deregisterListener(UpdateFieldListener listener);
    void deregisterListener(AddFieldListener listener);
    void deregisterListener(DeleteFieldListener listener);
    void deregisterListener(UpdateNodeListener listener);


    interface MessageSource {
        void register(NotificationService service);
    }

    interface Message {
        String getString(String name);
        int getInteger(String name);
        Node getNode();
    }
    
    enum  MessageType {
        UPDATE_ACCOUNT, UPDATE_TASK_STATE, UPDATE_FIELD, ADD_FIELD, DELETE_FIELD, UPDATE_NODE
    }

    interface UpdateTaskStateListener {
        void notify(String id);
    }

    interface UpdateAccountListener {
        void notify(User user);
    }

    interface UpdateFieldListener {
        void notify(String fieldPath, String entityId, Object value);
    }

    interface AddFieldListener {
        void notify(String fieldPath, String entityId, Field field, int position);
    }

    interface DeleteFieldListener {
        void notify(String fieldPath, String entityId, int position);
    }

    interface UpdateNodeListener {
        void notify(Node node);
    }
}
