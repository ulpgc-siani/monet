package client.presenters.displays.entity.field;

import client.core.model.definition.entity.field.UriFieldDefinition;
import client.core.model.Node;
import client.core.model.fields.UriField;
import client.core.model.types.Uri;
import client.presenters.displays.entity.FieldDisplay;

public class UriFieldDisplay extends FieldDisplay<UriField, UriFieldDefinition, Uri> implements IsUriFieldDisplay {

	public static final Type TYPE = new Type("UriFieldDisplay", FieldDisplay.TYPE);

	public UriFieldDisplay(Node node, UriField field) {
		super(node, field);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public String getValueAsString() {
		return getValue().toString();
	}

	@Override
	protected Uri format(Uri uri) {
		return uri;
	}

	@Override
	public Uri createUri(String value) {
		return getEntityFactory().createUri(value);
	}

	@Override
	public void addHook(Hook hook) {
		super.addHook(hook);
	}

	public interface Hook extends FieldDisplay.Hook {
		void value();
	}
}
