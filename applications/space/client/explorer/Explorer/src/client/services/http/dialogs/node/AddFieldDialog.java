package client.services.http.dialogs.node;

import client.core.model.Field;
import client.core.model.Node;
import client.core.model.Space;
import client.services.http.dialogs.HttpDialog;
import client.services.http.serializers.fields.FieldSerializer;

public class AddFieldDialog extends HttpDialog {

    private final Node node;

    public AddFieldDialog(Node node, Field parent, Field field, int pos, Space space) {
        this.node = node;

        add("instance_id", space.getInstanceId());
        add("parent", parent.getPath());
        add("field", new FieldSerializer().serialize(field));
        add("pos", pos);
    }

    @Override
    public String getOperation() {
        return "field$add";
    }

    @Override
    public String getEntityId() {
        return node.getId();
    }

}
