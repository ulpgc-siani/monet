package org.monet.space.explorer.control.dialogs;

import com.google.inject.Inject;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.control.dialogs.deserializers.field.AbstractFieldDeserializer;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.model.*;

public class SaveFieldDialog extends HttpDialog {

	private LayerProvider layerProvider;

	@Inject
	public void inject(LayerProvider layerProvider) {
		this.layerProvider = layerProvider;
	}

	public Node getNode() {
		return layerProvider.getNodeLayer().loadNode(getEntityId());
	}

	public Attribute getFieldAttribute() {
        final Attribute attribute = deserializeField(getString(Parameter.FIELD));
        final Field field = new Field(getNode(), attribute, null);
        field.setFieldDeclaration(dictionary.getFieldDefinition(attribute.getCode()));
        return field.getAttribute();
	}

    public String getPath() {
		return getString(Parameter.PATH);
	}

    public String getInstanceId() {
        return getString(Parameter.INSTANCE_ID);
    }

    private Attribute deserializeField(String field) {
        return AbstractFieldDeserializer.deserialize(field, createDeserializerHelper());
    }
}
