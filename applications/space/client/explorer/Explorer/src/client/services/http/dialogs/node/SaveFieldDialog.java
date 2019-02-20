package client.services.http.dialogs.node;

import client.core.model.Field;
import client.core.model.Node;
import client.core.model.Space;
import client.services.http.dialogs.HttpDialog;
import client.services.http.serializers.fields.FieldSerializer;

public class SaveFieldDialog extends HttpDialog {
    private final Node node;

    public SaveFieldDialog(Node node, Field field, Space space) {
        this.node = node;

        if (field == null)
            return;

        add("field", new FieldSerializer().serialize(field));
        add("path", field.getPath());
        add("view", node.getViews().getDefaultView().getKey().getCode());
        add("instance_id", space.getInstanceId());
    }

    @Override
    public String getOperation() {
        return "field$save";
    }

    @Override
    public String getEntityId() {
        return node.getId();
    }

}
